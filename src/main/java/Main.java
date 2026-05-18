import Utilidades.ListaEnlazadaDoble;

public class Main {
    public static void main(String[] args) {
        ListaEnlazadaDoble<Integer> listaDoble = new ListaEnlazadaDoble<>();
        /*listaDoble.preponer(3);
        listaDoble.preponer(5);
        listaDoble.preponer(6);*/
        listaDoble.adicionar(54);
        listaDoble.adicionar(44);
        listaDoble.preponer(34);
        listaDoble.adicionar(55);
        System.out.println(listaDoble);
        System.out.println(listaDoble.toStringDetallado());
    }
}
