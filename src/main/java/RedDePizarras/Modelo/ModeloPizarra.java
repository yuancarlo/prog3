package RedDePizarras.Modelo;

import Utilidades.ListaEnlazadaDoble;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ModeloPizarra {
    private ListaEnlazadaDoble<Figura> figurasPropias;
    private PropertyChangeSupport observado;

    public ModeloPizarra() {
        this.figurasPropias = new ListaEnlazadaDoble<>();
        this.observado = new PropertyChangeSupport(this);

    }

    public void addObserver(PropertyChangeListener listener) {}

    public void agregarFiguraPropia(Figura figura) {
        figurasPropias.adicionar(figura);
        observado.firePropertyChange("NUEVA_FIGURA", null, figura);
    }

   /* public void agregarFiguraRemota(Figura figura) {
        figurasRemotas.adicionar(figura);
        observado.firePropertyChange("NUEVA_FIGURA", null, figura);
    }*/

    public ListaEnlazadaDoble getListaPropia() { return figurasPropias; }
   // public ListaEnlazadaDoble getListaRemota() { return figurasRemotas; }
}
