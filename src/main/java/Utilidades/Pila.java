package Utilidades;

public class Pila<G> extends ListaEnlazadaDoble<G>{
    public Pila() {
        super();
    }

    public void apilar(G o) {
        this.preponer(o);
    }

    public G desapilar() {
        G obj = getValorPorIndice(0);
        eliminar(0);
        return obj;
    }
}
