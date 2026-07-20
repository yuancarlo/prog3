package AccesoADatos;


import AccesoADatos.configuracion.GestorSesionHibernate;
import AccesoADatos.controlador.EmpleadoControlador;
import AccesoADatos.dao.EmpleadoBdDaoHibernate;
import AccesoADatos.fabrica.FabricaPersistenciaSqlite;
import AccesoADatos.vista.VentanaEmpleado;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class Main {

    private static final Logger registradorLog = LogManager.getRootLogger();

    public static void main(String[] args) {
        registradorLog.info("Iniciando aplicación...");

        try {
            GestorSesionHibernate.obtenerInstancia().inicializar(new FabricaPersistenciaSqlite());

            SwingUtilities.invokeLater(() -> {
                VentanaEmpleado ventana = new VentanaEmpleado();
                new EmpleadoControlador(ventana, new EmpleadoBdDaoHibernate());
                ventana.setVisible(true);
            });

        } catch (Exception e) {
            registradorLog.fatal("Error crítico al arrancar", e);
            JOptionPane.showMessageDialog(null, "Error al arrancar la BD: " + e.getMessage());
            System.exit(1);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            GestorSesionHibernate.obtenerInstancia().apagar();
        }));
    }
}
