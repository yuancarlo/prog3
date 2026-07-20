package AccesoADatos.dao;

import AccesoADatos.configuracion.GestorSesionHibernate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class RepositorioHibernateAbstracto<T, ID> implements Repositorio<T, ID> {

    protected final Logger registradorLog = LogManager.getRootLogger();

    protected interface OperacionBaseDatos<R> {
        R ejecutar(Session sesion) throws Exception;
    }

    protected <R> R ejecutarEnTransaccion(OperacionBaseDatos<R> operacion) throws Exception {
        Transaction transaccion = null;
        try (Session sesion = GestorSesionHibernate.obtenerInstancia().abrirSesion()) {
            transaccion = sesion.beginTransaction();
            R resultado = operacion.ejecutar(sesion);
            transaccion.commit();
            return resultado;
        } catch (Exception e) {
            if (transaccion != null) transaccion.rollback();
            registradorLog.error("Error en DB propagado a la capa visual", e);
            throw e; // Propagación directa
        }
    }
}