package AccesoADatos.configuracion;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import AccesoADatos.modelo.Persona; 

public final class HibernateSessionManager {
    private static final SessionFactory sessionFactory = construirSessionFactory();

    private static SessionFactory construirSessionFactory() throws ExceptionInInitializerError {
        try {
            
            Configuration configuration = new Configuration().configure("hibernate/hibernate.cfg.xml");
            
          
            configuration.addAnnotatedClass(Persona.class);
            
    
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError("Error al iniciar Hibernate: " + ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}