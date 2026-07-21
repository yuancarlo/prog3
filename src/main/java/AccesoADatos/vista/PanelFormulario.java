package AccesoADatos.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelFormulario extends JPanel {

    private JTextField txtNombre, txtEdad, txtFecha;
    private JCheckBox chkActivo;
    private JButton btnGuardar, btnLimpiar;

    public PanelFormulario() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Datos Generales"),
                new EmptyBorder(10, 10, 10, 10)
        ));
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

     
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNombre = new JTextField(15);
        add(txtNombre, gbc);

        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        add(new JLabel("Edad:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEdad = new JTextField(15);
        add(txtEdad, gbc);

       
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        add(new JLabel("Nacimiento (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtFecha = new JTextField(15);
        add(txtFecha, gbc);

       
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        chkActivo = new JCheckBox("Usuario Activo");
        add(chkActivo, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnLimpiar = new JButton("Limpiar");
        btnGuardar = new JButton("Guardar Registro");
        btnGuardar.setBackground(new Color(0, 120, 215)); // Tono azul Windows
        btnGuardar.setForeground(Color.WHITE);
        
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnGuardar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.insets = new Insets(15, 5, 5, 5);
        add(panelBotones, gbc);
    }


    public String getNombre() { return txtNombre.getText().trim(); }
    public String getEdad() { return txtEdad.getText().trim(); }
    public String getFecha() { return txtFecha.getText().trim(); }
    public boolean isActivo() { return chkActivo.isSelected(); }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnLimpiar() { return btnLimpiar; }

    public void cargarDatos(String nombre, String edad, String fecha, boolean activo) {
        txtNombre.setText(nombre);
        txtEdad.setText(edad);
        txtFecha.setText(fecha);
        chkActivo.setSelected(activo);
    }

    public void limpiar() {
        txtNombre.setText("");
        txtEdad.setText("");
        txtFecha.setText("");
        chkActivo.setSelected(false);
    }
}
