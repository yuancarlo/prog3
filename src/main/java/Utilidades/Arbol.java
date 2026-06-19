package Utilidades;

import java.util.Map;
import java.util.LinkedHashMap;

public class Arbol<E> {
    protected Nodo<E> raiz;
    protected Map<String, Nodo<E>>  nodos;

    public Arbol() {
        raiz = null;
        nodos = new LinkedHashMap<>();
    }

    /**
     * Recorrer en anchura
     */
    public void recorrerBfs() {
        for(Nodo<E> nodoHijo : nodos.values()) {
            nodoHijo.resetVisita();
        }
        Cola<Nodo<E>> colaVisita = new Cola<>();
        colaVisita.encolar(raiz);
        while(colaVisita.getTamano() > 0) {
            Nodo<E> nodo = colaVisita.desencolar();
            nodo.visitar();
            System.out.print(nodo.getContenido() + " - ");
            ListaEnlazadaDoble<Nodo<E>> hijosNoVisitadosYNoEnCola = new ListaEnlazadaDoble<>();
            for(Nodo<E> hijo : nodo.getHijos()) {
                if (hijo.getVisitado() > 0)
                    continue;
                boolean hijoEnCola = false;
                for(Nodo<E> enCola : colaVisita) {
                    if (hijo == enCola) {
                        hijoEnCola = true;
                        break;
                    }
                }
                if (hijoEnCola)
                    continue;

                hijosNoVisitadosYNoEnCola.preponer(hijo);
            }
            for(Nodo<E> nodosEncontrados : hijosNoVisitadosYNoEnCola) {
                colaVisita.encolar(nodosEncontrados);
            }
        }
        System.out.println();
    }

    /**
     * Recorrer en profundidad
     */
    public void recorrerDfs() {
        for(Nodo<E> nodoHijo : nodos.values()) {
            nodoHijo.resetVisita();
        }
        Pila<Nodo<E>> pilaVisita = new Pila<>();
        pilaVisita.apilar(raiz);
        while(pilaVisita.getTamano() > 0) {
            Nodo<E> nodo = pilaVisita.desapilar();
            nodo.visitar();
            System.out.print(nodo.getContenido() + " - ");
            ListaEnlazadaDoble<Nodo<E>> hijosNoVisitadosYNoEnPila = new ListaEnlazadaDoble<>();
            for(Nodo<E> hijo : nodo.getHijos()) {
                if (hijo.getVisitado() > 0)
                    continue;
                boolean hijoEnPila = false;
                for(Nodo<E> enPila : pilaVisita) {
                    if (hijo == enPila) {
                        hijoEnPila = true;
                        break;
                    }
                }
                if (hijoEnPila)
                    continue;

                hijosNoVisitadosYNoEnPila.preponer(hijo);
            }
            for(Nodo<E> nodosEncontrados : hijosNoVisitadosYNoEnPila) {
                pilaVisita.apilar(nodosEncontrados);
            }
        }
        System.out.println();
    }

    public void insertar(String padre, E hijo) {
        if (padre == null) {
            raiz = new Nodo(hijo);
            nodos.put(hijo.toString(), raiz);
            return;
        }
        Nodo<E> nodoPadre = nodos.get(padre);
        Nodo<E> nodoHijo = new Nodo<>(hijo);
        nodoPadre.insertarHijo(nodoHijo);
        nodos.put(hijo.toString(), nodoHijo);
    }

    public boolean eliminarNodo(String idEliminar) {
        if (raiz != null && raiz.getContenido().toString().equals(idEliminar)) {
            raiz = null;
            nodos.clear();
            return true;
        }

        for (Nodo<E> nodo : nodos.values()) {
            boolean eliminado = false;
            for (Nodo<E> hijo : nodo.getHijos()) {
                if (hijo.getContenido().toString().equals(idEliminar)) {
                    nodo.getHijos().eliminarPorValor(hijo);
                    eliminado = true;
                    break;
                }
            }
            if (eliminado) {
                // Se debe eliminar del mapa también para evitar conflictos futuros
                nodos.remove(idEliminar);
                return true;
            }
        }
        return false;
    }

    public Nodo<E> getRaiz() {
        return raiz;
    }

    public E buscar(String id) {
        if (raiz != null) {
            Nodo<E> resultado = raiz.buscar(id);
            if (resultado != null)
                return resultado.getContenido();
        }
        return null;
    }

    @Override
    public String toString() {
        if (raiz == null) {
            return "[VACIO]";
        }
        return raiz.toString();
    }

    public void setRaiz(Nodo<E> raiz) {
        this.raiz = raiz;
    }

    public class Nodo<E> {
        private E contenido;
        private ListaEnlazadaDoble<Nodo<E>> hijos;
        private int visitado;

        public Nodo(E contenido) {
            this.contenido = contenido;
            this.hijos = new ListaEnlazadaDoble<>();
            visitado = 0;
        }

        public void visitar() {
            visitado++;
        }

        public int getVisitado() {
            return visitado;
        }

        public void resetVisita() {
            visitado = 0;
        }

        public Nodo<E> buscar(String id) {
            if (contenido.toString().equals(id)) {
                return this;
            }
            for(Nodo<E> hijo : hijos) {
                Nodo<E> resultadoHijo = hijo.buscar(id);
                if (resultadoHijo != null)
                    return resultadoHijo;
            }
            return null;
        }

        public E getContenido() {
            return contenido;
        }

        public ListaEnlazadaDoble<Nodo<E>> getHijos() {
            return hijos;
        }

        @Override
        public String toString() {
            if(hijos.getTamano() == 0)
                return contenido.toString();
            StringBuilder resultado = new StringBuilder(contenido.toString());
            resultado.append("-(");
            String separador = "";
            for(Nodo<E> hijo : hijos) {
                resultado.append(separador);
                resultado.append(hijo.toString());
                separador = " ";
            }
            resultado.append(")");
            return resultado.toString();
        }

        public void insertarHijo(Nodo<E> hijo) {

            hijos.preponer(hijo);
        }
    }
}

