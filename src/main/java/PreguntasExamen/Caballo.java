package PreguntasExamen;
//4.	Escriba un pequeño programa donde se simule una carrera de caballos. El programa debe contar con 2 clases, Programa y Caballo.
// Ademas, Caballo debe implementar Runnable para que pueda ser utilizado como Thread. Se deja a criterio del usuario el código de run()
// para todos los caballos simulando una carrera.

public class Caballo implements Runnable{
    private String nombre;
    private int distanciaRecorrida;
    private final int META = 100;

    public Caballo(String nombre){
        this.nombre = nombre;
        this.distanciaRecorrida = 0;
    }

    @Override
    public void run() {
        System.out.println("Caballo " + this.nombre + "inicio la carrera");
        while (distanciaRecorrida < META) {
            try {
                int avance = (int)(Math.random()*10)+1;
                distanciaRecorrida += avance;

                System.out.println(this.nombre + "lleva de distancia recorrida: " + distanciaRecorrida);

                Thread.sleep((int)(Math.random()*200)+100);
            }catch (InterruptedException e){
                System.out.println("tropezo y se detuvo");
                return;
            }
        }
        System.out.println("Caballo " + this.nombre + "finaliza la carrera");

    }

    public static void main(String[] args) {
        Caballo caballo = new Caballo("Rayo");
        Caballo caballo2 = new Caballo("Relampago");
        Caballo caballo3 = new Caballo("Patrick");

        Thread t1 = new Thread(caballo);
        Thread t2 = new Thread(caballo2);
        Thread t3 = new Thread(caballo3);

        System.out.println("Inicia la carrera");
        t1.start();
        t2.start();
        t3.start();
    }
}
