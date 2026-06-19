package Utilidades;

public class Cola<G> extends ListaEnlazadaDoble<G>{
    public Cola() {
        super();
    }

    public void encolar(G elemento) {
        this.adicionar(elemento);
    }

    public G desencolar() {
        G obj = getValorPorIndice(0);
        eliminar(0);
        return obj;
    }
}
