package RedDePizarras.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.Socket;

import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import RedDePizarras.modelo.Circulo;
import RedDePizarras.modelo.Cuadrado;
import RedDePizarras.modelo.Figura;
import RedDePizarras.modelo.ModeloPizarra;
import RedDePizarras.red.Protocolo;
import RedDePizarras.red.Conexion;
import RedDePizarras.red.Servidor;
import Utilidades.ListaEnlazadaDoble;

public class VentanaPrincipal extends JFrame  {
    private static final Logger logger = LogManager.getRootLogger();
    private PanelSeleccion panelSeleccion;
    private PanelPizarra panelPizarra;
    private ModeloPizarra modeloPizarra;
    private ControlMouse controlMouse;

    private Servidor servidorP2P;
    private Conexion clientePeer;
    private JTextField campoPuertoLocal;
    private JTextField campoPuertoRemoto;
    private JButton botonConectar;
    private JButton botonDesconectar;
    

    public VentanaPrincipal(){
        modeloPizarra = new ModeloPizarra();
        panelSeleccion = new PanelSeleccion();
        panelPizarra = new PanelPizarra(modeloPizarra);
        controlMouse = new ControlMouse();
        panelPizarra.addMouseListener(controlMouse);
        panelPizarra.addMouseMotionListener(controlMouse);

        JPanel panelRed = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        campoPuertoLocal = new JTextField("5000", 4);
        campoPuertoRemoto = new JTextField("5001", 4);
        botonConectar = new JButton("Conectar ");
        botonDesconectar = new JButton("Desconectar");
        
        panelRed.add(new JLabel("Mi Puerto:"));
        panelRed.add(campoPuertoLocal);
        panelRed.add(new JLabel("Puerto Par:"));
        panelRed.add(campoPuertoRemoto);
        panelRed.add(botonConectar);
        panelRed.add(botonDesconectar);

        JPanel contenedorNorte = new JPanel(new BorderLayout());
        contenedorNorte.add(panelSeleccion, BorderLayout.WEST);
        contenedorNorte.add(panelRed, BorderLayout.EAST);

        setSize(800, 600);
        setLayout(new BorderLayout());
        add(contenedorNorte, BorderLayout.NORTH);
        add(panelPizarra, BorderLayout.CENTER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        inicializarServidor();

        botonConectar.addActionListener(e -> iniciarConexionHaciaPeer());
        botonDesconectar.addActionListener(e -> procesarSalida());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                procesarSalida();
            }
        });

    }

    private void inicializarServidor() {
        int puertoLocal = Integer.parseInt(campoPuertoLocal.getText().trim());
        servidorP2P = new Servidor(puertoLocal, modeloPizarra);
        servidorP2P.start();
    }

    private void iniciarConexionHaciaPeer() {
        int puertoRemoto = Integer.parseInt(campoPuertoRemoto.getText().trim());
        new Thread(() -> {
            try {
                Socket socket = new Socket("localhost", puertoRemoto);
                clientePeer = new Conexion(socket, modeloPizarra);
                clientePeer.start();
                
                clientePeer.enviarMensaje("HOLA");
                
                Thread.sleep(200); 
                
                clientePeer.enviarMensaje("LISTA");
                
            } catch (Exception ex) {
                System.err.println("No se pudo conectar con el peer remoto: " + ex.getMessage());
            }
        }).start();
    }

    private void procesarSalida() {
        if (clientePeer != null) {
            clientePeer.ejecutarCierre();
        } else if (servidorP2P != null && servidorP2P.getPeerActual() != null) {
            servidorP2P.getPeerActual().ejecutarCierre();
        }
        
        try { Thread.sleep(300); } catch (InterruptedException ex) {}
        
        if (servidorP2P != null) servidorP2P.apagar();
        System.exit(0);
    }



    private class ControlMouse extends MouseAdapter {
    private Figura figuraSeleccionadaActualmente = null;

    @Override
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        
        figuraSeleccionadaActualmente = null;
        ListaEnlazadaDoble<Figura> figuras = modeloPizarra.getListaPropia();

        for (int i = figuras.getTamano() - 1; i >= 0; i--) {
            Figura f = figuras.getValorPorIndice(i);
            
            
            if (f.coordenadaDentroDeFigura(mouseX, mouseY) && figuraSeleccionadaActualmente == null) {
                figuraSeleccionadaActualmente = f;
                f.setSeleccionado(); 
                //logger.info("Colisión exitosa. Objeto capturado en el índice de lista: " + i);
                break; 
            }
            
        }

        if (figuraSeleccionadaActualmente == null) {
            String tipo = panelSeleccion.getSeleccion();
            int tamañoFigura = panelSeleccion.getTamañoEspecificado();

            //logger.info("Espacio vacío detectado. Solicitando creación al modelo: " + tipo + " en (" + mouseX + "," + mouseY + ")");

            if (tipo.equals("CUADRADO")) {
                Cuadrado nuevoCuadrado = new Cuadrado(mouseX-tamañoFigura/2, mouseY-tamañoFigura/2, tamañoFigura, Color.BLUE);
                modeloPizarra.agregarFiguraPropia(nuevoCuadrado);
                
            } else {
                int radio = tamañoFigura / 2;
                Circulo nuevoCirculo = new Circulo(mouseX, mouseY, radio, Color.BLUE);
                modeloPizarra.agregarFiguraPropia(nuevoCirculo);
    
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (figuraSeleccionadaActualmente != null) {
            int mouseX = e.getX();
            int mouseY = e.getY();

            if (figuraSeleccionadaActualmente instanceof Cuadrado) {
                Cuadrado c = (Cuadrado) figuraSeleccionadaActualmente;
                figuraSeleccionadaActualmente.setPosicion(mouseX - c.getLado() / 2, mouseY - c.getLado() / 2);
            } else {
                figuraSeleccionadaActualmente.setPosicion(mouseX, mouseY);
            }
            
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (figuraSeleccionadaActualmente != null) {
            //logger.info("MouseReleased: Soltando control físico de la figura.");
            figuraSeleccionadaActualmente.deseleccionar();
            figuraSeleccionadaActualmente = null;
        }
    }
    }


    public static void main(String[] args){

         new VentanaPrincipal();
        
       
    }


}


