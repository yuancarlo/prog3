package ServidorWeb.Modelo;

import Utilidades.IDibujable;
import Utilidades.Arbol;
import Utilidades.ArbolAritmetico.ArbolAritmetico;
import Utilidades.ArbolAritmetico.OperacionAritmetica;
import java.awt.*;

public class DibujoArbolAritmetico implements IDibujable {

    // 1. CONSTANTES (Eliminación de "Magic Numbers")
    private static final int LIENZO_ANCHO = 800;
    private static final int LIENZO_ALTO = 600;
    private static final int TAMANO_NODO = 50;
    private static final int ESPACIO_HORIZONTAL = 30;
    private static final int ESPACIO_VERTICAL_IDEAL = 55;
    private static final int ALTURA_MAXIMA_PERMITIDA = 400;
    private static final int MIN_ESPACIO_VERTICAL = 25;

    private final ArbolAritmetico modelo;

    public DibujoArbolAritmetico(ArbolAritmetico modelo) {
        this.modelo = modelo;
    }

    // 2. MÉTODO PRINCIPAL DE DIBUJO (Limpiado y orquestado)
    @Override
    public void dibujar(Graphics g, int x, int y) {
        dibujarFondo(g);

        if (modelo.getRaiz() != null) {
            int profundidad = calcularProfundidad(modelo.getRaiz());
            int espacioVertical = calcularEspacioVerticalDinamico(profundidad);
            int xInicial = calcularCentradoHorizontal();

            // Pinta el árbol desde una posición Y inicial (y + 25 como padding superior)
            dibujarSubArbol(g, modelo.getRaiz(), xInicial, y + 25, espacioVertical);
        }

        dibujarResultado(g);
    }

    // 3. RESPONSABILIDADES SEPARADAS (Métodos pequeños y cohesivos)

    private void dibujarFondo(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, LIENZO_ANCHO, LIENZO_ALTO);
    }

    private void dibujarResultado(Graphics g) {
        double resultado = modelo.evaluar().getValor();
        g.setColor(Color.BLACK);
        g.setFont(new Font("Consolas", Font.BOLD, 22));
        g.drawString("Resultado: " + resultado, 530, 550);
    }

    private int calcularEspacioVerticalDinamico(int profundidad) {
        if (profundidad <= 1) return ESPACIO_VERTICAL_IDEAL;

        int alturaArbolNatural = (profundidad * TAMANO_NODO) + ((profundidad - 1) * ESPACIO_VERTICAL_IDEAL);

        if (alturaArbolNatural > ALTURA_MAXIMA_PERMITIDA) {
            int espacioDisponible = ALTURA_MAXIMA_PERMITIDA - (profundidad * TAMANO_NODO);
            int espacioCalculado = espacioDisponible / (profundidad - 1);
            return Math.max(espacioCalculado, MIN_ESPACIO_VERTICAL);
        }

        return ESPACIO_VERTICAL_IDEAL;
    }

    private int calcularCentradoHorizontal() {
        int anchoTotal = calcularAncho(modelo.getRaiz());
        int xCentrado = (LIENZO_ANCHO - anchoTotal) / 2;
        return Math.max(xCentrado, 20); // Retorna centrado, garantizando un margen mínimo de 20
    }

    // 4. LÓGICA DE DIBUJO ESTRUCTURAL (Refactorizada a Iterativa N-aria)

    private void dibujarSubArbol(Graphics g, Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> nodo, int x, int y, int espacioVertical) {
        if (nodo.getHijos().getTamano() == 0) {
            dibujarNodoSuelto(g, nodo.getContenido().toString(), x, y);
            return;
        }

        int anchoTotalNodo = calcularAncho(nodo);
        int xCentroNodo = x + (anchoTotalNodo / 2) - (TAMANO_NODO / 2);

        int xHijoActual = x;
        int yHijos = y + TAMANO_NODO + espacioVertical;

        g.setColor(Color.BLACK);

        // ITERADOR INSPIRADO EN TU CLASE DE REFERENCIA:
        // En lugar de hacer if(izq) y if(der) a mano, iteramos dinámicamente.
        // Esto reduce el código a la mitad y lo hace mucho más tolerante a fallos.
        for (Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> hijo : nodo.getHijos()) {
            int anchoHijo = calcularAncho(hijo);
            int xCentroHijo = xHijoActual + (anchoHijo / 2);

            // Dibujar línea conectora entre el padre y este hijo
            g.drawLine(xCentroNodo + (TAMANO_NODO / 2), y + (TAMANO_NODO / 2),
                    xCentroHijo, yHijos + (TAMANO_NODO / 2));

            // Llamada recursiva para dibujar el subárbol de este hijo
            dibujarSubArbol(g, hijo, xHijoActual, yHijos, espacioVertical);

            // Desplazar el puntero X para el hermano derecho
            xHijoActual += anchoHijo + ESPACIO_HORIZONTAL;
        }

        // Dibujar el círculo del nodo padre ENCIMA de las líneas
        dibujarNodoSuelto(g, nodo.getContenido().toString(), xCentroNodo, y);
    }

    private void dibujarNodoSuelto(Graphics g, String contenido, int x, int y) {
        g.setColor(Color.WHITE);
        g.fillArc(x, y, TAMANO_NODO, TAMANO_NODO, 0, 360);

        g.setColor(Color.BLACK);
        g.drawArc(x, y, TAMANO_NODO, TAMANO_NODO, 0, 360);

        // Mantenemos tu lógica original de centrado dinámico (¡Es muy buena práctica!)
        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();
        int anchoTexto = fm.stringWidth(contenido);
        int altoTexto = fm.getAscent();

        int xCentro = x + (TAMANO_NODO - anchoTexto) / 2;
        int yCentro = y + (TAMANO_NODO + altoTexto) / 2 - 3;

        g.drawString(contenido, xCentro, yCentro);
    }

    // 5. MÉTODOS DE CÁLCULO (Adaptados para usar el Iterador)

    private int calcularAncho(Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> nodo) {
        if (nodo == null) return 0;
        if (nodo.getHijos().getTamano() == 0) return TAMANO_NODO;

        int anchoAcumulado = 0;
        int separacionHorizontal = 0;

        for (Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> hijo : nodo.getHijos()) {
            anchoAcumulado += separacionHorizontal + calcularAncho(hijo);
            separacionHorizontal = ESPACIO_HORIZONTAL;
        }

        return anchoAcumulado;
    }

    private int calcularProfundidad(Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> nodo) {
        if (nodo == null) return 0;
        if (nodo.getHijos().getTamano() == 0) return 1;

        int profundidadMaximaDeHijos = 0;

        for (Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> hijo : nodo.getHijos()) {
            int profundidadHijo = calcularProfundidad(hijo);
            if (profundidadHijo > profundidadMaximaDeHijos) {
                profundidadMaximaDeHijos = profundidadHijo;
            }
        }

        return 1 + profundidadMaximaDeHijos;
    }
}