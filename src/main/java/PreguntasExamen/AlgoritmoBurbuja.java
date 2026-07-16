package PreguntasExamen;
//3.	Explique cuál es la complejidad de un algoritmo de ordenamiento como el algoritmo de burbuja.
// Fundamente su respuesta para llegar a la expresión  matemática de la complejidad de este tipo de algoritmos
public class AlgoritmoBurbuja {
    /** La complejidad de bubble sort es de O(n^2),el algoritmo funciona revisando repetidamente el array q va ordenar, compara elementos adyacentes de par en par
     * y los intercambia si estan en el orden equivocado,este proceso se repite hasta q no se necesiten mas intercambios(es decir la lista este ordenada).
     * Fundamentacion matematica
     * cantidad de comparaciones del algoritmo en un array de 10 elementos:
     * 1 iteracion: 9 pasos(n-1)
     * 2 iteracion: 8 pasos (n-2)
     * 3 iteracion: 7 pasos (n-3)
     * asi sucesivamente, hasta que solo queden dos elementos por comparar(1 paso)
     * Progresion aritmetica: (n-1) + (n-2) + ...+ 2 +1; esta progresion representa la suma de los primeros (n-1) numeros naturales: ((n-1)n)/2
     * Esta formula e sigual a esto:(n^2-n)/2, lo q es igual a ((1/2)n^2) - (1/2)n
     * Como la notacion Big O busca responder ¿q tan rapìdo se degrada el rendimiento de un algoritmo cuando la cantidad de datos crece hacia el infinito?,
     * eliminamos el termino de menor orden q seria "n" y tambien eliminamos las constantes, lo q nos daria una notacion de O(n^2)
     *
     */

    public static void bubblesort(int[] array){
        int n =  array.length;

        for(int i=0; i < n-1; i++){
            for(int j=0; j < n-1; j++){
                if (array[j] > array[j+1]){
                    int aux = array[j];
                    array[j] = array[j+1];
                    array[j+1] = aux;
                }
            }
        }
    }
    public static void main(String[] args) {
        int[] array = {4,6,2,8,1,9,5,3,7};
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
        bubblesort(array);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }

    }
}
