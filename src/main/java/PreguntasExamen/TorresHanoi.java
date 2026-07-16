package PreguntasExamen;
//5.	Se dice que cuando el universo comenzó se inició un Hanoi con 64 discos. Y que, de la misma  manera, cuando el Hanoi sea resuelto,
// el universo terminará. Si el universo comenzó hace 13.700.000.000 años  y los monjes en Hanoi logran mover correctamente un anillo en 1 segundo.
// ¿Cuánto tiempo le queda de vida al universo?La complejidad del algoritmo de Hanoi es 2^n-1. Eso significa que se necesitan 2^64 movimientos para resolver el Hanoi.
public class TorresHanoi {
    /**Datos:
     *  Edad del universo: 13.700.000.000
     * Movimientos para resolver el hanoi: (2^64)-1 = 18.446.744.073.709.551.615
     * Segundos en un año:  31.557.600
     * Resolucion:
     * Tiempo total pa resolver el hanoi es igual a (2^64)-1/Segundos en un año osea = 584.542.046.090 años
     * Tiempo de vida restante en el universo es igual a TiempoTotal-Edad del universo, osea = 570.842.046.090
     */
}
