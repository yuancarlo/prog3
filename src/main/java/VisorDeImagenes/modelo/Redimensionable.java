package VisorDeImagenes.modelo;

public interface Redimensionable<G> {
    G agrandar(G objeto, int nuevoAncho, int nuevoAlto);
    G achicar(G objeto, int nuevoAncho, int nuevoAlto);
}