package RedDePizarras.vista;

import RedDePizarras.Modelo.Figura;
import RedDePizarras.Modelo.ModeloPizarra;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PanelPizarra extends JPanel implements PropertyChangeListener {
    private ModeloPizarra modelo;

    public PanelPizarra(ModeloPizarra modelo) {
        this.modelo = modelo;
        this.modelo.addObserver(this);
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("FIGURA".equals(evt.getPropertyName())) {
            repaint();
        }

        else if ("NUEVA_FIGURA".equals(evt.getPropertyName())) {

            Figura nueva = (Figura) evt.getNewValue();
            nueva.addObserver(this);
            repaint();
        }

    }


}
