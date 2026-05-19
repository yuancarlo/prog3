package RedDePizarras.vista;

import Utilidades.ListaEnlazadaDoble;
import javax.swing.*;

import RedDePizarras.modelo.Figura;
import RedDePizarras.modelo.ModeloPizarra;

import java.awt.Color;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PanelPizarra extends JPanel implements PropertyChangeListener {
    private ModeloPizarra modelo;

    public PanelPizarra(ModeloPizarra modelo) {
        this.modelo = modelo;
        this.modelo.addObserver(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        ListaEnlazadaDoble<Figura> propias = modelo.getListaPropia();
        ListaEnlazadaDoble<Figura> remotas = modelo.getListaRemota();

        for (int i = 0; i < propias.getTamano(); i++) {
            Figura f = propias.getValorPorIndice(i); 
            if (f != null) {
                f.setColor(Color.BLUE); 
                f.dibujar(g);           
            }
        }


        for (int i = 0; i < remotas.getTamano(); i++) {
            Figura f = remotas.getValorPorIndice(i);
            if (f != null) {
                f.setColor(Color.RED);  
                f.dibujar(g);           
            }
        }
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
            repaint();
    }


}
