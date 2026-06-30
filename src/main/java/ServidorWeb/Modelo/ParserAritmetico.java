package ServidorWeb.Modelo;

import Utilidades.ArbolAritmetico.ArbolAritmetico;
import Utilidades.ArbolAritmetico.EnumOperacion;
import Utilidades.ArbolAritmetico.Numero;
import Utilidades.ArbolAritmetico.OperacionAritmetica;
import Utilidades.ArbolAritmetico.Operador;

public class ParserAritmetico {

    // Método público original intacto. No afecta a las otras clases.
    public static ArbolAritmetico construirDesdeString(String expresion) {
        // Se instancia un objeto parseador interno por cada petición.
        // Esto garantiza que el servidor web sea Thread-Safe (seguro en hilos).
        return new ParseadorRecursivo(expresion).parsear();
    }

    /**
     * Clase interna privada que implementa el Descenso Recursivo.
     * Reemplaza las pilas explícitas utilizando la pila de llamadas nativa de Java.
     */
    private static class ParseadorRecursivo {
        private final String expresion;
        private int posicion = -1;
        private int caracterActual;
        private final ArbolAritmetico arbol;

        public ParseadorRecursivo(String expresion) {
            this.expresion = expresion;
            this.arbol = new ArbolAritmetico();
        }

        // --- NÚCLEO DEL PARSEADOR ---

        public ArbolAritmetico parsear() {
            avanzarCaracter();
            ArbolAritmetico.Nodo<OperacionAritmetica> raiz = procesarExpresion();

            if (posicion < expresion.length()) {
                throw new IllegalArgumentException("Carácter inesperado en la expresión: " + (char)caracterActual);
            }

            arbol.setRaiz(raiz);
            return arbol;
        }

        // 1. Nivel de Sumas y Restas (Menor precedencia)
        private ArbolAritmetico.Nodo<OperacionAritmetica> procesarExpresion() {
            ArbolAritmetico.Nodo<OperacionAritmetica> nodo = procesarTermino();

            for (;;) {
                if (consumirCaracter('+')) {
                    nodo = crearNodo(EnumOperacion.Suma, nodo, procesarTermino());
                } else if (consumirCaracter('-')) {
                    nodo = crearNodo(EnumOperacion.Resta, nodo, procesarTermino());
                } else {
                    return nodo;
                }
            }
        }

        // 2. Nivel de Multiplicaciones y Divisiones (Mayor precedencia)
        private ArbolAritmetico.Nodo<OperacionAritmetica> procesarTermino() {
            ArbolAritmetico.Nodo<OperacionAritmetica> nodo = procesarFactor();

            for (;;) {
                if (consumirCaracter('*')) {
                    nodo = crearNodo(EnumOperacion.Multiplicacion, nodo, procesarFactor());
                } else if (consumirCaracter('/')) {
                    nodo = crearNodo(EnumOperacion.Division, nodo, procesarFactor());
                } else {
                    return nodo;
                }
            }
        }

        // 3. Nivel Base: Números aislados o Paréntesis (Máxima precedencia)
        private ArbolAritmetico.Nodo<OperacionAritmetica> procesarFactor() {
            if (consumirCaracter('(')) {
                ArbolAritmetico.Nodo<OperacionAritmetica> nodo = procesarExpresion();
                consumirCaracter(')');
                return nodo;
            }

            int posicionInicio = this.posicion;
            if ((caracterActual >= '0' && caracterActual <= '9') || caracterActual == '.') {
                while ((caracterActual >= '0' && caracterActual <= '9') || caracterActual == '.') {
                    avanzarCaracter();
                }

                double valor = Double.parseDouble(expresion.substring(posicionInicio, this.posicion));
                return arbol.new Nodo<>(new Numero(valor));
            }

            throw new IllegalArgumentException("Sintaxis incorrecta cerca de la posición: " + posicionInicio);
        }

        // --- HERRAMIENTAS UTILITARIAS ---

        private void avanzarCaracter() {
            caracterActual = (++posicion < expresion.length()) ? expresion.charAt(posicion) : -1;
        }

        private boolean consumirCaracter(int caracterBuscado) {
            while (caracterActual == ' ') {
                avanzarCaracter(); // Ignora espacios en blanco dinámicamente
            }
            if (caracterActual == caracterBuscado) {
                avanzarCaracter();
                return true;
            }
            return false;
        }

        private ArbolAritmetico.Nodo<OperacionAritmetica> crearNodo(
                EnumOperacion op,
                ArbolAritmetico.Nodo<OperacionAritmetica> izq,
                ArbolAritmetico.Nodo<OperacionAritmetica> der) {

            ArbolAritmetico.Nodo<OperacionAritmetica> nodoOp = arbol.new Nodo<>(new Operador(op));
            nodoOp.insertarHijo(izq); // La clase Nodo mantiene tu estructura original
            nodoOp.insertarHijo(der);
            return nodoOp;
        }
    }
}