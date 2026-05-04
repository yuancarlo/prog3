package Cartas.programa;

import Cartas.servicio.Barajista;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info(" INCIANDO PROCESO DE BARAJADO ");

        try {

            Barajista barajista = new Barajista();
            logger.info("Componentes inicializados correctamente.");

            barajista.barajarMazo();

            String reporteFinal = barajista.interpretarResultados();

            logger.warn(reporteFinal);

        } catch (Exception e) {

            logger.error("Se produjo un error inesperado durante la ejecución: {}", e.getMessage());
        }

        logger.info(" FINALIZACIÓN DEL PROCESO ");
    }
}