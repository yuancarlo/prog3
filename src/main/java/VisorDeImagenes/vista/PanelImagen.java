package VisorDeImagenes.vista;

import VisorDeImagenes.modelo.ModeloImagen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PanelImagen extends JPanel {
    private static final Logger logger = LogManager.getLogger(PanelImagen.class);
    private ModeloImagen modelo;

    public PanelImagen() {
        setBackground(new Color(45, 45, 45)); // Fondo gris oscuro profesional
    }

    public void setModelo(ModeloImagen modelo) {
        this.modelo = modelo;
        if (modelo != null) {
            logger.debug("Repintando panel con imagen de {}x{}", modelo.getAncho(), modelo.getAlto());
            setPreferredSize(new Dimension(modelo.getAncho(), modelo.getAlto()));
        }
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (modelo == null) return;

        BufferedImage bi = new BufferedImage(modelo.getAncho(), modelo.getAlto(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < modelo.getAlto(); y++) {
            for (int x = 0; x < modelo.getAncho(); x++) {
                bi.setRGB(x, y, modelo.getPixeles()[y][x]);
            }
        }

        int x = Math.max(0, (getWidth() - modelo.getAncho()) / 2);
        int y = Math.max(0, (getHeight() - modelo.getAlto()) / 2);

        g.drawImage(bi, x, y, null);
    }
}