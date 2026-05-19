
package RedDePizarras.red;

import RedDePizarras.modelo.*;
import Utilidades.ListaEnlazadaDoble;
import java.awt.Color;

public class Protocolo {
    private ModeloPizarra modelo;

    public Protocolo(ModeloPizarra modelo) {
        this.modelo = modelo;
    }

    public String procesarComando(String linea, Conexion peer) {
        if (linea == null || linea.trim().isEmpty()) {
            return null;
        }

        System.out.println("<<< " + linea.trim()); 

        String[] tokens = linea.trim().split("\\s+");
        if (tokens.length == 0) return null;
        
        String comando = tokens[0].toUpperCase();

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
                    int qx = Integer.parseInt(tokens[1]);
                    int qy = Integer.parseInt(tokens[2]);
                    int lado = Integer.parseInt(tokens[3]); // Usamos el ancho como lado
                    
                    Cuadrado nuevoCuadrado = new Cuadrado(qx, qy, lado, Color.RED);
                    modelo.agregarFiguraRemota(nuevoCuadrado);
                    return "OK";
                } catch (Exception e) {
                    return "ERROR EN ARGUMENTOS CUADRADO";
                }

            case "CIRCULO":
                try {
                    int cx = Integer.parseInt(tokens[1]);
                    int cy = Integer.parseInt(tokens[2]);
                    int radio = Integer.parseInt(tokens[3]) / 2;
                    
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
        System.out.println(">>> " + mensaje.trim());
    }
}