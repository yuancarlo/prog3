package RedDePizarras.modelo;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class Figura {
    protected static final Logger logger = LogManager.getRootLogger();
    protected  int x;
    protected  int y;
    protected Color color;
    protected  boolean seleccionada;
    protected PropertyChangeSupport observado;

    public void addObserver(PropertyChangeListener listener) {
        observado.addPropertyChangeListener(listener);
    }

    public void setPosicion(int nx, int ny) {
        x = nx;
        y = ny;
        observado.firePropertyChange("FIGURA", true, false);
    }

    public void setSeleccionado() {
        observado.firePropertyChange("FIGURA", true, false);
    }

    public void deseleccionar() {
        boolean anteriorEstado = seleccionada;
        seleccionada = false;
        if (anteriorEstado)
            observado.firePropertyChange("FIGURA", true, false);
    }

    public abstract void dibujar(Graphics g);

    public abstract boolean coordenadaDentroDeFigura(int x, int y);

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
