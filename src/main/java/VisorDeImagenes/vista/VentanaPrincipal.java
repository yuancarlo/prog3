package VisorDeImagenes.vista;

import VisorDeImagenes.modelo.LectorImagen;
import VisorDeImagenes.modelo.ModeloImagen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VentanaPrincipal extends JFrame {
    private static final Logger logger = LogManager.getLogger(VentanaPrincipal.class);
    private PanelImagen panelImagen;

    private ModeloImagen modeloOriginal;
    private ModeloImagen modeloActual;

    public VentanaPrincipal() {
        logger.info("Iniciando Visor de Imágenes...");
        setTitle("Visor De Imagen");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 800);

        panelImagen = new PanelImagen();
        add(new JScrollPane(panelImagen), BorderLayout.CENTER);

        configurarMenus();
        setLocationRelativeTo(null);
    }

    private void configurarMenus() {
        JMenuBar mb = new JMenuBar();
        JMenu mArchivo = new JMenu("Archivo");

        JMenuItem itemFile = new JMenuItem("Cargar Archivo...");
        itemFile.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                cargarNuevaImagen(fc.getSelectedFile().getAbsolutePath());
            }
        });

        JMenuItem itemUrl = new JMenuItem("Cargar URL...");
        itemUrl.addActionListener(e -> {
            String url = JOptionPane.showInputDialog(this, "Introduce URL:");
            if (url != null && !url.trim().isEmpty()) cargarNuevaImagen(url);
        });

        mArchivo.add(itemFile); mArchivo.add(itemUrl);

        JMenu mImagen = new JMenu("Imagen");
        JMenuItem itemRedim = new JMenuItem("Redimensionar...");
        itemRedim.addActionListener(e -> ejecutarRedimension());

        mImagen.add(itemRedim);
        mb.add(mArchivo); mb.add(mImagen);
        setJMenuBar(mb);
    }

    private void cargarNuevaImagen(String ruta) {
        logger.info("Intentando cargar: {}", ruta);
        BufferedImage bi = LectorImagen.leerImagen(ruta);
        if (bi != null) {
            modeloOriginal = new ModeloImagen(bi);
            modeloActual = modeloOriginal;
            panelImagen.setModelo(modeloActual);
            logger.info("Imagen cargada y establecida como Original.");
        } else {
            logger.error("No se pudo cargar la imagen desde: {}", ruta);
        }
    }

    private void ejecutarRedimension() {
        if (modeloOriginal == null) {
            logger.warn("Intento de redimensión sin imagen cargada.");
            return;
        }

        DialogoRedimensionar diag = new DialogoRedimensionar(this, modeloActual);
        diag.setVisible(true);

        if (diag.isAceptado()) {
            int nw = diag.getNuevoAncho();
            int nh = diag.getNuevoAlto();

            logger.info("Redimensionando de {}x{} a {}x{}", modeloActual.getAncho(), modeloActual.getAlto(), nw, nh);

            modeloActual = modeloOriginal.redimensionar(nw, nh);

            panelImagen.setModelo(modeloActual);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}