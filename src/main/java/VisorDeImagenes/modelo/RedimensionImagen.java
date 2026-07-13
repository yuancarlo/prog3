package VisorDeImagenes.modelo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RedimensionImagen implements Redimensionable<int[][]> {
    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public int[][] achicar(int[][] original, int nuevoAncho, int nuevoAlto) {
        logger.info("Achicando imagen a " + nuevoAncho + "x" + nuevoAlto);
        int altoOriginal = original.length;
        int anchoOriginal = original[0].length;
        int[][] resultado = new int[nuevoAlto][nuevoAncho];

        double relacionY = (double) altoOriginal / nuevoAlto;
        double relacionX = (double) anchoOriginal / nuevoAncho;

        for (int y = 0; y < nuevoAlto; y++) {
            for (int x = 0; x < nuevoAncho; x++) {
                int originalY = (int) (y * relacionY);
                int originalX = (int) (x * relacionX);
                resultado[y][x] = original[originalY][originalX];
            }
        }
        return resultado;
    }

    @Override
    public int[][] agrandar(int[][] original, int nuevoAncho, int nuevoAlto) {
        logger.info("Agrandando imagen mediante aproximación lineal a " + nuevoAncho + "x" + nuevoAlto);
        int altoOriginal = original.length;
        int anchoOriginal = original[0].length;
        int[][] resultado = new int[nuevoAlto][nuevoAncho];

        double relacionY = (double) (altoOriginal - 1) / (nuevoAlto - 1);
        double relacionX = (double) (anchoOriginal - 1) / (nuevoAncho - 1);

        for (int y = 0; y < nuevoAlto; y++) {
            for (int x = 0; x < nuevoAncho; x++) {
                double originalY = y * relacionY;
                double originalX = x * relacionX;

                int y1 = (int) originalY;
                int x1 = (int) originalX;
                int y2 = Math.min(y1 + 1, altoOriginal - 1);
                int x2 = Math.min(x1 + 1, anchoOriginal - 1);

                double difY = originalY - y1;
                double difX = originalX - x1;

                resultado[y][x] = interpolarBilineal(
                        original[y1][x1], original[y1][x2],
                        original[y2][x1], original[y2][x2],
                        difX, difY
                );
            }
        }
        return resultado;
    }

    private int interpolarBilineal(int p1, int p2, int p3, int p4, double dx, double dy) {
        int a = interpolarCanal((p1 >> 24) & 0xff, (p2 >> 24) & 0xff, (p3 >> 24) & 0xff, (p4 >> 24) & 0xff, dx, dy);
        int r = interpolarCanal((p1 >> 16) & 0xff, (p2 >> 16) & 0xff, (p3 >> 16) & 0xff, (p4 >> 16) & 0xff, dx, dy);
        int g = interpolarCanal((p1 >> 8) & 0xff,  (p2 >> 8) & 0xff,  (p3 >> 8) & 0xff,  (p4 >> 8) & 0xff,  dx, dy);
        int b = interpolarCanal(p1 & 0xff,         p2 & 0xff,         p3 & 0xff,         p4 & 0xff,         dx, dy);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private int interpolarCanal(int c1, int c2, int c3, int c4, double dx, double dy) {
        double superior = c1 * (1 - dx) + c2 * dx;
        double inferior = c3 * (1 - dx) + c4 * dx;
        return (int) (superior * (1 - dy) + inferior * dy);
    }
}