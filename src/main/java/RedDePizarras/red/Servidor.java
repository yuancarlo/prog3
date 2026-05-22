package RedDePizarras.red;

import RedDePizarras.modelo.ModeloPizarra;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends Thread {
    private static final Logger logger = LogManager.getRootLogger();
    private int puerto;
    private ModeloPizarra modelo;
    private ServerSocket serverSocket;
    private Conexion peerActual;

    public Servidor(int puerto, ModeloPizarra modelo) {
        this.puerto = puerto;
        this.modelo = modelo;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(puerto);
            logger.info("Servidor P2P a la escucha en el puerto: " + puerto);
            
            Socket socketCliente = serverSocket.accept();
            logger.info("Conexión remota entrante aceptada.");
            
            peerActual = new Conexion(socketCliente, modelo);
            peerActual.start();
            
        } catch (IOException e) {
            logger.info("Servidor P2P detenido o puerto liberado.");
        }
    }

    public Conexion getPeerActual() {
        return peerActual;
    }

    public void apagar() {
        try {
            if (peerActual != null) peerActual.desconectar();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            logger.error("Error al apagar el servidor: " + e.getMessage());
        }
    }
}