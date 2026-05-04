package LLuviaDeCirculos.modelo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Circulo implements Runnable{
    private static final Logger logger = LogManager.getRootLogger();
    private int radio;
    private int x;
    private int y;
    private boolean estado;
    private int limiteY;
    private PropertyChangeSupport observado;

    public Circulo(int radio, int x, int y, int limiteY){
        this.radio = radio;
        this.x = x;
        this.y = y;
        this.estado = false;
        this.limiteY = limiteY;
        this.observado = new PropertyChangeSupport(this);
        logger.debug("Círculo instanciado: radio={}, x={}, y inicial={}, limiteY={}", radio, x, y, limiteY);
    }

    public boolean estaClickeado(int clickX, int clickY) {
        int distanciaX = clickX - this.x;
        int distanciaY = clickY - this.y;
        return (distanciaX * distanciaX) + (distanciaY * distanciaY) <= (radio * radio);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        observado.addPropertyChangeListener(listener);
    }

    public int getRadio() {
        return radio;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isEliminado() {
        logger.info("Círculo eliminado por el usuario en x={}, y={}", x, y);
        return estado;
    }

    public void setEstado() {
        this.estado = true;
        observado.firePropertyChange("eliminado", false, true);
    }

    @Override
    public String toString() {
        return String.valueOf(radio);
    }

    @Override
    public void run() {
        logger.debug("Hilo de círculo iniciado");
        while (!estado && y < limiteY) {
            int yVieja = this.y;

            this.y += 5;

            observado.firePropertyChange("posicionY", yVieja, this.y);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                logger.warn("Hilo del círculo interrumpido", e);
                Thread.currentThread().interrupt();
                break;
            }
        }

        if (!estado) {
            this.estado = true;
            logger.debug("Círculo alcanzó el límite inferior y fue descartado");
            observado.firePropertyChange("eliminado", false, true);
        }
    }
}
