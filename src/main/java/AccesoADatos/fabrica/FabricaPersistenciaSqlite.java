package AccesoADatos.fabrica;


public class FabricaPersistenciaSqlite implements FabricaPersistencia {
    @Override
    public String obtenerRecursoConfiguracion() {
        return "hibernate/hibernate-sqlite.cfg.xml";
    }
}
