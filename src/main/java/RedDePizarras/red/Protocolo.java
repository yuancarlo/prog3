
package RedDePizarras.red;

import RedDePizarras.modelo.*;
import Utilidades.ListaEnlazadaDoble;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;

public class Protocolo {
    private static final Logger logger = LogManager.getRootLogger();
    private ModeloPizarra modelo;

    public Protocolo(ModeloPizarra modelo) {
        this.modelo = modelo;
    }

    public String procesarComando(String linea, Conexion peer) {
        if (linea == null || linea.trim().isEmpty()) {
            return null;
        }

        logger.info("<<< " + linea.trim());

        String[] partesComando = linea.trim().split("\\s+");
        if (partesComando.length == 0) return null;
        
        String comando = partesComando[0].toUpperCase();

        switch (comando) {
            case "HOLA":
                return "OK";

            case "LISTA":
                ListaEnlazadaDoble<Figura> propias = modelo.getListaPropia();
                int tamano = propias.getTamano();
                
                peer.enviarMensajeDirecto(String.valueOf(tamano));
                
                for (int i = 0; i < tamano; i++) {
                    Figura f = propias.getValorPorIndice(i);
                    if (f instanceof Circulo) {
                        Circulo c = (Circulo) f;
                        peer.enviarMensajeDirecto("CIRCULO " + c.getX() + " " + c.getY() + " " + c.diametro());
                    } else if (f instanceof Cuadrado) {
                        Cuadrado cu = (Cuadrado) f;
                        peer.enviarMensajeDirecto("CUADRADO " + cu.getX() + " " + cu.getY() + " " + cu.getLado() + " " + cu.getLado());
                    }
                }
                return "OK";

            case "FIGURA":
                return "OK";

            case "CUADRADO":
                try {
                    int qx = Integer.parseInt(partesComando[1]);
                    int qy = Integer.parseInt(partesComando[2]);
                    int lado = Integer.parseInt(partesComando[3]);
                    
                    Cuadrado nuevoCuadrado = new Cuadrado(qx, qy, lado, Color.RED);
                    modelo.agregarFiguraRemota(nuevoCuadrado);
                    return "OK";
                } catch (Exception e) {
                    return "ERROR EN ARGUMENTOS CUADRADO";
                }

            case "CIRCULO":
                try {
                    int cx = Integer.parseInt(partesComando[1]);
                    int cy = Integer.parseInt(partesComando[2]);
                    int radio = Integer.parseInt(partesComando[3]) / 2;
                    
                    Circulo nuevoCirculo = new Circulo(cx, cy, radio, Color.RED);
                    modelo.agregarFiguraRemota(nuevoCirculo);
                    return "OK";
                } catch (Exception e) {
                    return "ERROR EN ARGUMENTOS CIRCULO";
                }

            case "CHAU":
                return "OK";

            case "OK":
                return null; 

            default:
                if (comando.matches("\\d+")) {
                    return null; 
                }
                return "ERROR";
        }
    }

    public void enviarLog(String mensaje) {
        logger.info(">>> " + mensaje.trim());
    }
}