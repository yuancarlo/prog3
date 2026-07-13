package VisorDeImagenes.modelo;

public interface Cifrable<G> {
    G cifrar(G dato, String clave);
    G descifrar(G dato, String clave);
}