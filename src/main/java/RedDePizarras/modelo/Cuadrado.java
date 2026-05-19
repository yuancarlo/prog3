package RedDePizarras.modelo;

import java.awt.*;
import java.beans.PropertyChangeSupport;

public class Cuadrado extends Figura{
    private int lado;

    public Cuadrado(int x, int y, int lado, Color c) {
        this.x = x;
        this.y = y;
        this.color = c;
        this.lado = lado;
        this.observado = new PropertyChangeSupport(this);
        seleccionada = false;
        //logger.info("Instanciado nuevo Cuadrado - Esquina: (" + x + "," + y + "), Lado: " + lado);
    }

    public int getLado() {
        return lado;
    }

    @Override
    public void dibujar(Graphics g) {
        g.setColor(this.color);
        g.drawRect(x, y, lado, lado);
    }

    @Override
    public boolean coordenadaDentroDeFigura(int px, int py) {
        return (px > this.x && px < (this.x + lado) && py > this.y && py < (this.y + lado));
    }


}
