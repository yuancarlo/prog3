package VisorDeImagenes.vista;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PanelImagen extends JPanel implements PropertyChangeListener {
    private static final Logger logger = LogManager.getRootLogger();
    private BufferedImage imagenVisible;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenVisible != null) {
            int x = (getWidth() - imagenVisible.getWidth()) / 2;
            int y = (getHeight() - imagenVisible.getHeight()) / 2;
            g.drawImage(imagenVisible, x, y, this);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("imagen".equals(evt.getPropertyName())) {
            logger.info("Panel de imagen notificado del cambio en el modelo, repintando...");
            this.imagenVisible = (BufferedImage) evt.getNewValue();
            revalidate();
            repaint();
        }
    }
}
