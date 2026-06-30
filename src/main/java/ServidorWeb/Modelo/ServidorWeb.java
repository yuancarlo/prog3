package ServidorWeb.Modelo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorWeb implements Runnable, PropertyChangeListener {
    private static final Logger logger = LogManager.getRootLogger();
    private final int puerto;
    private volatile EstadoServidor estado;
    private ServerSocket srv;
    public final PropertyChangeSupport observador;

    private int clientesAtendidos = 0;
    private long totalBytesEntrada = 0;
    private long totalBytesSalida = 0;

    public ServidorWeb(int puerto) {
        this.puerto = puerto;
        this.estado = EstadoServidor.CREADO;
        this.observador = new PropertyChangeSupport(this);
        logger.info("Instancia del ServidorWeb creada en el puerto " + puerto + " (Estado: CREADO).");
    }

    public void comenzar() {
        if (estado != EstadoServidor.COMENZADO) {
            logger.info("Iniciando secuencia de arranque del servidor...");
            EstadoServidor estadoAnterior = this.estado;
            this.estado = EstadoServidor.COMENZADO;

            new Thread(this, "Hilo-ServidorPrincipal").start();

            observador.firePropertyChange("ESTADO", estadoAnterior, EstadoServidor.COMENZADO);
        } else {
            logger.warn("Intento de iniciar el servidor cuando ya estaba COMENZADO.");
        }
    }

    public void pararServidor() {
        if (estado == EstadoServidor.COMENZADO) {
            logger.info("Secuencia de apagado solicitada. Deteniendo servidor y rechazando nuevas conexiones...");
            EstadoServidor estadoAnterior = this.estado;
            this.estado = EstadoServidor.DETENIDO;
            try {
                if (srv != null && !srv.isClosed()) {
                    srv.close();
                    logger.info("ServerSocket cerrado correctamente.");
                }
            } catch (IOException e) {
                logger.error("Excepción al intentar cerrar el ServerSocket", e);
            }
            observador.firePropertyChange("ESTADO", estadoAnterior, EstadoServidor.DETENIDO);
            logger.info("El servidor se ha detenido por completo.");
        }
    }

    @Override
    public void run() {
        try {
            srv = new ServerSocket(puerto);
            logger.info("Servidor escuchando activamente en el puerto " + puerto);

            while (estado == EstadoServidor.COMENZADO) {
                Socket cliente = srv.accept();
                clientesAtendidos++;

                // Log detallado de la conexión entrante
                String ipCliente = cliente.getInetAddress().getHostAddress();
                logger.info("NUEVA CONEXIÓN ACEPTADA: Cliente #" + clientesAtendidos + " [IP: " + ipCliente + "]");

                observador.firePropertyChange("CLIENTES", clientesAtendidos - 1, clientesAtendidos);

                ProtocoloWeb protocolo = new ProtocoloWeb(cliente);
                protocolo.observadorProtocolo.addPropertyChangeListener(this);

                new Thread(protocolo, "Hilo-Cliente-" + clientesAtendidos).start();
            }
        } catch (IOException e) {
            if (estado == EstadoServidor.COMENZADO) {
                logger.error("Caída de conexión inesperada mientras el servidor estaba activo", e);
            } else {
                logger.info("Interrupción controlada del hilo de escucha (Servidor detenido).");
            }
        }
    }

    public boolean isCorriendo() {
        return estado == EstadoServidor.COMENZADO;
    }

    public EstadoServidor getEstado() {
        return estado;
    }

    @Override
    public synchronized void propertyChange(PropertyChangeEvent evt) {
        if ("NUEVO_TRAFICO".equals(evt.getPropertyName())) {
            long[] nuevosBytes = (long[]) evt.getNewValue();
            this.totalBytesEntrada += nuevosBytes[0];
            this.totalBytesSalida += nuevosBytes[1];

            observador.firePropertyChange("TRAFICO", null, new long[]{totalBytesEntrada, totalBytesSalida});
        }
    }

    public int getClientesAtendidos() { return clientesAtendidos; }
}