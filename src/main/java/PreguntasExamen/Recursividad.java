package PreguntasExamen;
//2.	Explique el concepto de recurrencia en programación. A modo de práctica resuelva los siguientes ejercicios con una función recurrente
// (utilice programación defensiva para los parámetros):
//a.	La suma de los primeros n números impares
//b.	Si n=1, imprimir ‘ab’, si n=2, imprimir ‘aabb’ y asi sucesivamente. Escribir la función que toma como argumento un entero n.
public class Recursividad {
    /** La recurrencia es la accion de volver a repetirse, ocurre cuando una funcion se llama a si misma para poder
     resolver una version mas pequeña del mismo problema, todas las funciones que apliquen recurrencia deben tener estos dos elementos en su estructura,
     para evitar errores de desbordamiento de pila:
     -caso base: es la condicion que detiene la recurrencia(el escenario mas simple de un problema, q se puede resolver sin lamar a la funcion)
     -caso recursivo: es el paso donde la funcion se llama a si misma,modificando sus parametros hasta llegar al caso base**/

    /**Suma de los primeros n numeros impares */
    public static int sumaNumerosImpares(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        if (n == 1) {
            return 1;
        }


        int nsimoNumeroImpar = (2 * n)-1;
        return nsimoNumeroImpar + sumaNumerosImpares(n-1);

    }

    public static String impresionSucesiva(int n){
        if (n  <= 0) {
            throw new IllegalArgumentException();
        }
        if (n == 1) {
            return "ab";
        }
        return "a" + impresionSucesiva(n-1) +"b";
    }
    public static void main(String[] args) {
        Recursividad recursividad = new Recursividad();
        System.out.println(recursividad.sumaNumerosImpares(2));
        System.out.println(recursividad.impresionSucesiva(5));

    }
}
