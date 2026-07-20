package AccesoADatos.configuracion;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionManager {
    private static final SessionFactory sessionFactory = construirSessionFactory();

    private static SessionFactory construirSessionFactory() throws ExceptionInInitializerError {
        try {
            return new Configuration().configure("hibernate/hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Error al iniciar Hibernate: " + ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}