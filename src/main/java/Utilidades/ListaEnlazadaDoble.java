package Utilidades;

import java.util.Iterator;

public class ListaEnlazadaDoble<G> implements Iterable<G>{
    protected Nodo<G> primero;
    protected Nodo<G> ultimo;
    protected int tamano;

    public ListaEnlazadaDoble() {
        this.primero = null;
        this.ultimo = null;
        this.tamano = 0;
    }

    public void preponer(G o){
        /**Nodo<G> nuevo = new Nodo<>(o);
        nuevo.setSiguiente(primero);
        primero = nuevo;
        tamano++;
         */
        Nodo<G> nuevo = new Nodo<>(o);
        if (primero == null) {
            /** Sus punteros siguiente y anterior ya son null por defecto*/
            primero = nuevo;
            ultimo = nuevo;
        } else {
            /** El nuevo apunta hacia adelante al viejo primero*/
            nuevo.setSiguiente(primero);
            /** El viejo primero apunta hacia atrás al nuevo nodo*/
            primero.setAnterior(nuevo);
            primero = nuevo;
        }
        tamano++;
    }

    public G getValorPorIndice(int indice) {
    
        if (indice < 0) {
            return null; 
        }

        Nodo<G> actual = primero;
        int contador = 0;

        while (actual != null) {
            if (contador == indice) {
                return actual.getContenido(); 
            }
        
            contador++;
            actual = actual.siguiente;
        }
        return null; 
    }


    public void adicionar(G o){
        Nodo<G> nuevo = new Nodo<>(o);
        if (ultimo == null) {
            ultimo = nuevo;
            primero = nuevo;
        }else {
            /**El nuevo apunta hacia atras al viejo ultino*/
            nuevo.setAnterior(ultimo);
            /**El viejo ultimo apunta hacia adelante al nuevo nodo*/
            ultimo.setSiguiente(nuevo);
            ultimo = nuevo;
        }
        tamano++;
    }

    public void eliminar(int indice) {
        if (indice < 0 || indice >= tamano) {
            return;
        }

        if (indice == 0) {
            primero = primero.getSiguiente();
            if (primero != null) {
                primero.setAnterior(null);
            } else {
                ultimo = null;
            }
        } else if (indice == tamano - 1) {
            ultimo = ultimo.getAnterior();
            if (ultimo != null) {
                ultimo.setSiguiente(null);
            } else {
                primero = null;
            }
        } else {

            Nodo<G> actual = primero;
            int contador = 0;

            while (contador < indice) {
                actual = actual.getSiguiente();
                contador++;
            }
            Nodo<G> nodoAnterior = actual.getAnterior();
            Nodo<G> nodoSiguiente = actual.getSiguiente();

            nodoAnterior.setSiguiente(nodoSiguiente);
            nodoSiguiente.setAnterior(nodoAnterior);
        }
        tamano--;
    }

    public G remover(int indice) {
        if (indice < 0 || indice >= tamano) {
            throw new IndexOutOfBoundsException("Indice invalido");
        }

        G eliminado;

        if (indice == 0) {
            eliminado = primero.getContenido();
            primero = primero.getSiguiente();
            if (primero != null) {
                primero.setAnterior(null);
            } else {
                ultimo = null;
            }
        } else if (indice == tamano - 1) {
            eliminado = ultimo.getContenido();
            ultimo = ultimo.getAnterior();
            if (ultimo != null) {
                ultimo.setSiguiente(null);
            } else {
                primero = null;
            }
        } else {
            Nodo<G> actual = primero;
            int contador = 0;

            while (contador < indice) {
                actual = actual.getSiguiente();
                contador++;
            }

            eliminado = actual.getContenido();
            Nodo<G> nodoAnterior = actual.getAnterior();
            Nodo<G> nodoSiguiente = actual.getSiguiente();

            nodoAnterior.setSiguiente(nodoSiguiente);
            nodoSiguiente.setAnterior(nodoAnterior);
        }
        tamano--;
        return eliminado;
    }


    public boolean eliminarPorValor(G valor) {
        Nodo<G> actual = primero;
        int indice = 0;
        while(actual != null) {
            if (actual.getContenido().equals(valor)) {
                eliminar(indice);
                return true;
            }
            actual = actual.getSiguiente();
            indice++;
        }
        return false;
    }

    public String toStringDetallado(){
        if(this.primero == null){
            return "{VACIA}";
        }

        StringBuilder resultado = new StringBuilder();
        resultado.append("null <- ");
        Nodo<G> actual = primero;
        while(actual != null) {
            resultado.append(actual.toString());
            actual = actual.getSiguiente();
        }

        resultado.setLength(resultado.length() - 5);

        resultado.append(" -> null");
        return resultado.toString();
    }

    @Override
    public String toString() {
        if(this.primero == null){
            return "[]";
        }
        StringBuilder resultado = new StringBuilder();
        resultado.append("[");
        Nodo<G> actual = primero;
        while(actual != null) {
            resultado.append(actual.getContenido());
            if(actual.getSiguiente() != null){
                resultado.append(", ");
            }
            actual = actual.getSiguiente();
        }
        resultado.append("]");
        return resultado.toString();
    }

    public int getTamano() {
        return tamano;
    }

    public Nodo<G> getPrimero() {
        return primero;
    }

    public Nodo<G> getUltimo() {
        return ultimo;
    }

    public void setPrimero(Nodo<G> primero) {
        this.primero = primero;
    }

    public void setUltimo(Nodo<G> ultimo) {
        this.ultimo = ultimo;
    }
    
    @Override
    public Iterator<G> iterator() {
        return new IteradorLista(this);
    }

    private static class Nodo<G> {
        private G contenido;
        private Nodo<G> siguiente;
        private  Nodo<G> anterior;

        public Nodo(G o) {
            this.contenido = o;
            this.siguiente = null;
            this.anterior = null;
        }

        public G getContenido() {
            return contenido;
        }

        public void setContenido(G contenido) {
            this.contenido = contenido;
        }

        public Nodo<G> getSiguiente() {
            return siguiente;
        }

        public void setSiguiente(Nodo<G> siguiente) {
            this.siguiente = siguiente;
        }

        public Nodo<G> getAnterior() {
            return anterior;
        }

        public void setAnterior(Nodo<G> anterior) {this.anterior = anterior;}

        @Override
        public String toString() {
            return "[" + contenido.toString() + "] <-> ";
        }
    }

    private static class IteradorLista<G> implements Iterator<G> {

        private Nodo<G> actual;

        public IteradorLista(ListaEnlazadaDoble<G> lista) {
            actual = lista.getPrimero();
        }

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public G next() {
            G o = actual.getContenido();
            actual = actual.getSiguiente();
            return o;
        }
    }

    
}
