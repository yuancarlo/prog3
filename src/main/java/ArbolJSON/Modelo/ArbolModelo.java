package ArbolJSON.Modelo;

import Utilidades.Arbol;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ArbolModelo {
    private static final Logger logger = LogManager.getRootLogger();
    private Arbol<String> arbol;
    private PropertyChangeSupport observado;
    private Gson gson;

    public ArbolModelo() {
        this.arbol = new Arbol<>();
        this.observado = new PropertyChangeSupport(this);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void agregarObservador(PropertyChangeListener observador) {
        observado.addPropertyChangeListener(observador);
    }

    public Arbol<String> getArbol() {
        return arbol;
    }

    public void actualizarDesdeJson(String json) {
        try {
            logger.info("Intentando parsear JSON a DTO");
            NodoDTO dtoRaiz = gson.fromJson(json, NodoDTO.class);
            Arbol<String> nuevoArbol = new Arbol<>();
            if (dtoRaiz != null) {
                construirArbolDesdeDTO(null, dtoRaiz, nuevoArbol);
            }
            this.arbol = nuevoArbol;
            logger.info("Árbol actualizado correctamente desde JSON");
            observado.firePropertyChange("arbol", null, this.arbol);
            observado.firePropertyChange("json", null, obtenerJsonActual());
        } catch (Exception e) {
            logger.error("Error al parsear el JSON: ", e);
        }
    }

    private void construirArbolDesdeDTO(String padre, NodoDTO dto, Arbol<String> destino) {
        destino.insertar(padre, dto.getContenido());
        if (dto.getHijos() != null) {
            for (NodoDTO hijo : dto.getHijos()) {
                construirArbolDesdeDTO(dto.getContenido(), hijo, destino);
            }
        }
    }

    public String obtenerJsonActual() {
        if (arbol.getRaiz() == null) return "{}";
        NodoDTO dtoRaiz = construirDTODesdeArbol(arbol.getRaiz());
        return gson.toJson(dtoRaiz);
    }

    private NodoDTO construirDTODesdeArbol(Arbol<String>.Nodo<String> nodoArbol) {
        NodoDTO dto = new NodoDTO(nodoArbol.getContenido());
        for (Arbol<String>.Nodo<String> hijo : nodoArbol.getHijos()) {
            dto.getHijos().add(construirDTODesdeArbol(hijo));
        }
        return dto;
    }

    public void agregarNodoGraph(String padreId, String nuevoId) {
        logger.info("Agregando nuevo nodo hijo '{}' a '{}'", nuevoId, padreId);
        arbol.insertar(padreId, nuevoId);
        notificarCambios();
    }

    public void eliminarNodoGraph(String id) {
        logger.info("Intentando eliminar nodo: {}", id);
        if (arbol.eliminarNodo(id)) {
            logger.info("Nodo {} eliminado con éxito", id);
            notificarCambios();
        } else {
            logger.warn("No se pudo eliminar el nodo {}", id);
        }
    }

    private void notificarCambios() {
        observado.firePropertyChange("arbol", null, this.arbol);
        observado.firePropertyChange("json", null, obtenerJsonActual());
    }
}