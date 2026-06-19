package ArbolJSON.Vista;

import Utilidades.Arbol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PanelDerecho extends JPanel {
    private static final Logger logger = LogManager.getRootLogger();

    private Arbol<String> arbol;
    private DibujoArbol dibujador;

    public PanelDerecho() {
        setBorder(BorderFactory.createTitledBorder("Gráfico"));
        setBackground(Color.WHITE);
    }

    public void setArbol(Arbol<String> arbol) {
        this.arbol = arbol;
        repaint();
    }

    public Map<Rectangle, String> getAreasClickeables() {
        if (dibujador != null) {
            return dibujador.getAreasClickeables();
        }
        return new HashMap<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (arbol != null && arbol.getRaiz() != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            dibujador = new DibujoArbol(arbol);
            dibujador.dibujar(g2d, 20, 40);
        } else {
            dibujador = null;
        }
    }
}