package PreguntasExamen;
//13.	Escriba el código del método insertar(E obj) en una Cadena que sea ordenada.
// Es decir, al insertar el objeto, la Cadena lo coloca en el lugar que corresponde y entonces la Cadena siempre esta ordenada.
public class InsertarLista <E> {
    public int tamano;
    public Anillo.Nodo<E> primero;
    public void insertar(E o) {
        Anillo.Nodo<E> nuevo = new Anillo.Nodo<>(o);

        // 1. Si el objeto no se puede comparar, lo insertamos al inicio por defecto
        if (!(o instanceof Comparable)) {
            nuevo.setSiguiente(primero);
            primero = nuevo;
            tamano++;
            return;
        }

        Comparable<E> objComparable = (Comparable<E>) o;

        // 2. Si la lista está vacía O el nuevo elemento es menor o igual al primero
        // Lo insertamos en la primera posición
        if (primero == null || objComparable.compareTo(primero.getContenido()) <= 0) {
            nuevo.setSiguiente(primero);
            primero = nuevo;
            tamano++;
            return;
        }

        // 3. Buscar la posición correcta en el medio o al final
        Anillo.Nodo<E> actual = primero;

        // La condición combinada (tu aporte):
        // Mientras NO sea el último nodo Y el nuevo objeto sea mayor que el siguiente
        while (actual.getSiguiente() != null &&
                objComparable.compareTo(actual.getSiguiente().getContenido()) > 0) {
            actual = actual.getSiguiente();
        }

        // 4. Conectamos los punteros en la posición encontrada
        nuevo.setSiguiente(actual.getSiguiente());
        actual.setSiguiente(nuevo);
        tamano++;
    }
}
