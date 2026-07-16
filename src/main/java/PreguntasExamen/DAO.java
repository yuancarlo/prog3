package PreguntasExamen;
import java.util.ArrayList;
import java.util.List;



//15.	De un ejemplo de uso del patrón de diseño DAO, en qué consiste el mismo y dé un ejemplo de su uso en Java.
public class DAO {
    /**El Patrón de Diseño DAO (Data Access Object) es un patrón estructural cuyo propósito principal es separar la lógica de negocio de la lógica de acceso a los datos.
     En palabras sencillas: tu programa no debería saber si los datos vienen de una base de datos MySQL, de un archivo de texto, o de la nube.
     El DAO actúa como un "intermediario" o "traductor". La aplicación le pide datos al DAO, y el DAO es el único responsable de ir a buscar esos datos y convertirlos
     en objetos que el programa entienda.
     ¿En qué consiste? (Estructura típica)
     Generalmente se compone de 3 partes:
     El Modelo (Entity): La clase que representa un objeto de la vida real (ej. Estudiante).
     La Interfaz DAO: Define qué acciones se pueden hacer (CRUD: Crear, Leer, Actualizar, Borrar).
     La Implementación DAO: El código real que dice cómo conectarse a la base de datos y hacer esas acciones.*/

}
class Estudiante {
    private int id;
    private String nombre;

    public Estudiante(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

interface EstudianteDAO {
    List<Estudiante> obtenerTodos();
    Estudiante obtenerPorId(int id);
    void guardar(Estudiante estudiante);
    void eliminar(Estudiante estudiante);
}

class EstudianteDAOImpl implements EstudianteDAO {
    // Simulamos una base de datos usando una lista
    private List<Estudiante> baseDeDatosEstudiantes;

    public EstudianteDAOImpl() {
        baseDeDatosEstudiantes = new ArrayList<>();
        baseDeDatosEstudiantes.add(new Estudiante(1, "Ana"));
        baseDeDatosEstudiantes.add(new Estudiante(2, "Juan"));
    }

    @Override
    public List<Estudiante> obtenerTodos() {
        return baseDeDatosEstudiantes;
    }

    @Override
    public Estudiante obtenerPorId(int id) {
        for (Estudiante e : baseDeDatosEstudiantes) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    @Override
    public void guardar(Estudiante estudiante) {
        baseDeDatosEstudiantes.add(estudiante);
        System.out.println("Estudiante guardado en la base de datos: " + estudiante.getNombre());
    }

    @Override
    public void eliminar(Estudiante estudiante) {
        baseDeDatosEstudiantes.remove(estudiante);
        System.out.println("Estudiante eliminado: " + estudiante.getNombre());
    }
}
