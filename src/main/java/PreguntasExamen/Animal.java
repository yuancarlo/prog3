package PreguntasExamen;
//Que es clase y que es objeto? Definición y ejemplos

public class Animal {
    /**Una clase es un molde o plano que define las caracteristicas y comportamientos de un tipo de dato(objeto),
    no es un objeto, sino es la definicion de la estructura de un objeto mediante atributos y metodos
     **/
    private String nombre;
    private String raza;
    private int edad;

    public Animal(String nombre, String raza, int edad) {
        this.nombre = nombre;
        this.raza = raza;
        this.edad = edad;
    }

    public String getNombre() {return this.nombre;}
    public String ladrar() {return "GUAU, GUAU";}

    public static void main(String[] args) {
        /** Un objeto es una instancia concreta de una clase, con sus caracteristicas ya definidas(es cuando se crea un elemento real
          en memoria con datos especificos) **/
        Animal a = new Animal("Caniche", "San Bernando", 18);
        System.out.println(a.ladrar());
    }

}

