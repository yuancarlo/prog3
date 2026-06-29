package ServidorWeb.Modelo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// Implementa PropertyChangeListener para escuchar a sus hilos hijos
public class ServidorWeb implements Runnable, PropertyChangeListener {
    private static final Logger logger = LogManager.getRootLogger();
    private final int puerto;
    private volatile boolean corriendo;
    private ServerSocket srv;
    public final PropertyChangeSupport observador;

    private int clientesAtendidos = 0;
    private long totalBytesEntrada = 0;
    private long totalBytesSalida = 0;

    public ServidorWeb(int puerto) {
        this.puerto = puerto;
        this.corriendo = false;
        this.observador = new PropertyChangeSupport(this);
    }

    public void comenzar() {
        if (!corriendo) {
            corriendo = true;
            new Thread(this).start();
            observador.firePropertyChange("ESTADO", "PARADO", "CORRIENDO");
        }
    }

    public void pararServidor() {
        corriendo = false;
        try {
            if (srv != null && !srv.isClosed()) {
                srv.close();
            }
        } catch (IOException e) {
            logger.error("Error al cerrar ServerSocket", e);
        }
        observador.firePropertyChange("ESTADO", "CORRIENDO", "PARADO");
    }

    @Override
    public void run() {
        try {
            srv = new ServerSocket(puerto);
            logger.info("Servidor escuchando en puerto " + puerto);

            while (corriendo) {
                Socket cliente = srv.accept();
                clientesAtendidos++;
                observador.firePropertyChange("CLIENTES", clientesAtendidos - 1, clientesAtendidos);

                // Creamos el protocolo
                ProtocoloWeb protocolo = new ProtocoloWeb(cliente);
                // ¡El Servidor se suscribe como OYENTE del hilo antes de iniciarlo!
                protocolo.observadorProtocolo.addPropertyChangeListener(this);

                new Thread(protocolo).start();
            }
        } catch (IOException e) {
            if(corriendo) logger.error("Error en la conexión", e);
        }
    }

    // Método que reacciona a los "gritos" (eventos) del ProtocoloWeb
    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        if ("NUEVO_TRAFICO".equals(evt.getPropertyName())) {
            long[] nuevosBytes = (long[]) evt.getNewValue();
            this.totalBytesEntrada += nuevosBytes[0];
            this.totalBytesSalida += nuevosBytes[1];

            // Re-transmitimos la información acumulada a la ventana (UI)
            observador.firePropertyChange("TRAFICO", null, new long[]{totalBytesEntrada, totalBytesSalida});
        }
    }

    public boolean isCorriendo() { return corriendo; }
    public int getClientesAtendidos() { return clientesAtendidos; }
}