package AccesoADatos.vista;

import AccesoADatos.dao.PersonaDbDao;
import AccesoADatos.factory.PersistenceFactory;
import AccesoADatos.factory.SqlitePersistenceFactory;
import AccesoADatos.modelo.Persona;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class Registro extends JFrame {

    private static final Logger logger = LogManager.getRootLogger();

    private final PersonaDbDao personaDao;
    private int paginaActual = 1;
    private final int LIMITE = 20;
    private Integer idSeleccionado = null;

    private JTextField txtNombre, txtEdad, txtFecha, txtBuscar;
    private JCheckBox chkActivo;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JButton btnAnterior;

    public Registro() {
        // Implementación del patrón Factory
        PersistenceFactory factory = new SqlitePersistenceFactory();
        personaDao = factory.getPersonaDao();

        setTitle("Registro");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarUI();
        cargarTabla();
    }

    private void inicializarUI() {
        // Panel Formulario (Izquierda)
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos Generales"));

        panelForm.add(new JLabel("Nombre Completo:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Edad:"));
        txtEdad = new JTextField();
        panelForm.add(txtEdad);

        panelForm.add(new JLabel("Fecha (YYYY-MM-DD):"));
        txtFecha = new JTextField();
        panelForm.add(txtFecha);

        panelForm.add(new JLabel("Activo:"));
        chkActivo = new JCheckBox();
        panelForm.add(chkActivo);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        panelForm.add(btnLimpiar);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardar());
        panelForm.add(btnGuardar);

        add(panelForm, BorderLayout.WEST);

        // Panel Tabla y Búsqueda (Centro)
        JPanel panelCentro = new JPanel(new BorderLayout());

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> {
            paginaActual = 1;
            cargarTabla();
        });

        panelBusqueda.add(new JLabel("Buscar por nombre:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelCentro.add(panelBusqueda, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Edad", "Nacimiento", "Activo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() != -1) {
                seleccionarFila();
            }
        });

        panelCentro.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Panel Paginación y Eliminar (Sur)
        JPanel panelSur = new JPanel(new FlowLayout());
        btnAnterior = new JButton("< Anterior");
        btnAnterior.addActionListener(e -> paginar(-1));

        JButton btnSiguiente = new JButton("Siguiente >");
        btnSiguiente.addActionListener(e -> paginar(1));

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminar());

        panelSur.add(btnAnterior);
        panelSur.add(btnSiguiente);
        panelSur.add(new JLabel(" | "));
        panelSur.add(btnEliminar);

        panelCentro.add(panelSur, BorderLayout.SOUTH);
        add(panelCentro, BorderLayout.CENTER);
    }

    private void cargarTabla() {
        try {
            List<Persona> lista = personaDao.listarPaginado(txtBuscar.getText().trim(), paginaActual, LIMITE);
            modeloTabla.setRowCount(0);

            for (Persona p : lista) {
                modeloTabla.addRow(new Object[]{
                        p.getId(), p.getNombre(), p.getEdad(), p.getFechaNacimiento(), p.getActivo()
                });
            }
            logger.info("Consulta exitosa. Página: " + paginaActual + " - Registros mostrados: " + lista.size());
            btnAnterior.setEnabled(paginaActual > 1);

        } catch (Exception ex) {
            logger.error("Fallo al listar elementos", ex);
            JOptionPane.showMessageDialog(this, "Error de base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() {
        try {
            if (txtNombre.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
                return;
            }
            int edad = Integer.parseInt(txtEdad.getText().trim());
            LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());

            Persona persona = new Persona(txtNombre.getText().trim(), chkActivo.isSelected(), edad, fecha);

            if (idSeleccionado == null) {
                personaDao.guardar(persona);
                logger.info("Ítem creado: " + persona.getNombre());
            } else {
                persona.setId(idSeleccionado);
                personaDao.actualizar(persona);
                logger.info("Ítem actualizado ID: " + idSeleccionado);
            }

            limpiarFormulario();
            cargarTabla();
            JOptionPane.showMessageDialog(this, "Operación exitosa.");

        } catch (NumberFormatException | DateTimeParseException ex) {
            logger.warn("Error de formato en formulario", ex);
            JOptionPane.showMessageDialog(this, "Revise los formatos de Edad (número) y Fecha (YYYY-MM-DD).");
        } catch (Exception ex) {
            logger.error("Excepción en base de datos al guardar", ex);
            JOptionPane.showMessageDialog(this, "Error al guardar el ítem.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) return;

        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar ítem definitivamente?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                Persona p = new Persona();
                p.setId(idSeleccionado);

                personaDao.eliminar(p);
                logger.info("Ítem eliminado ID: " + idSeleccionado);

                limpiarFormulario();
                cargarTabla();

            } catch (Exception ex) {
                logger.error("Error al eliminar", ex);
                JOptionPane.showMessageDialog(this, "Fallo al intentar eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void seleccionarFila() {
        int fila = tabla.getSelectedRow();
        idSeleccionado = (Integer) modeloTabla.getValueAt(fila, 0);
        txtNombre.setText((String) modeloTabla.getValueAt(fila, 1));
        txtEdad.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
        txtFecha.setText(modeloTabla.getValueAt(fila, 3).toString());
        chkActivo.setSelected((Boolean) modeloTabla.getValueAt(fila, 4));
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        txtNombre.setText("");
        txtEdad.setText("");
        txtFecha.setText("");
        chkActivo.setSelected(false);
        tabla.clearSelection();
    }

    private void paginar(int dir) {
        paginaActual += dir;
        if (paginaActual < 1) paginaActual = 1;
        cargarTabla();
    }

    public static void main(String[] args) {
        // Se aplica el diseño nativo del sistema operativo para una interfaz más atractiva
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.warn("No se pudo cargar el estilo visual nativo.");
        }
        SwingUtilities.invokeLater(() -> new Registro().setVisible(true));
    }
}