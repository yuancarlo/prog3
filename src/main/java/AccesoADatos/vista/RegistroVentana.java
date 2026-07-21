package AccesoADatos.vista;

import AccesoADatos.dao.PersonaDbDao;
import AccesoADatos.factory.PersistenceFactory;
import AccesoADatos.factory.SqlitePersistenceFactory;
import AccesoADatos.modelo.Persona;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class RegistroVentana extends JFrame {

    private static final Logger logger = LogManager.getRootLogger();

    private final PersonaDbDao personaDao;
    private final PanelFormulario panelFormulario;
    private final PanelTabla panelTabla;
    
    private int paginaActual = 1;
    private final int LIMITE = 20;
    private Integer idSeleccionado = null;

    public RegistroVentana() {
        PersistenceFactory factory = new SqlitePersistenceFactory();
        personaDao = factory.getPersonaDao();

        setTitle("Registro de Personas");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panelFormulario = new PanelFormulario();
        panelTabla = new PanelTabla();

        // Evita que el formulario se estire infinitamente a los lados
        JPanel contenedorOeste = new JPanel(new BorderLayout());
        contenedorOeste.add(panelFormulario, BorderLayout.NORTH);
        
        add(contenedorOeste, BorderLayout.WEST);
        add(panelTabla, BorderLayout.CENTER);

        configurarEventos();
    }

    private void configurarEventos() {
        panelFormulario.getBtnGuardar().addActionListener(e -> guardar());
        panelFormulario.getBtnLimpiar().addActionListener(e -> limpiarFormulario());

        panelTabla.getBtnBuscar().addActionListener(e -> {
            paginaActual = 1;
            cargarDatos();
        });
        
        panelTabla.getBtnAnterior().addActionListener(e -> paginar(-1));
        panelTabla.getBtnSiguiente().addActionListener(e -> paginar(1));
        panelTabla.getBtnEliminar().addActionListener(e -> eliminar());

        panelTabla.getTabla().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && panelTabla.getTabla().getSelectedRow() != -1) {
                seleccionarFila();
            }
        });
    }

    public void cargarDatos() {
        try {
            List<Persona> lista = personaDao.listarPaginado(panelTabla.getTextoBusqueda(), paginaActual, LIMITE);
            panelTabla.getModelo().setRowCount(0); 
            
            for (Persona p : lista) {
                panelTabla.getModelo().addRow(new Object[]{
                        p.getId(), p.getNombre(), p.getEdad(), p.getFechaNacimiento(), p.getActivo()
                });
            }
            logger.info("Datos cargados exitosamente. Página: " + paginaActual);
            panelTabla.getBtnAnterior().setEnabled(paginaActual > 1);
            
        } catch (Exception ex) {
            logger.error("Error al cargar la lista desde SQLite", ex);
        }
    }

    private void guardar() {
        try {
            String nombre = panelFormulario.getNombre();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int edad = Integer.parseInt(panelFormulario.getEdad());
            LocalDate fecha = LocalDate.parse(panelFormulario.getFecha());

            Persona persona = new Persona(nombre, panelFormulario.isActivo(), edad, fecha);

            if (idSeleccionado == null) {
                personaDao.guardar(persona);
                logger.info("Nuevo registro insertado: " + persona.getNombre());
            } else {
                persona.setId(idSeleccionado);
                personaDao.actualizar(persona);
                logger.info("Registro actualizado, ID: " + idSeleccionado);
            }

            limpiarFormulario();
            cargarDatos();
            JOptionPane.showMessageDialog(this, "Operación guardada con éxito.");

        } catch (NumberFormatException | DateTimeParseException ex) {
            logger.warn("El usuario ingresó un formato inválido en el formulario.", ex);
            JOptionPane.showMessageDialog(this, "Revise los datos: La edad debe ser numérica y la fecha YYYY-MM-DD.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            logger.error("Fallo durante la transacción de guardado", ex);
            JOptionPane.showMessageDialog(this, "Error al intentar guardar en la base de datos.", "Error Interno", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un ítem de la tabla.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int conf = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este registro permanentemente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                Persona p = new Persona();
                p.setId(idSeleccionado);
                personaDao.eliminar(p);
                
                logger.info("Registro eliminado de la base de datos, ID: " + idSeleccionado);
                limpiarFormulario();
                cargarDatos();
                
            } catch (Exception ex) {
                logger.error("No se pudo eliminar el registro", ex);
                JOptionPane.showMessageDialog(this, "Fallo al ejecutar la eliminación.", "Error Interno", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void seleccionarFila() {
        int fila = panelTabla.getTabla().getSelectedRow();
        idSeleccionado = (Integer) panelTabla.getModelo().getValueAt(fila, 0);
        
        panelFormulario.cargarDatos(
            (String) panelTabla.getModelo().getValueAt(fila, 1),
            String.valueOf(panelTabla.getModelo().getValueAt(fila, 2)),
            panelTabla.getModelo().getValueAt(fila, 3).toString(),
            (Boolean) panelTabla.getModelo().getValueAt(fila, 4)
        );
    }

    private void limpiarFormulario() {
        idSeleccionado = null;
        panelFormulario.limpiar();
        panelTabla.getTabla().clearSelection();
    }

    private void paginar(int direccion) {
        paginaActual += direccion;
        if (paginaActual < 1) paginaActual = 1;
        cargarDatos();
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.warn("No se pudo establecer el Look and Feel nativo del sistema.");
        }
        
        SwingUtilities.invokeLater(() -> {
            RegistroVentana app = new RegistroVentana();
            app.setVisible(true); 
            app.cargarDatos();    
        });
    }
}