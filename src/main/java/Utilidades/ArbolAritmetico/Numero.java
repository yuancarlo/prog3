package Utilidades.ArbolAritmetico;

public class Numero extends OperacionAritmetica {
    private Double valor;

    public Numero(double v) {
        this.valor = v;
    }

    @Override
    public Numero evaluar(Numero[] operandos) {
        return this;
    }

    public double getValor() { return this.valor;}

    @Override
    public String toString() {
        return String.valueOf(valor);
    }
}
