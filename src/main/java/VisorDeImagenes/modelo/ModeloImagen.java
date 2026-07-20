package VisorDeImagenes.modelo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ModeloImagen {
    private static final Logger logger = LogManager.getRootLogger();
    private int[][] matrizOriginal;
    private int[][] matrizActual;

    private final Redimensionable<int[][]> redimensionador;
    private final Cifrable<int[][]> cifrador;
    private final PropertyChangeSupport notificador;

    public ModeloImagen() {
        this.redimensionador = new RedimensionImagen();
        this.cifrador = new CifradoImagen();
        this.notificador = new PropertyChangeSupport(this);
    }

    public void agregarObservador(PropertyChangeListener observador) {
        notificador.addPropertyChangeListener(observador);
    }

    public void cargarImagenBase(BufferedImage imagen) {
        logger.info("Cargando nueva imagen base al modelo");
        this.matrizOriginal = convertirABidimensional(imagen);
        this.matrizActual = copiarMatriz(this.matrizOriginal);
        notificarCambio();
    }

    public void redimensionar(int nuevoAncho, int nuevoAlto, boolean mantenerProporcion) {
        if (matrizOriginal == null) return;

        int altoOriginal = matrizOriginal.length;
        int anchoOriginal = matrizOriginal[0].length;

        if (mantenerProporcion) {
            double proporcion = (double) anchoOriginal / altoOriginal;
            if (nuevoAncho != getAnchoActual()) {
                nuevoAlto = (int) (nuevoAncho / proporcion);
            } else if (nuevoAlto != getAltoActual()) {
                nuevoAncho = (int) (nuevoAlto * proporcion);
            }
        }

        if (nuevoAncho > anchoOriginal || nuevoAlto > altoOriginal) {
            matrizActual = redimensionador.agrandar(matrizOriginal, nuevoAncho, nuevoAlto);
        } else {
            matrizActual = redimensionador.achicar(matrizOriginal, nuevoAncho, nuevoAlto);
        }
        notificarCambio();
    }

    public void cifrarImagen(String clave) {
        if (matrizActual != null) {
            matrizActual = cifrador.cifrar(matrizActual, clave);
            notificarCambio();
        }
    }

    public void descifrarImagen(String clave) {
        if (matrizActual != null) {
            matrizActual = cifrador.descifrar(matrizActual, clave);
            notificarCambio();
        }
    }

    public void restaurarImagenOriginal() {
        if (matrizOriginal != null) {
            logger.info("Restaurando la imagen a su estado original");
            // Sobreescribimos la matriz actual con una copia limpia de la original
            this.matrizActual = copiarMatriz(this.matrizOriginal);
            notificarCambio();
        } else {
            logger.warn("Se intentó restaurar la imagen, pero no hay ninguna imagen cargada.");
        }
    }

    public BufferedImage obtenerImagenActual() {
        if (matrizActual == null) return null;
        int alto = matrizActual.length;
        int ancho = matrizActual[0].length;
        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                imagen.setRGB(x, y, matrizActual[y][x]);
            }
        }
        return imagen;
    }

    private void notificarCambio() {
        notificador.firePropertyChange("imagen", null, obtenerImagenActual());
    }

    private int[][] convertirABidimensional(BufferedImage imagen) {
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        int[][] matriz = new int[alto][ancho];
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                matriz[y][x] = imagen.getRGB(x, y);
            }
        }
        return matriz;
    }

    private int[][] copiarMatriz(int[][] origen) {
        int[][] copia = new int[origen.length][origen[0].length];
        for (int i = 0; i < origen.length; i++) {
            System.arraycopy(origen[i], 0, copia[i], 0, origen[i].length);
        }
        return copia;
    }

    public int getAnchoActual() { return matrizActual != null ? matrizActual[0].length : 0; }
    public int getAltoActual() { return matrizActual != null ? matrizActual.length : 0; }
}