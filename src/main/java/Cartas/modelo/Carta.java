package Cartas.modelo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Carta {
    private static Logger logger = LogManager.getLogger(Carta.class);;
    private final int valor;
    private Palo palo;
    private final int posicionOriginal;
    private int posicionActual;

    public Carta(int valor, Palo palo, int posicionOriginal) {
        if (valor < 1 || valor > 13) {
            throw new IllegalArgumentException("Los valores de las cartas solo pùeden estar en el rango 1-13");
        }
        this.valor = valor;
        this.palo = palo;
        this.posicionOriginal = posicionOriginal;
        this.posicionActual = posicionOriginal;
        logger.trace("Instanciada: {} de {} (ID: {})", valor, palo, posicionOriginal);
    }

    public Palo getPalo() {
        return palo;
    }

    public int getValor() {
        return valor;
    }

    public int getPosicionOriginal() {
        return posicionOriginal;
    }

    public int getPosicionActual() {
        return posicionActual;
    }

    public void setPosicionActual(int posicionActual) {
        this.posicionActual = posicionActual;
    }

    @Override
    public String toString() {
        String nombreValor;
        switch (this.valor) {
            case 1:nombreValor = "A"; break;
            case 11:nombreValor = "J"; break;
            case 12:nombreValor = "Q"; break;
            case 13:nombreValor = "K"; break;
            default: nombreValor = String.valueOf(valor);
        }
        return "[ " + nombreValor + " de " + palo + "|Posicion Original: " + posicionOriginal +"|Posicion Actual: " + posicionActual + " ]";
    }
}
