package ServidorWeb.Modelo;

import Utilidades.IDibujable;
import Utilidades.Arbol;
import Utilidades.ArbolAritmetico.ArbolAritmetico;
import Utilidades.ArbolAritmetico.OperacionAritmetica;
import java.awt.*;

public class DibujoArbolAritmetico implements IDibujable {
    private static final int TAMANO_NODO = 50;
    private static final int ESPACIO_HORIZONTAL = 30;
    private static final int ESPACIO_VERTICAL_IDEAL = 55; // Separación perfecta y natural para árboles pequeños
    private final ArbolAritmetico modelo;

    public DibujoArbolAritmetico(ArbolAritmetico modelo) {
        this.modelo = modelo;
    }

    @Override
    public void dibujar(Graphics g, int x, int y) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, 600);

        if (modelo.getRaiz() != null) {
            int profundidad = calcularProfundidad(modelo.getRaiz());

            // 1. CÁLCULO VERTICAL INTELIGENTE (Auto-ajustable)
            int espacioVerticalDinamico = ESPACIO_VERTICAL_IDEAL;

            if (profundidad > 1) {
                int alturaMaximaPermitida = 400;
                // Calculamos cuánto mediría el árbol con el espaciado ideal
                int alturaArbolNatural = (profundidad * TAMANO_NODO) + ((profundidad - 1) * ESPACIO_VERTICAL_IDEAL);

                // Solo si el tamaño natural supera el límite permitido, comprimimos las líneas
                if (alturaArbolNatural > alturaMaximaPermitida) {
                    int espacioDisponible = alturaMaximaPermitida - (profundidad * TAMANO_NODO);
                    espacioVerticalDinamico = espacioDisponible / (profundidad - 1);

                    if (espacioVerticalDinamico < 25) {
                        espacioVerticalDinamico = 25; // Límite mínimo de compresión por seguridad
                    }
                }
            }

            // 2. CÁLCULO HORIZONTAL (Centrado perfecto en el lienzo de 800px)
            int anchoTotalArbol = calcularAncho(modelo.getRaiz());
            int xInicialCentrado = (800 - anchoTotalArbol) / 2;
            if (xInicialCentrado < 20) {
                xInicialCentrado = 20;
            }

            // 3. Renderizar el árbol con los nuevos parámetros calculados
            dibujarSubArbol(g, modelo.getRaiz(), xInicialCentrado, y + 25, espacioVerticalDinamico);
        }

        // 4. Zona de seguridad garantizada para el resultado (Abajo a la derecha)
        double resultado = modelo.evaluar().getValor();
        g.setColor(Color.BLACK);
        g.setFont(new Font("Consolas", Font.BOLD, 22));
        g.drawString("Resultado: " + resultado, 530, 550);
    }

    private void dibujarSubArbol(Graphics g, Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> nodo, int x, int y, int espacioVertical) {
        if (nodo.getHijos().getTamano() == 0) {
            dibujarNodoSuelto(g, nodo.getContenido().toString(), x, y);
            return;
        }

        Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> izq = modelo.getIzquierda(nodo);
        Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> der = modelo.getDerecha(nodo);

        int anchoTotal = calcularAncho(nodo);
        int anchoIzq = (izq != null) ? calcularAncho(izq) : 0;

        int xNodoPadre = x + (anchoTotal / 2) - (TAMANO_NODO / 2);
        int yHijo = y + TAMANO_NODO + espacioVertical;

        g.setColor(Color.BLACK);

        if (izq != null) {
            int xHijoIzq = x + (anchoIzq / 2) - (TAMANO_NODO / 2);
            g.drawLine(xNodoPadre + TAMANO_NODO / 2, y + TAMANO_NODO / 2,
                    xHijoIzq + TAMANO_NODO / 2, yHijo + TAMANO_NODO / 2);
            dibujarSubArbol(g, izq, x, yHijo, espacioVertical);
        }

        if (der != null) {
            int xBaseSubArbolDerecho = x + anchoIzq + ESPACIO_HORIZONTAL;
            int anchoDer = calcularAncho(der);
            int xHijoDer = xBaseSubArbolDerecho + (anchoDer / 2) - (TAMANO_NODO / 2);
            g.drawLine(xNodoPadre + TAMANO_NODO / 2, y + TAMANO_NODO / 2,
                    xHijoDer + TAMANO_NODO / 2, yHijo + TAMANO_NODO / 2);
            dibujarSubArbol(g, der, xBaseSubArbolDerecho, yHijo, espacioVertical);
        }

        dibujarNodoSuelto(g, nodo.getContenido().toString(), xNodoPadre, y);
    }

    private void dibujarNodoSuelto(Graphics g, String contenido, int x, int y) {
        g.setColor(Color.WHITE);
        g.fillArc(x, y, TAMANO_NODO, TAMANO_NODO, 0, 360);

        g.setColor(Color.BLACK);
        g.drawArc(x, y, TAMANO_NODO, TAMANO_NODO, 0, 360);

        g.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();
        int anchoTexto = fm.stringWidth(contenido);
        int altoTexto = fm.getAscent();

        int xCentro = x + (TAMANO_NODO - anchoTexto) / 2;
        int yCentro = y + (TAMANO_NODO + altoTexto) / 2 - 3;

        g.drawString(contenido, xCentro, yCentro);
    }

    private int calcularAncho(Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> nodo) {
        if (nodo == null) return 0;
        if (nodo.getHijos().getTamano() == 0) return TAMANO_NODO;

        Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> izq = modelo.getIzquierda(nodo);
        Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> der = modelo.getDerecha(nodo);

        int anchoIzq = (izq != null) ? calcularAncho(izq) : 0;
        int anchoDer = (der != null) ? calcularAncho(der) : 0;

        return anchoIzq + ESPACIO_HORIZONTAL + anchoDer;
    }

    private int calcularProfundidad(Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> nodo) {
        if (nodo == null) return 0;
        if (nodo.getHijos().getTamano() == 0) return 1;

        int profIzq = calcularProfundidad(modelo.getIzquierda(nodo));
        int profDer = calcularProfundidad(modelo.getDerecha(nodo));

        return 1 + Math.max(profIzq, profDer);
    }
}