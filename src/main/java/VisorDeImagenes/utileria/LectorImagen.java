package VisorDeImagenes.utileria;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class LectorImagen {
    private static Logger logger = LogManager.getRootLogger();

    public static BufferedImage leerImagen(String ruta){
        logger.info("Inicio de lectura de imagen");
        try {
            if (ruta.startsWith("http://") || ruta.startsWith("https://")){
                logger.debug("Ruta web detectada, usando URL");
                return ImageIO.read(new URL(ruta));
            } else {
                logger.debug("Ruta de archivo detectada, usando archivo");
                return ImageIO.read(new File(ruta));
            }
        } catch (Exception e){
            logger.error("Error al leer imagen: " + ruta, e);
            return null;
        }
    }
}