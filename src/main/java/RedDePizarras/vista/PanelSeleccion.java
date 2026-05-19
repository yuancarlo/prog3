package RedDePizarras.vista;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class PanelSeleccion extends JPanel {
    private JRadioButton cuadrado;
    private JRadioButton circulo;
    private JLabel tamaño;
    private JTextField campoTamaño;
    //private JButton botonLimpiar;

    public PanelSeleccion(){
        cuadrado = new JRadioButton("Cuadrado", true);
        cuadrado.setBackground(Color.LIGHT_GRAY);
        cuadrado.setFocusPainted(false);
        circulo = new JRadioButton("Circulo");
        circulo.setBackground(Color.LIGHT_GRAY);
        circulo.setFocusPainted(false);
        tamaño = new JLabel("Lado/Diámetro");
        campoTamaño = new JTextField("20", 4);
        //botonLimpiar = new JButton("LIMPIAR");
        //botonLimpiar.setFocusPainted(false);

        ButtonGroup grupoBotones = new ButtonGroup();
        grupoBotones.add(cuadrado);
        grupoBotones.add(circulo);

        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(cuadrado);
        add(circulo);
        add(tamaño);
        add(campoTamaño);
        //add(botonLimpiar);
        setBackground(Color.LIGHT_GRAY);
    }

    public String getSeleccion() {
        if (cuadrado.isSelected()) return "CUADRADO";
        return "CIRCULO";
    }


    public int getTamañoEspecificado() {
        try {
            return Integer.parseInt(campoTamaño.getText().trim());
        } catch (NumberFormatException e) {
            return 20;
        }
    }
}
