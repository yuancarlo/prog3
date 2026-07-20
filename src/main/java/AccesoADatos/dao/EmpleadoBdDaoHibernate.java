package AccesoADatos.dao;


import AccesoADatos.modelo.EmpleadoBd;
import java.util.List;

public class EmpleadoBdDaoHibernate extends RepositorioHibernateAbstracto<EmpleadoBd, Integer> {

    @Override
    public EmpleadoBd guardarOActualizar(EmpleadoBd entidad) throws Exception {
        registradorLog.info("Guardando o actualizando empleado...");
        return ejecutarEnTransaccion(sesion -> sesion.merge(entidad));
    }

    @Override
    public void eliminarPorId(Integer id) throws Exception {
        registradorLog.info("Eliminando empleado ID: " + id);
        ejecutarEnTransaccion(sesion -> {
            EmpleadoBd empleado = sesion.get(EmpleadoBd.class, id);
            if (empleado != null) sesion.remove(empleado);
            return null;
        });
    }

    @Override
    public EmpleadoBd buscarPorId(Integer id) throws Exception {
        return ejecutarEnTransaccion(sesion -> sesion.get(EmpleadoBd.class, id));
    }

    @Override
    public List<EmpleadoBd> buscarPaginado(String textoBusqueda, int limite, int desplazamiento) throws Exception {
        registradorLog.info("Búsqueda paginada. Filtro: '" + textoBusqueda + "'");
        return ejecutarEnTransaccion(sesion -> {
            String hql = "FROM EmpleadoBd e WHERE lower(e.nombreCompleto) LIKE :texto ORDER BY e.id DESC";
            return sesion.createQuery(hql, EmpleadoBd.class)
                    .setParameter("texto", "%" + textoBusqueda.toLowerCase() + "%")
                    .setMaxResults(limite)
                    .setFirstResult(desplazamiento)
                    .list();
        });
    }
}