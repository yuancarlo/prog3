package LLuviaDeCirculos.modelo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

public class Espacio implements Runnable {

    private static final Logger logger = LogManager.getRootLogger();
    private Circulo[] circulos;
    private final int MAX_CIRCULOS = 20;
    private int puntos;
    private int limiteX;
    private int limiteY;

    private PropertyChangeSupport observado;

    public Espacio(int limiteX, int limiteY) {
        this.circulos = new Circulo[MAX_CIRCULOS];
        this.puntos = 0;
        this.limiteX = limiteX;
        this.limiteY = limiteY;
        this.observado = new PropertyChangeSupport(this);
        logger.info("Espacio inicializado con límites X: {}, Y: {}", limiteX, limiteY);
    }


    public void addPropertyChangeListener(PropertyChangeListener listener) {
        observado.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        observado.removePropertyChangeListener(listener);
    }


    public void sumarPunto() {
        int puntosViejos = this.puntos;
        this.puntos++;
        logger.info("¡Punto sumado! Puntos totales: {}", this.puntos);
        observado.firePropertyChange("puntos", puntosViejos, this.puntos);
    }


    @Override
    public void run() {
        logger.info("Hilo creador de Espacio iniciado. Comenzando a generar {} círculos.", MAX_CIRCULOS);
        Random random = new Random();

        for (int i = 0; i < MAX_CIRCULOS; i++) {

            int radio = random.nextInt(20) + 20;
            int x = random.nextInt(limiteX - (radio * 2));
            int y = -radio * 2;
            Circulo nuevoCirculo = new Circulo(radio, x, y, limiteY);
            circulos[i] = nuevoCirculo;
            logger.info("Hilo creador de Espacio iniciado. Comenzando a generar {} círculos.", MAX_CIRCULOS);

            observado.firePropertyChange("nuevoCirculo", null, nuevoCirculo);
            Thread hiloCirculo = new Thread(nuevoCirculo);
            hiloCirculo.start();
            try {
                Thread.sleep(random.nextInt(1000) + 500);
            } catch (InterruptedException e) {
                logger.warn("Hilo creador de Espacio interrumpido", e);
                Thread.currentThread().interrupt();
                break;
            }
        }
        logger.info("Creación de círculos finalizada.");
    }


    public Circulo[] getCirculos() {
        return circulos;
    }

    public int getPuntos() {
        return puntos;
    }
}