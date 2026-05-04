package LLuviaDeCirculos.vista;

import LLuviaDeCirculos.modelo.Circulo;
import LLuviaDeCirculos.modelo.Espacio;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class PanelJuego extends JPanel implements PropertyChangeListener {
    private static final Logger logger = LogManager.getRootLogger();

    private Espacio espacio;

    public PanelJuego(Espacio espacio) {
        this.espacio = espacio;
        this.setBackground(Color.DARK_GRAY);
        this.espacio.addPropertyChangeListener(this);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                logger.debug("Clic registrado en la vista en X: {}, Y: {}", e.getX(), e.getY());
                verificarClic(e.getX(), e.getY());
            }
        });
        logger.info("PanelJuego inicializado y escuchando eventos");
    }


    private void verificarClic(int clickX, int clickY) {
        Circulo[] circulos = espacio.getCirculos();

        for (Circulo c : circulos) {
            if (c != null && !c.isEliminado() && c.estaClickeado(clickX, clickY)) {
                c.setEstado();
                espacio.sumarPunto();
                break;
            }
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String evento = evt.getPropertyName();

        if ("nuevoCirculo".equals(evento)) {
            Circulo nuevoCirculo = (Circulo) evt.getNewValue();
            nuevoCirculo.addPropertyChangeListener(this);
            logger.debug("PanelJuego suscrito a un nuevo círculo");
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Puntos: " + espacio.getPuntos(), 20, 40);

        g2d.setColor(Color.CYAN);
        Circulo[] circulos = espacio.getCirculos();

        for (Circulo c : circulos) {
            if (c != null && !c.isEliminado()) {
                int radio = c.getRadio();
                g2d.fillOval(c.getX() - radio, c.getY() - radio, radio * 2, radio * 2);
            }
        }
    }
}