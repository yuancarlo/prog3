package ServidorWeb.Modelo;

import Utilidades.ArbolAritmetico.*;

import java.util.Stack;

public class ParserAritmetico {

    // Método pragmático para construir el árbol usando dos pilas (Shunting Yard simplificado)
    public static ArbolAritmetico construirDesdeString(String expresion) {
        ArbolAritmetico arbol = new ArbolAritmetico();
        Stack<ArbolAritmetico.Nodo<OperacionAritmetica>> nodos = new Stack<>();
        Stack<Character> operadores = new Stack<>();

        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            if (c == ' ') continue;

            if (Character.isDigit(c) || c == '.') {
                StringBuilder num = new StringBuilder();
                while (i < expresion.length() && (Character.isDigit(expresion.charAt(i)) || expresion.charAt(i) == '.')) {
                    num.append(expresion.charAt(i++));
                }
                i--;
                Numero numero = new Numero(Double.parseDouble(num.toString()));
                nodos.push(arbol.new Nodo<>(numero));
            } else if (c == '(') {
                operadores.push(c);
            } else if (c == ')') {
                while (operadores.peek() != '(') {
                    nodos.push(crearNodoOperador(arbol, operadores.pop(), nodos.pop(), nodos.pop()));
                }
                operadores.pop();
            } else if (esOperador(c)) {
                while (!operadores.isEmpty() && precedencia(c) <= precedencia(operadores.peek())) {
                    nodos.push(crearNodoOperador(arbol, operadores.pop(), nodos.pop(), nodos.pop()));
                }
                operadores.push(c);
            }
        }

        while (!operadores.isEmpty()) {
            nodos.push(crearNodoOperador(arbol, operadores.pop(), nodos.pop(), nodos.pop()));
        }

        arbol.setRaiz(nodos.pop());
        return arbol;
    }

    private static ArbolAritmetico.Nodo<OperacionAritmetica> crearNodoOperador(ArbolAritmetico arbol, char op, ArbolAritmetico.Nodo<OperacionAritmetica> der, ArbolAritmetico.Nodo<OperacionAritmetica> izq) {
        EnumOperacion enumOp = switch (op) {
            case '+' -> EnumOperacion.Suma;
            case '-' -> EnumOperacion.Resta;
            case '*' -> EnumOperacion.Multiplicacion;
            case '/' -> EnumOperacion.Division;
            default -> throw new IllegalArgumentException("Operador no soportado");
        };
        Operador operador = new Operador(enumOp);
        ArbolAritmetico.Nodo<OperacionAritmetica> nodoOp = arbol.new Nodo<>(operador);
        nodoOp.insertarHijo(izq);
        nodoOp.insertarHijo(der);
        return nodoOp;
    }

    private static boolean esOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static int precedencia(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return -1;
    }
}