package AccesoADatos.factory;


import AccesoADatos.dao.HibernatePersonaDbDao;
import AccesoADatos.dao.PersonaDbDao;

public class SqlitePersistenceFactory implements PersistenceFactory {
    @Override
    public PersonaDbDao getPersonaDao() {
        return new HibernatePersonaDbDao();
    }
}