package ArbolJSON.Modelo;

import java.util.ArrayList;
import java.util.List;


public class NodoDTO {
    private String contenido;
    private List<NodoDTO> hijos;

    public NodoDTO() {
        this.hijos = new ArrayList<>();
    }

    public NodoDTO(String contenido) {
        this.contenido = contenido;
        this.hijos = new ArrayList<>();
    }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public List<NodoDTO> getHijos() { return hijos; }
    public void setHijos(List<NodoDTO> hijos) { this.hijos = hijos; }
}