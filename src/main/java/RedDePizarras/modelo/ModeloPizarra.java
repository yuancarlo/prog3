package RedDePizarras.modelo;

import Utilidades.ListaEnlazadaDoble;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ModeloPizarra implements PropertyChangeListener{
    private static final Logger logger = LogManager.getRootLogger();
    private ListaEnlazadaDoble<Figura> figurasPropias;
    private ListaEnlazadaDoble<Figura> figurasRemotas;
    private PropertyChangeSupport observado;
    

    public ModeloPizarra() {
        this.figurasPropias = new ListaEnlazadaDoble<>();
        this.figurasRemotas = new ListaEnlazadaDoble<>();
        this.observado = new PropertyChangeSupport(this);
        //logger.info("ModeloPizarra inicializado. Listas enlazadas listas para recibir datos.");
    }

    public void addObserver(PropertyChangeListener listener) {observado.addPropertyChangeListener(listener);}

    public void agregarFiguraPropia(Figura figura) {
        figurasPropias.adicionar(figura);
        figura.addObserver(this);
        
        observado.firePropertyChange("NUEVA_FIGURA", null, figura);
    }

    public void agregarFiguraRemota(Figura figura) {
        figurasRemotas.adicionar(figura);
        observado.firePropertyChange("NUEVA_FIGURA", null, figura);
    }

    public ListaEnlazadaDoble<Figura> getListaPropia() { return figurasPropias; }
    public ListaEnlazadaDoble<Figura> getListaRemota() { return figurasRemotas; }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("FIGURA".equals(evt.getPropertyName())) {
            observado.firePropertyChange("FIGURA_MODIFICADA", null, evt.getSource());
        };
    }

     
}
