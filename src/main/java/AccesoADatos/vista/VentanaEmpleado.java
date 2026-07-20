package AccesoADatos.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaEmpleado extends JFrame {

    public JTextField txtId = new JTextField();
    public JTextField txtNombre = new JTextField();
    public JSpinner spnEdad = new JSpinner(new SpinnerNumberModel(18, 18, 100, 1));
    public JCheckBox chkActivo = new JCheckBox("Empleado Activo");
    public JTextField txtFecha = new JTextField();

    public JButton btnGuardar = new JButton("Guardar / Actualizar");
    public JButton btnLimpiar = new JButton("Limpiar Formulario");
    public JButton btnEliminar = new JButton("Eliminar Seleccionado");

    public JTextField txtBuscar = new JTextField(15);
    public JButton btnBuscar = new JButton("Buscar");
    public JTable tablaEmpleados;
    public DefaultTableModel modeloTabla;

    public JButton btnAnterior = new JButton("< Anterior");
    public JButton btnSiguiente = new JButton("Siguiente >");
    public JLabel lblPagina = new JLabel("Pág. 1");

    public VentanaEmpleado() {
        setTitle("Gestión de Empleados - CRUD Paginado");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        txtId.setEditable(false);

        // Panel Izquierdo
        JPanel panelIzq = new JPanel(new GridLayout(6, 2, 10, 10));
        panelIzq.setBorder(BorderFactory.createTitledBorder("Datos del Empleado"));
        panelIzq.add(new JLabel("ID:")); panelIzq.add(txtId);
        panelIzq.add(new JLabel("Nombre Completo:")); panelIzq.add(txtNombre);
        panelIzq.add(new JLabel("Edad:")); panelIzq.add(spnEdad);
        panelIzq.add(new JLabel("Fecha (YYYY-MM-DD):")); panelIzq.add(txtFecha);
        panelIzq.add(new JLabel("Estado:")); panelIzq.add(chkActivo);
        panelIzq.add(btnGuardar); panelIzq.add(btnLimpiar);

        add(panelIzq, BorderLayout.WEST);

        // Panel Central
        JPanel panelDer = new JPanel(new BorderLayout());
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.add(new JLabel("Buscar Nombre:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        panelBusqueda.add(btnEliminar);
        panelDer.add(panelBusqueda, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Activo", "Edad", "Fecha"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaEmpleados = new JTable(modeloTabla);
        panelDer.add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);

        // Panel Inferior
        JPanel panelPag = new JPanel();
        panelPag.add(btnAnterior);
        panelPag.add(lblPagina);
        panelPag.add(btnSiguiente);
        panelDer.add(panelPag, BorderLayout.SOUTH);

        add(panelDer, BorderLayout.CENTER);
    }
}