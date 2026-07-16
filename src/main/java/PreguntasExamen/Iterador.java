package PreguntasExamen;

import java.util.Iterator;

//11.	De la definición del patrón de diseño Iterator. Indique cuál es su función principal, su diagrama de clases y de un ejemplo de su uso en Java.
public class Iterador {
    /**El patrón Iterator es un patrón de diseño de comportamiento que permite recorrer secuencialmente los elementos de una colección de datos
    como listas, pilas, árboles o grafos) sin exponer los detalles de su estructura interna.
     En lugar de que la colección misma maneje la lógica de cómo moverse de un elemento a otro, esta responsabilidad se extrae y se le entrega
     a un objeto independiente llamado iterador.*/
    /**Su función principal es permitir el recorrido secuencial de los elementos de una colección (como una lista, árbol o arreglo) sin exponer cómo están guardados
    internamente.
   Gracias a esto, al programa que consume los datos no le importa si tu colección usa arreglos estáticos, nodos enlazados o árboles binarios; solo le importa que
     puede pedir el "siguiente" elemento una y otra vez hasta que se acaben.*/
    /**
     +-------------------------+                      +-------------------------+
     |      <<Interface>>      |                      |      <<Interface>>      |
     |        Iterable         |                      |        Iterator         |
     +-------------------------+    crea / devuelve   +-------------------------+
     | + iterator(): Iterator  | - - - - - - - - - -> | + hasNext(): boolean    |
     +-------------------------+                      | + next(): Object        |
     ^                                    +-------------------------+
     |                                                 ^
     | implementa                                      | implementa
     |                                                 |
     +-------------------------+                      +-------------------------+
     |    ColeccionConcreta    |                      |    IteradorConcreto     |
     +-------------------------+   conoce / recorre   +-------------------------+
     | - datos: Estructura     | <------------------- | - posicionActual: int   |
     +-------------------------+                      | - coleccion: Coleccion  |
     | + iterator(): Iterator  |                      +-------------------------+
     +-------------------------+                      | + hasNext(): boolean    |
     | + next(): Object        |
     +-------------------------+
     */

}

// 1. La Colección Concreta (Implementa Iterable)
class ColeccionNombres implements Iterable<String> {
    private String[] nombres;

    public ColeccionNombres(String[] nombres) {
        this.nombres = nombres;
    }

    // Obligatorio por implementar Iterable. Aquí creamos "el motor" del recorrido.
    @Override
    public Iterator<String> iterator() {
        return new MiIterador();
    }

    // 2. El Iterador Concreto (Clase Interna)
    // Es buena práctica hacerla interna para que tenga acceso a los datos (nombres)
    private class MiIterador implements Iterator<String> {
        private int posicionActual = 0; // Guarda el estado del recorrido

        @Override
        public boolean hasNext() {
            // Verifica si todavía quedan elementos por recorrer
            return posicionActual < nombres.length;
        }

        @Override
        public String next() {
            // Retorna el elemento en la posición actual y LUEGO suma 1 a la posición
            String nombre = nombres[posicionActual];
            posicionActual++;
            return nombre;
        }
    }
}
