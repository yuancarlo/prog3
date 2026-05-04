package LLuviaDeCirculos.vista;

import LLuviaDeCirculos.modelo.Espacio;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Principal extends JFrame {
    private static final Logger logger = LogManager.getRootLogger();

    public Principal() {

        setTitle("Juego de Círculos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        Espacio espacio = new Espacio(800, 600);

        PanelJuego panel = new PanelJuego(espacio);
        add(panel);

        Thread hiloDelJuego = new Thread(espacio);
        hiloDelJuego.start();
        logger.info("Hilo principal del modelo arrancado exitosamente");
    }

    public static void main(String[] args) {

        logger.info("--- Iniciando aplicación Juego de Círculos ---");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Principal ventana = new Principal();
                ventana.setVisible(true);
                logger.info("Ventana gráfica desplegada y visible");
            }
        });
    }
}