package AccesoADatos.configuracion;

import AccesoADatos.fabrica.FabricaPersistencia;
import AccesoADatos.modelo.EmpleadoBd;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class GestorSesionHibernate {

    private final Logger registradorLog = LogManager.getRootLogger();
    private static GestorSesionHibernate instancia;
    private SessionFactory fabricaSesiones;

    private GestorSesionHibernate() {}

    public static synchronized GestorSesionHibernate obtenerInstancia() {
        if (instancia == null) {
            instancia = new GestorSesionHibernate();
        }
        return instancia;
    }

    public void inicializar(FabricaPersistencia fabrica) throws Exception {
        if (fabricaSesiones == null) {
            String rutaConfiguracion = fabrica.obtenerRecursoConfiguracion();
            registradorLog.info("Inicializando Hibernate con: " + rutaConfiguracion);


            fabricaSesiones = new Configuration()
                    .configure(rutaConfiguracion)
                    .addAnnotatedClass(EmpleadoBd.class)
                    .buildSessionFactory();
        }
    }

    public Session abrirSesion() {
        return fabricaSesiones.openSession();
    }

    public void apagar() {
        if (fabricaSesiones != null) {
            registradorLog.info("Cerrando conexión de Hibernate.");
            fabricaSesiones.close();
        }
    }
}