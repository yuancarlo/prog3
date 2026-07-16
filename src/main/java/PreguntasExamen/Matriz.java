package PreguntasExamen;
//8.	Haga un método que tome como argumento una matriz m1 y una matriz m2.
// El método revisa que las matrices tengan las dimensiones adecuadas para hacer la multiplicación m1 x m2 y por supuesto,
//  hace la multiplicación de matrices y devuelve la matriz resultante.
public class Matriz {
}
class OperacionesMatrices {

    public static int[][] multiplicar(int[][] m1, int[][] m2) {
        // 1. Obtenemos las dimensiones de ambas matrices
        int filasM1 = m1.length;
        int columnasM1 = m1[0].length;

        int filasM2 = m2.length;
        int columnasM2 = m2[0].length;

        // 2. Validación crucial: ¿Son compatibles?
        if (columnasM1 != filasM2) {
            // Lanzamos un error si no cumplen la regla matemática
            throw new IllegalArgumentException(
                    "Error: El número de columnas de m1 (" + columnasM1 +
                            ") debe ser igual al número de filas de m2 (" + filasM2 + ")."
            );
        }

        // 3. Creamos la matriz resultante con las dimensiones correctas (Filas m1 x Columnas m2)
        int[][] resultado = new int[filasM1][columnasM2];

        // 4. Algoritmo de multiplicación (Producto punto fila x columna)
        for (int i = 0; i < filasM1; i++) {           // Recorre las filas de m1
            for (int j = 0; j < columnasM2; j++) {    // Recorre las columnas de m2
                for (int k = 0; k < columnasM1; k++) { // Recorre los elementos a multiplicar
                    // Acumulamos la suma de las multiplicaciones
                    resultado[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }

        return resultado;
    }
}
