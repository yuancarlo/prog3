package AccesoADatos.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "empleados")
public class EmpleadoBd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Entero Primary key

    @Column(length = 200, nullable = false)
    private String nombreCompleto; // Varchar(200)

    @Column(nullable = false)
    private boolean activo; // Boolean

    @Column(nullable = false)
    private int edad; // Entero

    @Column(nullable = false)
    private LocalDate fechaIngreso; // Fecha

    public EmpleadoBd() {}

    public EmpleadoBd(String nombreCompleto, boolean activo, int edad, LocalDate fechaIngreso) {
        this.nombreCompleto = nombreCompleto;
        this.activo = activo;
        this.edad = edad;
        this.fechaIngreso = fechaIngreso;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
}
