package AccesoADatos.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Boolean activo;

    @Column(nullable = false)
    private Integer edad;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    public Persona() {}

    public Persona(String nombre, Boolean activo, Integer edad, LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.activo = activo;
        this.edad = edad;
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
}