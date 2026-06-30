package ServidorWeb.Modelo;

import Utilidades.IDibujable;
import Utilidades.Arbol;
import Utilidades.ArbolAritmetico.ArbolAritmetico;
import Utilidades.ArbolAritmetico.OperacionAritmetica;
import java.awt.*;

public class DibujoArbolAritmetico implements IDibujable {

    private static final int LIENZO_ANCHO = 800;
    private static final int LIENZO_ALTO = 600;
    private static final int TAMANO_NODO = 50;
    private static final int ESPACIO_HORIZONTAL = 30;
    private static final int ESPACIO_VERTICAL = 55;
    private static final int ALTURA_MAXIMA_PERMITIDA = 400;
    private static final int MIN_ESPACIO_VERTICAL = 25;

    private final ArbolAritmetico modelo;

    public DibujoArbolAritmetico(ArbolAritmetico modelo) {
        this.modelo = modelo;
    }

   
    @Override
    public void dibujar(Graphics g, int x, int y) {
        dibujarFondo(g);

        if (modelo.getRaiz() != null) {
            int profundidad = calcularProfundidad(modelo.getRaiz());
            int espacioVertical = calcularEspacioVerticalDinamico(profundidad);
            int xInicial = calcularCentradoHorizontal();

            dibujarSubArbol(g, modelo.getRaiz(), xInicial, y + 25, espacioVertical);
        }

        dibujarResultado(g);
    }

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
        if (profundidad <= 1) return ESPACIO_VERTICAL;

        int alturaArbolNatural = (profundidad * TAMANO_NODO) + ((profundidad - 1) * ESPACIO_VERTICAL);

        if (alturaArbolNatural > ALTURA_MAXIMA_PERMITIDA) {
            int espacioDisponible = ALTURA_MAXIMA_PERMITIDA - (profundidad * TAMANO_NODO);
            int espacioCalculado = espacioDisponible / (profundidad - 1);
            return Math.max(espacioCalculado, MIN_ESPACIO_VERTICAL);
        }

        return ESPACIO_VERTICAL;
    }

    private int calcularCentradoHorizontal() {
        int anchoTotal = calcularAncho(modelo.getRaiz());
        int xCentrado = (LIENZO_ANCHO - anchoTotal) / 2;
        return Math.max(xCentrado, 20); 
    }


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

    
        for (Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> hijo : nodo.getHijos()) {
            int anchoHijo = calcularAncho(hijo);
            int xCentroHijo = xHijoActual + (anchoHijo / 2);

            g.drawLine(xCentroNodo + (TAMANO_NODO / 2), y + (TAMANO_NODO / 2),
                    xCentroHijo, yHijos + (TAMANO_NODO / 2));

         
            dibujarSubArbol(g, hijo, xHijoActual, yHijos, espacioVertical);

            xHijoActual += anchoHijo + ESPACIO_HORIZONTAL;
        }

        dibujarNodoSuelto(g, nodo.getContenido().toString(), xCentroNodo, y);
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