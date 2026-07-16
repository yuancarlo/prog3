package PreguntasExamen;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

//7.	De la definición del patrón de diseño Observer. Indique cuál es su función principal, su diagrama de clases y de un ejemplo de su uso en Java.
public class Observer {
    /**Definición Formal
     El patrón Observer (Observador) es un patrón de diseño de comportamiento que define una dependencia de "uno a muchos" entre objetos.
     Su regla es simple: cuando un objeto cambia de estado, todos sus dependientes son notificados y se actualizan automáticamente.
     Función Principal
     Su función principal es desacoplar el objeto que genera la información (el Sujeto) de los objetos que la consumen (los Observadores).
     En lugar de que la Interfaz Gráfica tenga que estar preguntándole a cada rato al modelo "¿Ya cambiaste? ¿Ya cambiaste?",
     el modelo simplemente dice: "Me voy a dedicar a mi lógica; si algo cambia, yo les aviso a los que se hayan suscrito".*/
    /**
     +-------------------------------+         delega        +-------------------------------+
     |         CanalNoticias         | --------------------> |     PropertyChangeSupport     |
     |          (El Sujeto)          |                       |    (Gestor de Composición)    |
     +-------------------------------+                       +-------------------------------+
     | - ultimaNoticia: String       |                       | - listeners: List             |
     | - observado: Property...      | 1                   1 | - source: Object              |
     +-------------------------------+ <-------------------- +-------------------------------+
     | + addObserver(listener)       |         conoce        | + addPropertyChangeListener() |
     | + publicarNoticia(noticia)    |                       | + firePropertyChange()        |
     +-------------------------------+                       +-------------------------------+
     |
     | notifica a
     v
     +-------------------------------+                       +-------------------------------+
     |          Suscriptor           |                       |         <<Interface>>         |
     |        (El Observador)        |                       |    PropertyChangeListener     |
     +-------------------------------+                       +-------------------------------+
     | - nombre: String              | --------------------> | + propertyChange(evento)      |
     +-------------------------------+      implementa       +-------------------------------+
     */
}


class CanalNoticias {
    private String ultimaNoticia;

    // COMPOSICIÓN: La clase "tiene un" soporte para cambios, no hereda de él.
    private PropertyChangeSupport observado;

    public CanalNoticias() {
        // Le pasamos 'this' para que el gestor sepa quién está enviando el evento
        observado = new PropertyChangeSupport(this);
    }

    // Delegamos la acción de agregar observadores al objeto compuesto
    public void addObserver(PropertyChangeListener listener) {
        observado.addPropertyChangeListener(listener);
    }

    public void publicarNoticia(String nuevaNoticia) {
        String noticiaAnterior = this.ultimaNoticia;
        this.ultimaNoticia = nuevaNoticia;

        System.out.println("Canal: Publicando nueva noticia...");

        // Delegamos la notificación.
        // Parámetros: Nombre del evento, valor viejo, valor nuevo.
        observado.firePropertyChange("NOTICIA_NUEVA", noticiaAnterior, nuevaNoticia);
    }
}
class Suscriptor implements PropertyChangeListener {
    private String nombre;

    public Suscriptor(String nombre) {
        this.nombre = nombre;
    }

    // Este es el método que se dispara automáticamente
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Validamos a qué evento estamos reaccionando
        if ("NOTICIA_NUEVA".equals(evt.getPropertyName())) {
            System.out.println(nombre + " recibió la noticia: " + evt.getNewValue());
        }
    }
}
class Main {
    public static void main(String[] args) {
        CanalNoticias cnn = new CanalNoticias();

        Suscriptor juan = new Suscriptor("Juan");
        Suscriptor maria = new Suscriptor("María");

        // Suscribimos los observadores
        cnn.addObserver(juan);
        cnn.addObserver(maria);

        // Disparamos el cambio
        cnn.publicarNoticia("¡El examen de Progra 3 será un éxito!");
    }
}
