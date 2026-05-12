package subsistemarrhh;

import java.time.LocalDate;
import java.time.Period; // Para calcular la edad

public abstract class Persona {

    private String nombre;
    private String apellido;
    private String dni;
    private LocalDate fechaNacimiento; 

    public Persona() {}

    public Persona(String nombre, String apellido, String dni, LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
    }

    // --- Getters y Setters ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    /**
     Calcula la edad en tiempo real
     */
    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(this.fechaNacimiento, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        // Usamos getEdad() para mostrar la edad calculada
        return "Nombre: " + nombre + " " + apellido + " | DNI: " + dni + " | Edad: " + getEdad();
    }
}
