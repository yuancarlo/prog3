package RedDePizarras.red;

import RedDePizarras.modelo.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Conexion extends Thread implements PropertyChangeListener {
    private static final Logger logger = LogManager.getRootLogger();
    private Socket socket;
    private BufferedReader entrada;
    private PrintWriter salida;
    private Protocolo protocolo;
    private ModeloPizarra modelo;
    private boolean activo;

    public Conexion(Socket socket, ModeloPizarra modelo) {
        this.socket = socket;
        this.modelo = modelo;
        this.protocolo = new Protocolo(modelo);
        this.activo = true;
        this.modelo.addObserver(this);

        try {
            this.entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.salida = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            logger.error("Error al inicializar buffers de comunicación: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            String linea;
            while (activo && (linea = entrada.readLine()) != null) {
                String respuesta = protocolo.procesarComando(linea, this);
                if (respuesta != null) {
                    enviarMensaje(respuesta);
                }
                if (linea.equalsIgnoreCase("CHAU")) {
                    break;
                }
            }
        } catch (IOException e) {
            logger.info("Conexión finalizada o perdida con el peer remoto.");
        } finally {
            desconectar();
        }
    }


    public synchronized void enviarMensaje(String mensaje) {
        protocolo.enviarLog(mensaje);
        salida.println(mensaje);
    }


    public synchronized void enviarMensajeDirecto(String mensaje) {
        logger.info(">>> " + mensaje);
        salida.println(mensaje);
    }


    public void ejecutarCierre() {
        enviarMensaje("CHAU");
    }

    public void desconectar() {
        activo = false;
        try {
            if (entrada != null) entrada.close();
            if (salida != null) salida.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            logger.error("Error cerrando sockets de comunicación: " + e.getMessage());
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("NUEVA_FIGURA".equals(evt.getPropertyName())) {
            Figura f = (Figura) evt.getNewValue();
            

            if (modelo.getListaPropia().getTamano() > 0 && contieneFigura(modelo.getListaPropia(), f)) {
                new Thread(() -> {
                    enviarMensaje("FIGURA");
                    if (f instanceof Cuadrado) {
                        Cuadrado q = (Cuadrado) f;
                        enviarMensaje("CUADRADO " + q.getX() + " " + q.getY() + " " + q.getLado() + " " + q.getLado());
                    } else if (f instanceof Circulo) {
                        Circulo c = (Circulo) f;
                        enviarMensaje("CIRCULO " + c.getX() + " " + c.getY() + " " + c.diametro());
                    }
                }).start();
            }
        }
    }

    private boolean contieneFigura(Utilidades.ListaEnlazadaDoble<Figura> lista, Figura f) {
        for (int i = 0; i < lista.getTamano(); i++) {
            if (lista.getValorPorIndice(i) == f) return true;
        }
        return false;
    }
}