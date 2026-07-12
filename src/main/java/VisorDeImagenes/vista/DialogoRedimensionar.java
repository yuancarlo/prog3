package VisorDeImagenes.vista;

import VisorDeImagenes.modelo.ModeloImagen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DialogoRedimensionar extends JDialog {
    private static final Logger logger = LogManager.getRootLogger();
    private JTextField txtAncho, txtAlto;
    private JCheckBox opcionProporcional;
    private boolean aceptado = false;
    private ModeloImagen imagenModelo;

    public DialogoRedimensionar(Frame padre, ModeloImagen modelo) {
        super(padre, "Redimensionar", true);
        this.imagenModelo = modelo;

        setLayout(new GridLayout(4, 2, 10, 10));
        txtAncho = new JTextField(String.valueOf(modelo.getAncho()));
        txtAlto = new JTextField(String.valueOf(modelo.getAlto()));
        opcionProporcional = new JCheckBox("Mantener proporción", true);

        JButton btnOk = new JButton("Aplicar");
        JButton btnCancel = new JButton("Cancelar");

        txtAncho.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (opcionProporcional.isSelected() && !txtAncho.getText().isEmpty()) {
                    try {
                        int w = Integer.parseInt(txtAncho.getText());
                        txtAlto.setText(String.valueOf(imagenModelo.obtenerAltoProporcional(w)));
                    } catch (NumberFormatException ex) {
                        logger.warn("Input de ancho inválido: {}", txtAncho.getText());
                    }
                }
            }
        });

        btnOk.addActionListener(e -> { aceptado = true; setVisible(false); });
        btnCancel.addActionListener(e -> setVisible(false));

        add(new JLabel(" Ancho:")); add(txtAncho);
        add(new JLabel(" Alto:")); add(txtAlto);
        add(opcionProporcional); add(new JLabel(""));
        add(btnOk); add(btnCancel);

        pack();
        setLocationRelativeTo(padre);
    }

    public boolean isAceptado() { return aceptado; }
    public int getNuevoAncho() { return Integer.parseInt(txtAncho.getText()); }
    public int getNuevoAlto() { return Integer.parseInt(txtAlto.getText()); }
}