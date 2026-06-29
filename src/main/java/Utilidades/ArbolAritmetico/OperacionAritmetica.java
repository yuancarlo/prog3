package Utilidades.ArbolAritmetico;


import Utilidades.Identificable;

public abstract class OperacionAritmetica implements Identificable {
    protected String id;

    @Override
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    public abstract Numero evaluar(Numero[] operandos);
}
