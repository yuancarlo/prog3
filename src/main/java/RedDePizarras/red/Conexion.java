package RedDePizarras.red;

import RedDePizarras.modelo.*;
import java.io.*;
import java.net.Socket;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Conexion extends Thread implements PropertyChangeListener {
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
        this.modelo.addObserver(this); // Escuchar cambios locales para transmitirlos

        try {
            this.entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.salida = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Error al inicializar buffers de comunicación: " + e.getMessage());
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
            System.out.println("Conexión finalizada o perdida con el peer remoto.");
        } finally {
            desconectar();
        }
    }

    // Envía un mensaje imprimiendo el Log obligatorio de salida
    public synchronized void enviarMensaje(String mensaje) {
        protocolo.enviarLog(mensaje);
        salida.println(mensaje);
    }

    // Utilizado internamente para enviar datos intermedios sin re-procesar flujos de logs dobles
    public synchronized void enviarMensajeDirecto(String mensaje) {
        System.out.println(">>> " + mensaje);
        salida.println(mensaje);
    }

    // Implementación del flujo de desconexión ordenado
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
            System.err.println("Error cerrando sockets de comunicación: " + e.getMessage());
        }
    }

    /**
     * Requisito 8: Cuando dibujamos una figura localmente, este observer la captura
     * y la envía por la red automáticamente respetando el protocolo secuencial.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("NUEVA_FIGURA".equals(evt.getPropertyName())) {
            Figura f = (Figura) evt.getNewValue();
            
            // Filtrar para no reenviar lo que el otro ya nos mandó como remoto
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