package ArbolJSON.Vista;


import ArbolJSON.Controlador.ArbolControlador;
import ArbolJSON.Modelo.ArbolModelo;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private PanelIzquierdo panelIzquierdo;
    private PanelDerecho panelDerecho;

    public VentanaPrincipal() {
        setTitle("ArbolJSON");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        panelIzquierdo = new PanelIzquierdo();
        panelDerecho = new PanelDerecho();

        add(panelIzquierdo);
        add(panelDerecho);
        setVisible(true);
    }

    public PanelIzquierdo getPanelIzquierdo() { return panelIzquierdo; }
    public PanelDerecho getPanelDerecho() { return panelDerecho; }
    public static void main(String[] args) {
            ArbolModelo modelo = new ArbolModelo();
            VentanaPrincipal vista = new VentanaPrincipal();

            new ArbolControlador(modelo, vista);

            modelo.actualizarDesdeJson("{\n" +
                    "  \"contenido\": \"D\",\n" +
                    "  \"hijos\": [ ]\n" +
                    "}");
    }
}