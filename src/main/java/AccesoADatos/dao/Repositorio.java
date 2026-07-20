package AccesoADatos.dao;

import java.util.List;

public interface Repositorio<T, ID> {
    T guardarOActualizar(T entidad) throws Exception;
    void eliminarPorId(ID id) throws Exception;
    T buscarPorId(ID id) throws Exception;
    List<T> buscarPaginado(String textoBusqueda, int limite, int desplazamiento) throws Exception;
}