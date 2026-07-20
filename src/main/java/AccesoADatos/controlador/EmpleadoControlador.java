package AccesoADatos.controlador;

import AccesoADatos.dao.Repositorio;
import AccesoADatos.modelo.EmpleadoBd;
import AccesoADatos.vista.VentanaEmpleado;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class EmpleadoControlador {

    private final Logger registradorLog = LogManager.getRootLogger();
    private final VentanaEmpleado vista;
    private final Repositorio<EmpleadoBd, Integer> dao;

    private int paginaActual = 0;
    private final int ELEMENTOS_POR_PAGINA = 20;

    public EmpleadoControlador(VentanaEmpleado vista, Repositorio<EmpleadoBd, Integer> dao) {
        this.vista = vista;
        this.dao = dao;
        iniciarEventos();
        cargarDatosTabla();
    }

    private void iniciarEventos() {
        vista.btnGuardar.addActionListener(e -> guardarOActualizarEmpleado());
        vista.btnLimpiar.addActionListener(e -> limpiarFormulario());
        vista.btnEliminar.addActionListener(e -> eliminarEmpleado());
        vista.btnBuscar.addActionListener(e -> { paginaActual = 0; cargarDatosTabla(); });

        vista.btnSiguiente.addActionListener(e -> { paginaActual++; cargarDatosTabla(); });
        vista.btnAnterior.addActionListener(e -> { if(paginaActual > 0) { paginaActual--; cargarDatosTabla(); } });

        vista.tablaEmpleados.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && vista.tablaEmpleados.getSelectedRow() != -1) {
                cargarFilaAFormulario();
            }
        });
    }

    // TRY/CATCH PERMITIDO EN ESTA CAPA
    private void guardarOActualizarEmpleado() {
        try {
            String nombre = vista.txtNombre.getText().trim();
            String fechaTexto = vista.txtFecha.getText().trim();

            if (nombre.isEmpty() || fechaTexto.isEmpty()) {
                throw new IllegalArgumentException("Nombre y fecha son obligatorios.");
            }

            LocalDate fecha = LocalDate.parse(fechaTexto);
            int edad = (int) vista.spnEdad.getValue();
            boolean activo = vista.chkActivo.isSelected();

            EmpleadoBd empleado = new EmpleadoBd(nombre, activo, edad, fecha);

            if (!vista.txtId.getText().isEmpty()) {
                empleado.setId(Integer.parseInt(vista.txtId.getText()));
            }

            dao.guardarOActualizar(empleado); // Llamada que puede lanzar excepcion
            JOptionPane.showMessageDialog(vista, "Registro exitoso.");

            limpiarFormulario();
            cargarDatosTabla();

        } catch (Exception ex) {
            registradorLog.error("Error al procesar formulario", ex);
            JOptionPane.showMessageDialog(vista, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEmpleado() {
        int fila = vista.tablaEmpleados.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione un registro.");
            return;
        }

        // Ventana de confirmación
        int confirmacion = JOptionPane.showConfirmDialog(vista,
                "¿Desea eliminar el empleado?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = (int) vista.modeloTabla.getValueAt(fila, 0);
                dao.eliminarPorId(id);
                limpiarFormulario();
                cargarDatosTabla();
            } catch (Exception ex) {
                registradorLog.error("Error al eliminar", ex);
                JOptionPane.showMessageDialog(vista, "Fallo al eliminar: " + ex.getMessage());
            }
        }
    }

    private void cargarDatosTabla() {
        try {
            String busqueda = vista.txtBuscar.getText().trim();
            int offset = paginaActual * ELEMENTOS_POR_PAGINA;

            List<EmpleadoBd> lista = dao.buscarPaginado(busqueda, ELEMENTOS_POR_PAGINA, offset);

            vista.modeloTabla.setRowCount(0);
            for (EmpleadoBd e : lista) {
                vista.modeloTabla.addRow(new Object[]{
                        e.getId(), e.getNombreCompleto(), e.isActivo(), e.getEdad(), e.getFechaIngreso()
                });
            }

            vista.lblPagina.setText("Pág. " + (paginaActual + 1));
            vista.btnAnterior.setEnabled(paginaActual > 0);
            vista.btnSiguiente.setEnabled(lista.size() == ELEMENTOS_POR_PAGINA);

        } catch (Exception ex) {
            registradorLog.error("Error al buscar datos", ex);
            JOptionPane.showMessageDialog(vista, "Error al cargar la tabla.");
        }
    }

    private void cargarFilaAFormulario() {
        int fila = vista.tablaEmpleados.getSelectedRow();
        vista.txtId.setText(vista.modeloTabla.getValueAt(fila, 0).toString());
        vista.txtNombre.setText(vista.modeloTabla.getValueAt(fila, 1).toString());
        vista.chkActivo.setSelected((boolean) vista.modeloTabla.getValueAt(fila, 2));
        vista.spnEdad.setValue(vista.modeloTabla.getValueAt(fila, 3));
        vista.txtFecha.setText(vista.modeloTabla.getValueAt(fila, 4).toString());
    }

    private void limpiarFormulario() {
        vista.txtId.setText("");
        vista.txtNombre.setText("");
        vista.txtFecha.setText("");
        vista.chkActivo.setSelected(false);
        vista.spnEdad.setValue(18);
        vista.tablaEmpleados.clearSelection();
    }
}
