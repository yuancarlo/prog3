package AccesoADatos.dao;

public interface Repository<T> {
    void guardar(T t) throws Exception;
    void actualizar(T t) throws Exception;
    void eliminar(T t) throws Exception;
}