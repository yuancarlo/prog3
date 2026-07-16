package PreguntasExamen;
//6.	Se puede ver fácilmente que Quicksort se comporta muy mal para arreglos pequeños (de 50 elementos o menos), adicionalmente,
// dependiendo cómo elegimos el pivote, el algoritmo mejora o no. ¿Qué alternativas se tienen para mejorar el algoritmo?
// Proponga una implementación con mejora
public class QuickSort {
    /**Para mejorar el rendimiento de Quicksort se utiliza un enfoque híbrido con dos alternativas clave: primero,
     * para evitar el peor caso $O(N^2)$ provocado por un mal pivote, se usa la técnica de la Mediana de 3
     * (elegir el valor central entre el primer, el medio y el último elemento) lo que garantiza particiones siempre balanceadas;
     * segundo, como la recursividad consume demasiada memoria y tiempo en arreglos pequeños (50 elementos o menos),
     * se frena el Quicksort en ese punto y se delega el ordenamiento a Insertion Sort (Inserción), un algoritmo iterativo
     * que es mucho más rápido y eficiente para procesar pocos datos.*/
    /**El Insertion Sort (Ordenamiento por Inserción) es un algoritmo secuencial muy intuitivo que construye el arreglo ordenado elemento por elemento,
    desplazando los datos hacia la izquierda hasta encontrar su posición correcta.
     La mejor forma de entenderlo y explicarlo en tu examen es con su analogía clásica:
     funciona exactamente igual a cómo ordenas las cartas en tu mano cuando juegas naipes.
     ¿Cómo funciona paso a paso?
     Asumes que el primer elemento (la primera carta) ya está ordenado.
     Tomas el segundo elemento y lo comparas con el primero. Si es menor, lo mueves a la izquierda; si es mayor, lo dejas ahí.
     Tomas el tercer elemento y lo empiezas a comparar hacia atrás (de derecha a izquierda) con los que ya ordenaste. Vas "empujando"
     hacia la derecha a todos los que sean mayores para hacer un hueco, y ahí insertas tu número.
     Repites este proceso hasta llegar al final del arreglo.*/
    // Único método recursivo que contiene toda la lógica
    public static void quicksortMejorado(int[] arr, int izq, int der) {

        // ---------------------------------------------------------
        // MEJORA 1: Insertion Sort (para sub-arreglos pequeños <= 15)
        // ---------------------------------------------------------
        if (der - izq <= 15) {
            for (int i = izq + 1; i <= der; i++) {
                int actual = arr[i];
                int j = i - 1;
                // Movemos los mayores hacia la derecha
                while (j >= izq && arr[j] > actual) {
                    arr[j + 1] = arr[j];
                    j--;
                }
                arr[j + 1] = actual;
            }
            return; // Detiene la recursividad para este fragmento
        }

        // ---------------------------------------------------------
        // MEJORA 2: Selección del Pivote (Mediana de 3)
        // ---------------------------------------------------------
        int medio = izq + (der - izq) / 2;

        // Ordenamos los 3 elementos (izq, medio, der) usando variables temporales
        if (arr[izq] > arr[medio]) {
            int temp = arr[izq]; arr[izq] = arr[medio]; arr[medio] = temp;
        }
        if (arr[izq] > arr[der]) {
            int temp = arr[izq]; arr[izq] = arr[der]; arr[der] = temp;
        }
        if (arr[medio] > arr[der]) {
            int temp = arr[medio]; arr[medio] = arr[der]; arr[der] = temp;
        }

        // La mediana está ahora en arr[medio]. La pasamos al final para usarla de pivote.
        int pivote = arr[medio];
        arr[medio] = arr[der];
        arr[der] = pivote;

        // ---------------------------------------------------------
        // FASE 3: Partición clásica de Quicksort
        // ---------------------------------------------------------
        int i = izq - 1;

        for (int j = izq; j < der; j++) {
            // Si el elemento actual es menor o igual al pivote, lo pasamos a la izquierda
            if (arr[j] <= pivote) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // Colocamos el pivote en su posición final en el centro
        int posPivote = i + 1;
        int tempPiv = arr[posPivote];
        arr[posPivote] = arr[der];
        arr[der] = tempPiv;

        // ---------------------------------------------------------
        // FASE 4: Llamadas Recursivas
        // ---------------------------------------------------------
        // Ordenar la mitad izquierda (menores al pivote)
        quicksortMejorado(arr, izq, posPivote - 1);

        // Ordenar la mitad derecha (mayores al pivote)
        quicksortMejorado(arr, posPivote + 1, der);
    }
}
class QuicksortMejorado {

    // El umbral suele ser entre 10 y 50. Usaremos 15 como estándar general.
    private static final int UMBRAL = 15;

    // Método principal que llama el usuario
    public static void ordenar(int[] arr) {
        if (arr == null || arr.length <= 1) return;
        quicksortHibrido(arr, 0, arr.length - 1);
    }

    private static void quicksortHibrido(int[] arr, int izq, int der) {
        // MEJORA 1: Cortar la recursividad para arreglos pequeños
        if (der - izq <= UMBRAL) {
            insertionSort(arr, izq, der);
            return; // Terminamos aquí, ya está ordenado
        }

        // MEJORA 2: Selección inteligente del pivote (Mediana de 3)
        // Este método ordena el inicio, medio y fin, y deja la mediana al final
        aplicarMedianaDeTres(arr, izq, der);

        // Hacemos la partición normal, sabiendo que el pivote (arr[der]) es óptimo
        int indicePivote = particion(arr, izq, der);

        // Llamadas recursivas para ambas mitades
        quicksortHibrido(arr, izq, indicePivote - 1);
        quicksortHibrido(arr, indicePivote + 1, der);
    }

    // --- MÉTODOS AUXILIARES ---

    private static void aplicarMedianaDeTres(int[] arr, int izq, int der) {
        int medio = izq + (der - izq) / 2;

        // Ordenamos los 3 elementos (izq, medio, der)
        if (arr[izq] > arr[medio]) intercambiar(arr, izq, medio);
        if (arr[izq] > arr[der]) intercambiar(arr, izq, der);
        if (arr[medio] > arr[der]) intercambiar(arr, medio, der);

        // Ahora arr[medio] es la mediana real.
        // La movemos a la posición 'der' para usarla como pivote estándar
        intercambiar(arr, medio, der);
    }

    private static int particion(int[] arr, int izq, int der) {
        int pivote = arr[der];
        int i = izq - 1;

        for (int j = izq; j < der; j++) {
            if (arr[j] <= pivote) {
                i++;
                intercambiar(arr, i, j);
            }
        }
        intercambiar(arr, i + 1, der);
        return i + 1; // Retorna la posición final del pivote
    }

    private static void insertionSort(int[] arr, int izq, int der) {
        for (int i = izq + 1; i <= der; i++) {
            int actual = arr[i];
            int j = i - 1;
            // Desplazamos los elementos mayores hacia la derecha
            while (j >= izq && arr[j] > actual) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = actual;
        }
    }

    private static void intercambiar(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
