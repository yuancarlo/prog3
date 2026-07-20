package AccesoADatos.dao;

import AccesoADatos.configuracion.HibernateSessionManager;
import AccesoADatos.modelo.Persona;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class HibernatePersonaDbDao implements PersonaDbDao {

    @Override
    public void guardar(Persona persona) throws Exception {
        Transaction tx = null;
        try (Session session = HibernateSessionManager.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(persona);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void actualizar(Persona persona) throws Exception {
        Transaction tx = null;
        try (Session session = HibernateSessionManager.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(persona);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void eliminar(Persona persona) throws Exception {
        Transaction tx = null;
        try (Session session = HibernateSessionManager.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.remove(session.contains(persona) ? persona : session.merge(persona));
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public List<Persona> listarPaginado(String busqueda, int pagina, int limite) throws Exception {
        try (Session session = HibernateSessionManager.getSessionFactory().openSession()) {
            String hql = "FROM Persona p WHERE p.nombre LIKE :busqueda ORDER BY p.id DESC";
            Query<Persona> query = session.createQuery(hql, Persona.class);
            query.setParameter("busqueda", "%" + (busqueda == null ? "" : busqueda) + "%");

            query.setFirstResult((pagina - 1) * limite);
            query.setMaxResults(limite);

            return query.list();
        }
    }
}