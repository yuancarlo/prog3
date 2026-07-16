package PreguntasExamen;
//14.	Proponga una implementación de Anillo. Un Anillo es una cadena donde el último elemento no tiene un puntero a nulo sino al primer elemento.
public class Anillo<E> {
    /**Un Anillo (conocido formalmente en estructuras de datos como una Lista Enlazada Circular) funciona exactamente igual que una lista enlazada simple,
    con una diferencia clave: el puntero siguiente del último nodo siempre apunta de regreso al primero, formando un ciclo continuo.*/
    protected Nodo<E> primero;
    protected int tamano;

    public Anillo() {
        tamano = 0;
        primero = null;
    }

    // Método para agregar un elemento al final del Anillo
    public void adicionar(E o) {
        Nodo<E> nuevo = new Nodo<>(o);

        // CASO 1: El anillo está vacío
        if (primero == null) {
            primero = nuevo;
            // LA CLAVE DEL ANILLO: Si es el único elemento, se apunta a sí mismo
            primero.setSiguiente(primero);
        }
        // CASO 2: El anillo ya tiene elementos
        else {
            Nodo<E> actual = primero;

            // Buscamos el último elemento.
            // En una lista normal buscaríamos "null". En un anillo, buscamos "primero".
            while (actual.getSiguiente() != primero) {
                actual = actual.getSiguiente();
            }

            // Enganchamos el nuevo nodo al final
            actual.setSiguiente(nuevo);
            // LA CLAVE DEL ANILLO: El nuevo último nodo debe apuntar de nuevo al primero
            nuevo.setSiguiente(primero);
        }
        tamano++;
    }

    // Método para imprimir y comprobar que da la vuelta
    @Override
    public String toString() {
        if (primero == null) {
            return "{VACIA}";
        }

        StringBuilder resultado = new StringBuilder();
        Nodo<E> actual = primero;

        // Usamos un do-while para dar la vuelta exacta y evitar un bucle infinito
        do {
            resultado.append(actual.toString());
            actual = actual.getSiguiente();
        } while (actual != primero); // Se detiene cuando vuelve a llegar al principio

        resultado.append("(vuelve al inicio)");
        return resultado.toString();
    }

    // Tu misma clase Nodo (intacta)
    static class Nodo<E> {
        private E contenido;
        private Nodo<E> siguiente;

        public Nodo(E o) {
            contenido = o;
            siguiente = null;
        }

        public E getContenido() { return contenido; }
        public void setContenido(E contenido) { this.contenido = contenido; }
        public Nodo<E> getSiguiente() { return siguiente; }
        public void setSiguiente(Nodo<E> siguiente) { this.siguiente = siguiente; }

        @Override
        public String toString() {
            return "[" + contenido.toString() + "] -> ";
        }
    }
}
