package PreguntasExamen;
//16.	Cómo se puede realizar el algoritmo para agrandar una imagen?
//La imagen es una clase como se vio en el curso. Y tiene una matriz de puntos
//Int[][] pixeles
//Cómo sería el código de los siguientes métodos?
//Void agrandar(int factor)
//Void agrandar(int nuevoAncho, int nuevoAlto)
public class Imagen {
    private int ancho;
    private int alto;
    private int[][] pixeles;
    public Imagen(int ancho, int alto) {
        this.ancho = ancho;
        this.alto = alto;
    }
    // 1. Método para agrandar a un ancho y alto específicos
    public void agrandar(int nuevoAncho, int nuevoAlto) {
        // Creamos la nueva matriz con las dimensiones solicitadas
        int[][] nuevosPixeles = new int[nuevoAncho][nuevoAlto];

        // Recorremos cada píxel de la NUEVA matriz
        for (int x = 0; x < nuevoAncho; x++) {
            for (int y = 0; y < nuevoAlto; y++) {

                // Calculamos a qué coordenada de la matriz original corresponde este nuevo píxel
                int origX = (x * this.ancho) / nuevoAncho;
                int origY = (y * this.alto) / nuevoAlto;

                // Copiamos el color original en la nueva posición
                nuevosPixeles[x][y] = this.pixeles[origX][origY];
            }
        }

        // Actualizamos los atributos de la clase para que reflejen la nueva imagen
        this.ancho = nuevoAncho;
        this.alto = nuevoAlto;
        this.pixeles = nuevosPixeles;


    }


    // 2. Método para agrandar multiplicando por un factor (ej: factor 2 = doble de tamaño)
    public void agrandar(int factor) {
        // Reutilizamos el método anterior para no duplicar código
        int nuevoAncho = this.ancho * factor;
        int nuevoAlto = this.alto * factor;

        agrandar(nuevoAncho, nuevoAlto);
    }

    public int getAncho() {
        return this.ancho;
    }

    public int getAlto() {
        return  this.alto;
    }

    public int[] getRgb(int x, int y) {
        int px = pixeles[x][y];
        // Obtenemos los ultimos 8 bits
        int b = px & 0x000000FF;
        // Obtenemos los 8 bits de G y los movemos a la derecha
        int g = px & 0x0000FF00 >> 8;

        int r = px & 0x00FF0000 >> 16;

        int[] rgb = new int[3];
        rgb[0] = r;
        rgb[1] = g;
        rgb[2] = b;

        return rgb;
    }
}
