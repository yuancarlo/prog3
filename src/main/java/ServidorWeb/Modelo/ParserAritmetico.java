package ServidorWeb.Modelo;

import Utilidades.ArbolAritmetico.ArbolAritmetico;
import Utilidades.ArbolAritmetico.EnumOperacion;
import Utilidades.ArbolAritmetico.Numero;
import Utilidades.ArbolAritmetico.OperacionAritmetica;
import Utilidades.ArbolAritmetico.Operador;

public class ParserAritmetico {


    public static ArbolAritmetico construirDesdeString(String expresion) {
        return new ParseadorRecursivo(expresion).parsear();
    }


    private static class ParseadorRecursivo {
        private final String expresion;
        private int posicion = -1;
        private int caracterActual;
        private final ArbolAritmetico arbol;

        public ParseadorRecursivo(String expresion) {
            this.expresion = expresion;
            this.arbol = new ArbolAritmetico();
        }

        public ArbolAritmetico parsear() {
            avanzarCaracter();
            ArbolAritmetico.Nodo<OperacionAritmetica> raiz = procesarExpresion();

            if (posicion < expresion.length()) {
                throw new IllegalArgumentException("Carácter inesperado en la expresión: " + (char)caracterActual);
            }

            arbol.setRaiz(raiz);
            return arbol;
        }

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


        private void avanzarCaracter() {
            caracterActual = (++posicion < expresion.length()) ? expresion.charAt(posicion) : -1;
        }

        private boolean consumirCaracter(int caracterBuscado) {
            while (caracterActual == ' ') {
                avanzarCaracter(); 
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
            nodoOp.insertarHijo(izq); 
            nodoOp.insertarHijo(der);
            return nodoOp;
        }
    }
}