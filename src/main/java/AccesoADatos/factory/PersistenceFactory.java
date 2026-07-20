package AccesoADatos.factory;


import AccesoADatos.dao.PersonaDbDao;

public interface PersistenceFactory {
    PersonaDbDao getPersonaDao();
}