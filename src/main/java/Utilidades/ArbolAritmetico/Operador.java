package Utilidades.ArbolAritmetico;

public class Operador extends OperacionAritmetica {
    private String tipo;
    private EnumOperacion operacion;
    private String simbolo;

    public Operador(EnumOperacion op) {
        this.operacion = op;
        this.tipo = getOperacionString(op);
        this.simbolo = getSimboloOperacion(op);
    }

    @Override
    public Numero evaluar(Numero[] operandos) {
        if(operandos.length != 2) {
            return new Numero(Double.MIN_VALUE);
        }
        Numero izq = operandos[0];
        Numero der = operandos[1];
        switch(operacion) {
            case Suma -> { return new Numero(izq.getValor() + der.getValor());}
            case Resta -> {return new Numero(izq.getValor() - der.getValor());}
            case Multiplicacion -> { return new Numero(izq.getValor() * der.getValor()); }
            case Division -> { return new Numero(izq.getValor() / der.getValor());}
        }
        return new Numero(Double.MIN_VALUE);
    }

    private String getSimboloOperacion(EnumOperacion op) {
        switch(op) {
            case Suma -> { return "+";}
            case Resta -> {return "-";}
            case Multiplicacion -> { return "*";}
            case Division -> { return "/";}
        }
        return null;
    }

    private String getOperacionString(EnumOperacion op) {
        switch(op) {
            case Suma -> { return "Suma";}
            case Resta -> {return "Resta";}
            case Multiplicacion -> { return "Multiplicacion";}
            case Division -> { return "Division";}
        }
        return null;
    }

    @Override
    public String toString() {
        return simbolo;
    }
}
