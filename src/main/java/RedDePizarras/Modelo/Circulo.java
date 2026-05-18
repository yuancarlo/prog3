package RedDePizarras.Modelo;

import java.awt.*;
import java.beans.PropertyChangeSupport;

public class Circulo extends Figura{
    private int radio;

    public Circulo(int x, int y,int radio, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.observado = new PropertyChangeSupport(this);
        this.radio = radio;
        this.seleccionada = false;

    }

    public int getRadio() {
        return radio;
    }

    public int diametro() {
        return radio*2;
    }


    @Override
    public void dibujar(Graphics g) {
        g.setColor(this.color);
        g.drawOval(this.x-this.radio, this.y-this.radio, diametro(), diametro());
    }

    @Override
    public boolean coordenadaDentroDeFigura(int x, int y) {
        int distanciaX = x - this.x;
        int distanciaY = y - this.y;
        return (distanciaX * distanciaX) + (distanciaY * distanciaY) <= (radio * radio);
    }
}
