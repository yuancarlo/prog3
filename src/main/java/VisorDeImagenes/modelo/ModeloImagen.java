package VisorDeImagenes.modelo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.awt.image.BufferedImage;

public class ModeloImagen {
    private  static Logger logger = LogManager.getRootLogger();
    private int[][] pixeles;
    private int alto;
    private int ancho;

    public  ModeloImagen(int alto, int ancho){
        this.alto = alto;
        this.ancho = ancho;
        this.pixeles = new int[alto][ancho];
        logger.debug("Creado nuevo ModeloImagen vacío de {}x{}", ancho, alto);
    }

    public ModeloImagen(BufferedImage bi){
        this.alto = bi.getHeight();
        this.ancho = bi.getWidth();
        this.pixeles = new int[alto][ancho];
        logger.info("Construyendo ModeloImagen desde BufferedImage ({}x{})", ancho, alto);
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                this.pixeles[y][x] = bi.getRGB(x, y);
            }
        }
    }

    public ModeloImagen(int[][] pixeles, int alto, int ancho) {
        this.alto = alto;
        this.ancho = ancho;
        this.pixeles = pixeles;
        logger.debug("Construido ModeloImagen mediante matriz directa ({}x{})", ancho, alto);

    }


    public int getAlto() {
        return alto;
    }

    public int getAncho() {
        return ancho;
    }

    public int[][] getPixeles() {
        return pixeles;
    }

    public boolean validarCoordenada(int x, int y){
        return  x >= 0 && x < ancho && y >= 0 && y < alto;
    }

    public ModeloImagen achicar(int nuevoAncho, int nuevoAlto) {
        logger.info("Iniciando algoritmo 'Achicar' -> Destino: {}x{}", nuevoAncho, nuevoAlto);
        int[][] nuevosPixeles = new int[nuevoAlto][nuevoAncho];
        double factorX = (double) this.ancho / nuevoAncho;
        double factorY = (double) this.alto / nuevoAlto;
        logger.debug("Factores de escala calculados - FactorX: {}, FactorY: {}", factorX, factorY);

        for (int y = 0; y < nuevoAlto; y++) {
            for (int x = 0; x < nuevoAncho; x++) {
                int viejaY = (int) (y * factorY);
                int viejaX = (int) (x * factorX);
                nuevosPixeles[y][x] = this.pixeles[viejaY][viejaX];
            }
        }
        logger.info("Finalizado algoritmo 'Achicar' con éxito.");
        return new ModeloImagen( nuevosPixeles, nuevoAlto, nuevoAncho);
    }

    public ModeloImagen agrandar(int nuevoAncho, int nuevoAlto) {
        logger.info("Iniciando algoritmo 'Agrandar' (Interpolación Bilineal) -> Destino: {}x{}", nuevoAncho, nuevoAlto);

        ModeloImagen nuevaImg = new ModeloImagen(nuevoAlto, nuevoAncho);

        double ratioX = (double) nuevoAncho / this.ancho;
        double ratioY = (double) nuevoAlto / this.alto;
        logger.debug("Ratios calculados - RatioX: {}, RatioY: {}", ratioX, ratioY);

        for (int y = 0; y < this.alto; y++) {
            for (int x = 0; x < this.ancho; x++) {

                int[] supIzq = this.getArgb(x, y);
                int[] supDer = (x + 1 < this.ancho) ? this.getArgb(x + 1, y) : supIzq;
                int[] infIzq = (y + 1 < this.alto)  ? this.getArgb(x, y + 1) : supIzq;
                int[] infDer = (x + 1 < this.ancho && y + 1 < this.alto) ? this.getArgb(x + 1, y + 1) : supIzq;

                for (int j = 0; j < ratioY; j++) {
                    int ny = (int) (y * ratioY) + j;
                    if (ny >= nuevoAlto) break;

                    double fY = j / ratioY;

                    int aIncio = (int) (supIzq[0] + (infIzq[0] - supIzq[0]) * fY);
                    int rIncio = (int) (supIzq[1] + (infIzq[1] - supIzq[1]) * fY);
                    int gIncio = (int) (supIzq[2] + (infIzq[2] - supIzq[2]) * fY);
                    int bIncio = (int) (supIzq[3] + (infIzq[3] - supIzq[3]) * fY);

                    int aFin = (int) (supDer[0] + (infDer[0] - supDer[0]) * fY);
                    int rFin = (int) (supDer[1] + (infDer[1] - supDer[1]) * fY);
                    int gFin = (int) (supDer[2] + (infDer[2] - supDer[2]) * fY);
                    int bFin = (int) (supDer[3] + (infDer[3] - supDer[3]) * fY);

                    double incA = (aFin - aIncio) / ratioX;
                    double incR = (rFin - rIncio) / ratioX;
                    double incG = (gFin - gIncio) / ratioX;
                    double incB = (bFin - bIncio) / ratioX;

                    for (int i = 0; i < ratioX; i++) {
                        int nx = (int) (x * ratioX) + i;
                        if (nx >= nuevoAncho) break;

                        int resA = (int) (aIncio + (incA * i));
                        int resR = (int) (rIncio + (incR * i));
                        int resG = (int) (gIncio + (incG * i));
                        int resB = (int) (bIncio + (incB * i));

                        nuevaImg.setArgb(nx, ny, resA, resR, resG, resB);
                    }
                }
            }
        }
        logger.info("Finalizado algoritmo 'Agrandar' con éxito.");
        return nuevaImg;
    }

    public ModeloImagen redimensionar(int nuevoAncho, int nuevoAlto) {
        logger.info("Solicitud de redimensión recibida: {}x{}", nuevoAncho, nuevoAlto);
        int anchoFinal = nuevoAncho;
        int altoFinal = nuevoAlto;

        if (nuevoAncho <= 0 || nuevoAlto <= 0) {
            logger.error("Dimensiones inválidas solicitadas: {}x{}. Abortando.", nuevoAncho, nuevoAlto);
            return this;
        }


        if (anchoFinal > this.ancho || altoFinal > this.alto) {
            return this.agrandar(anchoFinal, altoFinal);
        }

        else {
            return this.achicar(anchoFinal, altoFinal);
        }
    }


    public int obtenerAltoProporcional(int nuevoAncho) {
        int altoProp = (int) Math.round((double) this.alto * nuevoAncho / this.ancho);
        logger.debug("Calculado Alto Proporcional: {} para Ancho: {}", altoProp, nuevoAncho);
        return altoProp;
    }


    public int obtenerAnchoProporcional(int nuevoAlto) {
        int anchoProp = (int) Math.round((double) this.ancho * nuevoAlto / this.alto);
        logger.debug("Calculado Ancho Proporcional: {} para Alto: {}", anchoProp, nuevoAlto);
        return anchoProp;
    }

    public int[] getArgb(int x, int y) {
        if (!validarCoordenada(x, y)) {
            logger.warn("Intento de acceso a coordenada fuera de rango: ({}, {})", x, y);
            return new int[] {0, 0, 0, 0};
        }
        int px = pixeles[y][x];

        int a = (px & 0xFF000000) >>> 24;
        int r = (px & 0x00FF0000) >> 16;
        int g = (px & 0x0000FF00) >> 8;
        int b = px & 0x000000FF;

        int[] argb = new int[4];
        argb[0] = a;
        argb[1] = r;
        argb[2] = g;
        argb[3] = b;

        return argb;
    }

    public void setArgb(int x, int y, int a, int r, int g, int b) {
        if (!validarCoordenada(x, y)) {
            return;
        }
        int c = (a << 24);
        c = c | (r << 16);
        c = c | (g << 8);
        c = c | b;

        pixeles[y][x] = c;
    }

}