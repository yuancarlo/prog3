package AccesoADatos.dao;

import AccesoADatos.modelo.Persona;
import java.util.List;

public interface PersonaDbDao extends Repository<Persona> {
    List<Persona> listarPaginado(String busqueda, int pagina, int limite) throws Exception;
}
