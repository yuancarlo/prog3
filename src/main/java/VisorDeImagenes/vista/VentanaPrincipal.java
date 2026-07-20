package VisorDeImagenes.vista;

import VisorDeImagenes.modelo.ModeloImagen;
import VisorDeImagenes.utileria.LectorImagen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VentanaPrincipal extends JFrame {
    private static final Logger logger = LogManager.getRootLogger();
    private ModeloImagen modelo;
    private PanelImagen panelImagen;

    public VentanaPrincipal() {
        modelo = new ModeloImagen();
        panelImagen = new PanelImagen();
        modelo.agregarObservador(panelImagen);

        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("Visor y Cifrador de Imágenes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        JMenuBar barraMenu = new JMenuBar();
        JMenu menuArchivo = new JMenu("Cargar");

        JMenuItem itemUrl = new JMenuItem("Desde URL");
        JMenuItem itemLocal = new JMenuItem("Desde Archivo Local");

        itemUrl.addActionListener(e -> cargarDesdeUrl());
        itemLocal.addActionListener(e -> cargarDesdeArchivo());

        menuArchivo.add(itemUrl);
        menuArchivo.add(itemLocal);
        barraMenu.add(menuArchivo);

        JButton btnRedimensionar = new JButton("Redimensionar");
        JButton btnCifrar = new JButton("Cifrar");
        JButton btnDescifrar = new JButton("Descifrar");
        JButton btnRestaurar = new JButton("Deshacer Cambios");

        btnRedimensionar.addActionListener(e -> mostrarDialogoRedimension());
        btnCifrar.addActionListener(e -> accionarCifrado(true));
        btnDescifrar.addActionListener(e -> accionarCifrado(false));
        btnRestaurar.addActionListener(e -> modelo.restaurarImagenOriginal());

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(barraMenu);
        panelSuperior.add(btnRedimensionar);
        panelSuperior.add(btnCifrar);
        panelSuperior.add(btnDescifrar);
        panelSuperior.add(btnRestaurar);

        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(panelImagen), BorderLayout.CENTER);
    }

    private void cargarDesdeUrl() {
        String url = JOptionPane.showInputDialog(this, "Introduce la URL de la imagen:");
        if (url != null && !url.trim().isEmpty()) {
            cargarProcesarImagen(url);
        }
    }

    private void cargarDesdeArchivo() {
        JFileChooser selectorArchivo = new JFileChooser();
        int resultado = selectorArchivo.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            cargarProcesarImagen(selectorArchivo.getSelectedFile().getAbsolutePath());
        }
    }

    private void cargarProcesarImagen(String ruta) {
        BufferedImage imagenCargada = LectorImagen.leerImagen(ruta);
        if (imagenCargada != null) {
            modelo.cargarImagenBase(imagenCargada);
        } else {
            JOptionPane.showMessageDialog(this, "Error al cargar la imagen", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoRedimension() {
        if (modelo.obtenerImagenActual() == null) return;

        JTextField txtAncho = new JTextField(String.valueOf(modelo.getAnchoActual()), 5);
        JTextField txtAlto = new JTextField(String.valueOf(modelo.getAltoActual()), 5);
        JCheckBox chkProporcion = new JCheckBox("Mantener Proporción");

        JPanel panelFondo = new JPanel(new GridLayout(3, 2, 5, 5));
        panelFondo.add(new JLabel("Nuevo Ancho:"));
        panelFondo.add(txtAncho);
        panelFondo.add(new JLabel("Nuevo Alto:"));
        panelFondo.add(txtAlto);
        panelFondo.add(new JLabel(""));
        panelFondo.add(chkProporcion);

        int confirmacion = JOptionPane.showConfirmDialog(this, panelFondo, "Redimensionar Imagen", JOptionPane.OK_CANCEL_OPTION);

        if (confirmacion == JOptionPane.OK_OPTION) {
            try {
                int ancho = Integer.parseInt(txtAncho.getText());
                int alto = Integer.parseInt(txtAlto.getText());
                modelo.redimensionar(ancho, alto, chkProporcion.isSelected());
            } catch (NumberFormatException ex) {
                logger.error("Valores de dimensión inválidos", ex);
                JOptionPane.showMessageDialog(this, "Por favor introduce números válidos.", "Entrada Inválida", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void accionarCifrado(boolean esCifrar) {
        if (modelo.obtenerImagenActual() == null) return;

        String clave = JOptionPane.showInputDialog(this, "Introduce la clave de cifrado:");
        if (clave != null && !clave.trim().isEmpty()) {
            if (esCifrar) {
                modelo.cifrarImagen(clave);
            } else {
                modelo.descifrarImagen(clave);
            }
        }
    }

    public static void main(String[] args) {
        logger.info("Iniciando aplicación...");
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                logger.warn("No se pudo cargar la interfaz nativa del SO", e);
            }
            new VentanaPrincipal().setVisible(true);
        });
    }
}