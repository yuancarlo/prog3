package PreguntasExamen;
//10.	En la clase Lista de la materia, escribir el código del método
//public void unirCon(Lista<E> otraLista)  {
//	…
//}
//a.	Para que se puedan unir dos cadenas
public class UnirLista {
    public void unirCon(Lista<E> otraLista) {
        // 1. Validación de seguridad:
        // Si la otra lista es nula o está vacía, no hay nada que unir.
        if (otraLista == null || otraLista.getPrimero() == null) {
            return;
        }

        // 2. Caso especial: Si nuestra lista actual está vacía
        // Simplemente apuntamos nuestro 'primero' al 'primero' de la otra lista
        if (this.primero == null) {
            this.primero = otraLista.getPrimero();
        }
        // 3. Caso normal: Buscar el final de nuestra lista y enganchar
        else {
            Nodo<E> actual = this.primero;

            // Recorremos hasta llegar al ÚLTIMO nodo
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }

            // Enlazamos el último nodo de nuestra lista con el primero de la otra
            actual.setSiguiente(otraLista.getPrimero());
        }

        // 4. Actualizamos el tamaño total
        this.tamano += otraLista.tamano();
    }
}
