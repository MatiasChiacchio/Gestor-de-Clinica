package subsistemarrhh;

import java.time.LocalDate;
/**
 * Clase Empleado
 * 
 * Clase abstracta que representa a un empleado de la clínica.
 * Hereda de Persona e incorpora información laboral básica como
 * el legajo interno y la fecha de inicio en la institución.
 * 
 * Esta clase sirve como base para los distintos tipos de empleados
 * (por ejemplo, Administrativo o Médico).
 */
public abstract class Empleado extends Persona {

    //  Atributos 
    private String fechaInicio;   // Fecha en la que el empleado comenzó a trabajar

    /**
     * Constructor vacío
     * 
     * Crea un empleado sin datos iniciales.
     */
    public Empleado() {}

    /**
     * Constructor principal
     * 
     * Crea un empleado con todos los datos personales y laborales.
     * @param nombre Nombre del empleado
     * @param apellido Apellido del empleado
     * @param dni Documento del empleado
     * @param fechaNacimiento Edad del empleado
     * @param fechaInicio Fecha de inicio en la clínica
     */
    // En el constructor:
    public Empleado(String nombre, String apellido, String dni, LocalDate fechaNacimiento, String fechaInicio) {
        super(nombre, apellido, dni, fechaNacimiento); // Pasa la fecha al padre
        this.fechaInicio = fechaInicio;
    }

    //  Métodos Getters y Setters
    //  Devuelven o modifican los valores de los atributos principales

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    /**
     * Método toString
     * 
     * Devuelve una representación textual del empleado,
     * mostrando tanto su información personal como laboral.
     * @return Cadena con los datos del empleado.
     */
    @Override
    public String toString() {
        return super.toString() + " | Fecha de Inicio: " + fechaInicio;
    }
}
