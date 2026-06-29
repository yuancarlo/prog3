package Utilidades.ArbolAritmetico;

import Utilidades.Arbol;

public class ArbolAritmetico extends Arbol<OperacionAritmetica> {

    public ArbolAritmetico() {
        super();
    }

    @Override
    public String insertar(String padre, OperacionAritmetica hijo) {
        String idHijo ="OA" + nodos.size();
        hijo.setId(idHijo);
        super.insertar(padre, hijo);
        return idHijo;
    }

    @Override
    public String toString() {
        return toString(raiz);
    }

    private String toString(Nodo<OperacionAritmetica> nodo) {
        if (nodo.getHijos().getTamano() == 0)
            return nodo.toString();
        return "(" + toString(getIzquierda(nodo)) +
                nodo.getContenido().toString() +
                toString(getDerecha(nodo)) +
                ")";
    }

    public Numero evaluar() {
        return evaluar(raiz);
    }

    private Numero evaluar(Nodo<OperacionAritmetica> nodo) {
        if (nodo.getHijos().getTamano() == 0) {
            return nodo.getContenido().evaluar(null);
        }
        Nodo<OperacionAritmetica> izq = getIzquierda(nodo);
        Nodo<OperacionAritmetica> der = getDerecha(nodo);

        Numero[] operandos = new Numero[2];
        operandos[0] = evaluar(izq);
        operandos[1] = evaluar(der);

        return nodo.getContenido().evaluar(operandos);
    }

    public Nodo<OperacionAritmetica> getIzquierda(Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> padre) {
        Nodo<OperacionAritmetica> hijo = padre.getHijos().getValorPorIndice(1);
        return hijo;
    }
    public Nodo<OperacionAritmetica> getDerecha(Arbol<OperacionAritmetica>.Nodo<OperacionAritmetica> padre) {
        Nodo<OperacionAritmetica> hijo = padre.getHijos().getValorPorIndice(0);
        return hijo;
    }
}
