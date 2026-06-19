package ArbolJSON.Controlador;

import ArbolJSON.Modelo.ArbolModelo;
import ArbolJSON.Vista.VentanaPrincipal;
import Utilidades.Arbol;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

public class ArbolControlador implements PropertyChangeListener {
    private static final Logger logger = LogManager.getRootLogger();
    private ArbolModelo modelo;
    private VentanaPrincipal vista;

    public ArbolControlador(ArbolModelo modelo, VentanaPrincipal vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.modelo.agregarObservador(this);
        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getPanelIzquierdo().getBtnActualizar().addActionListener(e -> {
            logger.info("Botón Actualizar presionado");
            String json = vista.getPanelIzquierdo().getJsonTexto();
            modelo.actualizarDesdeJson(json);
        });

        vista.getPanelIzquierdo().getBtnSubirDocumento().addActionListener(e -> {
            logger.info("Botón Subir Documento presionado");
            vista.getPanelIzquierdo().cargarArchivo();
        });

        vista.getPanelDerecho().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Map.Entry<Rectangle, String> entry : vista.getPanelDerecho().getAreasClickeables().entrySet()) {
                    if (entry.getKey().contains(e.getPoint())) {
                        mostrarMenuOpciones(entry.getValue(), e);
                        break;
                    }
                }
            }
        });
    }


    private void mostrarMenuOpciones(String nodoClickado, MouseEvent e) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem itemAñadir = new JMenuItem("Aumentar Hijo");
        itemAñadir.addActionListener(ev -> {
            String nuevoHijo = JOptionPane.showInputDialog(vista, "Nombre del nuevo nodo hijo:");
            if (nuevoHijo != null && !nuevoHijo.trim().isEmpty()) {
                modelo.agregarNodoGraph(nodoClickado, nuevoHijo.trim());
            }
        });

        JMenuItem itemEliminar = new JMenuItem("Eliminar Nodo");
        itemEliminar.addActionListener(ev -> {
            int confirm = JOptionPane.showConfirmDialog(vista,
                    "¿Eliminar nodo " + nodoClickado + " y todos sus subnodos?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                modelo.eliminarNodoGraph(nodoClickado);
            }
        });

        menu.add(itemAñadir);
        menu.add(itemEliminar);
        menu.show(vista.getPanelDerecho(), e.getX(), e.getY());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("arbol".equals(evt.getPropertyName())) {
            logger.info("Actualizando vista gráfica del árbol");
            Arbol<String> nuevoArbol = (Arbol<String>) evt.getNewValue();
            vista.getPanelDerecho().setArbol(nuevoArbol);
        }
        else if ("json".equals(evt.getPropertyName())) {
            logger.info("Actualizando panel de texto JSON");
            vista.getPanelIzquierdo().setJsonTexto((String) evt.getNewValue());
        }
    }
}
