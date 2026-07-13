package VisorDeImagenes.modelo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Random;

public class CifradoImagen implements Cifrable<int[][]> {
    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public int[][] cifrar(int[][] matriz, String clave) {
        logger.info("Cifrando imagen con clave provista");
        int[] arregloUnidimensional = convertirAUnidimensional(matriz);
        Random generadorAleatorio = new Random(clave.hashCode());

        for (int i = arregloUnidimensional.length - 1; i > 0; i--) {
            int indiceAleatorio = generadorAleatorio.nextInt(i + 1);
            intercambiar(arregloUnidimensional, i, indiceAleatorio);
        }

        return convertirABidimensional(arregloUnidimensional, matriz[0].length, matriz.length);
    }

    @Override
    public int[][] descifrar(int[][] matriz, String clave) {
        logger.info("Descifrando imagen con clave provista");
        int[] arregloUnidimensional = convertirAUnidimensional(matriz);
        Random generadorAleatorio = new Random(clave.hashCode());

        int[] intercambios = new int[arregloUnidimensional.length];
        for (int i = arregloUnidimensional.length - 1; i > 0; i--) {
            intercambios[i] = generadorAleatorio.nextInt(i + 1);
        }

        for (int i = 1; i < arregloUnidimensional.length; i++) {
            intercambiar(arregloUnidimensional, i, intercambios[i]);
        }

        return convertirABidimensional(arregloUnidimensional, matriz[0].length, matriz.length);
    }

    private void intercambiar(int[] arreglo, int i, int j) {
        int temporal = arreglo[i];
        arreglo[i] = arreglo[j];
        arreglo[j] = temporal;
    }

    private int[] convertirAUnidimensional(int[][] matriz) {
        int alto = matriz.length;
        int ancho = matriz[0].length;
        int[] resultado = new int[alto * ancho];
        for (int y = 0; y < alto; y++) {
            System.arraycopy(matriz[y], 0, resultado, y * ancho, ancho);
        }
        return resultado;
    }

    private int[][] convertirABidimensional(int[] arreglo, int ancho, int alto) {
        int[][] resultado = new int[alto][ancho];
        for (int y = 0; y < alto; y++) {
            System.arraycopy(arreglo, y * ancho, resultado[y], 0, ancho);
        }
        return resultado;
    }
}