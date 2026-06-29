package ArbolJSON.Modelo;

import Utilidades.Arbol;
import Utilidades.IDibujable;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DibujoArbol implements IDibujable {
    private static final int TAMANO_NODO = 60;
    private static final int ESPACIO_HORIZONTAL = 30;
    private static final int ESPACIO_VERTICAL = 40;
    private Arbol<String> modelo;
    private Map<Rectangle, String> areasClickeables;

    public DibujoArbol(Arbol<String> modelo) {
        this.modelo = modelo;
        this.areasClickeables = new HashMap<>();
    }

    public Map<Rectangle, String> getAreasClickeables() {
        return areasClickeables;
    }

    @Override
    public void dibujar(Graphics g, int x, int y) {
        areasClickeables.clear();
        Arbol<String>.Nodo<String> raiz = modelo.getRaiz();
        if (raiz != null) {
            dibujarSubArbol(g, raiz, x, y);
        }
    }

    private void dibujarSubArbol(Graphics g, Arbol<String>.Nodo<String> nodo, int x, int y) {
        if (nodo.getHijos().getTamano() == 0) {
            dibujarNodoSuelto(g, nodo.getContenido(), x, y);
            return;
        }

        int ancho = calcularAncho(nodo);
        int xNodo = x + ancho / 2 - TAMANO_NODO / 2;
        int xHijo = x;
        int yHijo = y + TAMANO_NODO + ESPACIO_VERTICAL;

        for(Arbol<String>.Nodo<String> hijo : nodo.getHijos()) {
            int anchoHijo = calcularAncho(hijo);
            g.setColor(Color.black);
            g.drawLine(xNodo + TAMANO_NODO / 2, y + TAMANO_NODO / 2,
                    xHijo + anchoHijo / 2, yHijo + TAMANO_NODO / 2);

            dibujarSubArbol(g, hijo, xHijo, yHijo);
            xHijo = xHijo + ESPACIO_HORIZONTAL + anchoHijo;
        }

        dibujarNodoSuelto(g, nodo.getContenido(), xNodo, y);
    }

    private void dibujarNodoSuelto(Graphics g, String contenido, int x, int y) {
        g.setColor(Color.white);
        g.fillArc(x, y, TAMANO_NODO, TAMANO_NODO, 0, 360);

        g.setColor(Color.black);
        g.drawArc(x, y, TAMANO_NODO, TAMANO_NODO, 0, 360);
        Font myFont = new Font("Arial", Font.PLAIN, 20);
        g.setFont(myFont);

        g.drawString(contenido, x + TAMANO_NODO / 2 - 5, y + TAMANO_NODO / 2 + 5);

        areasClickeables.put(new Rectangle(x, y, TAMANO_NODO, TAMANO_NODO), contenido);
    }

    private int calcularAncho(Arbol<String>.Nodo<String> nodo) {
        if(nodo.getHijos().getTamano() == 0) {
            return TAMANO_NODO;
        }
        int resultado = 0;
        int separacionHorizontal = 0;
        for(Arbol<String>.Nodo<String> hijo : nodo.getHijos()) {
            resultado = resultado + separacionHorizontal + calcularAncho(hijo);
            separacionHorizontal = ESPACIO_HORIZONTAL;
        }
        return resultado;
    }
}