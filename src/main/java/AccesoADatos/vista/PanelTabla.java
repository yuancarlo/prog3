package AccesoADatos.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelTabla extends JPanel {

    private JTextField txtBuscar;
    private JButton btnBuscar, btnAnterior, btnSiguiente, btnEliminar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public PanelTabla() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        inicializarComponentes();
    }

    private void inicializarComponentes() {
   
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(25);
        btnBuscar = new JButton("Buscar");
        panelBusqueda.add(new JLabel("Buscar por nombre: "));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        add(panelBusqueda, BorderLayout.NORTH);

    
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Edad", "Nacimiento", "Activo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setReorderingAllowed(false);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout());
        
        JPanel panelPaginacion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAnterior = new JButton("< Anterior");
        btnSiguiente = new JButton("Siguiente >");
        panelPaginacion.add(btnAnterior);
        panelPaginacion.add(btnSiguiente);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEliminar = new JButton("Eliminar Seleccionado");
        btnEliminar.setBackground(new Color(220, 53, 69)); 
        btnEliminar.setForeground(Color.WHITE);
        panelAcciones.add(btnEliminar);

        panelSur.add(panelPaginacion, BorderLayout.WEST);
        panelSur.add(panelAcciones, BorderLayout.EAST);
        add(panelSur, BorderLayout.SOUTH);
    }

    public DefaultTableModel getModelo() { return modeloTabla; }
    public JTable getTabla() { return tabla; }
    public String getTextoBusqueda() { return txtBuscar.getText().trim(); }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnAnterior() { return btnAnterior; }
    public JButton getBtnSiguiente() { return btnSiguiente; }
    public JButton getBtnEliminar() { return btnEliminar; }
}
