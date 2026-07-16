package PreguntasExamen;

import java.awt.*;

//9.	Proponga el modelo de diagrama de clases para dibujar el histograma de una imagen. Indique el código central donde se calcula el histograma de una imagen.
public class Histograma {
    /**+-------------------------+                 +-------------------------+
     |         Imagen          |                 |       Histograma        |
     +-------------------------+                 +-------------------------+
     | - ancho: int            |                 | - frecuencias: int[]    |
     | - alto: int             | 1             1 | - maxFrecuencia: int    |
     | - pixeles: int[][]      | <-------------- |                         |
     +-------------------------+     analiza     +-------------------------+
     | + getRgb(x, y): int[]   |                 | + calcular(img: Imagen) |
     | + getAncho(): int       |                 | + dibujar(g: Graphics)  |
     | + getAlto(): int        |                 | + getFrecuencias()      |
     +-------------------------+                 +-------------------------+
     */
    // Arreglo de 256 posiciones (de 0 a 255) para contar los píxeles
    private int[] frecuencias;
    private int maxFrecuencia;

    public Histograma() {
        frecuencias = new int[256];
        maxFrecuencia = 0;
    }

    // EL CÓDIGO CENTRAL SOLICITADO
    public void calcular(Imagen img) {
        // 1. Limpiamos las frecuencias por si calculamos una nueva imagen
        frecuencias = new int[256];
        maxFrecuencia = 0;

        int ancho = img.getAncho();
        int alto = img.getAlto();

        // 2. Recorremos toda la matriz de la imagen
        for (int x = 0; x < ancho; x++) {
            for (int y = 0; y < alto; y++) {

                // Usamos el método de tu clase Imagen para sacar el RGB
                int[] rgb = img.getRgb(x, y);

                // 3. Calculamos la intensidad promedio (Escala de grises)
                // r = rgb[0], g = rgb[1], b = rgb[2]
                int intensidad = (rgb[0] + rgb[1] + rgb[2]) / 3;

                // 4. Incrementamos el contador para esa intensidad
                frecuencias[intensidad]++;

                // Opcional: Guardamos cuál es la frecuencia más alta para
                // cuando queramos dibujar el gráfico en pantalla y no salirnos del límite.
                if (frecuencias[intensidad] > maxFrecuencia) {
                    maxFrecuencia = frecuencias[intensidad];
                }
            }
        }
    }

    // Código representativo de cómo se dibujaría usando las frecuencias calculadas
    public void dibujar(Graphics g, int xInicio, int yInicio, int alturaMax) {
        if (maxFrecuencia == 0) return;

        for (int i = 0; i < 256; i++) {
            // Regla de 3 para calcular el alto de la barra actual
            int altoBarra = (frecuencias[i] * alturaMax) / maxFrecuencia;

            // Dibuja una línea vertical para representar la barra del histograma
            g.drawLine(xInicio + i, yInicio, xInicio + i, yInicio - altoBarra);
        }
    }

}
