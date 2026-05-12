package subsistemarrhh;

import java.time.LocalDate;
/**
 * Clase Medico
 * 
 * Representa a un médico dentro de la clínica.
 * Hereda de la clase Empleado e implementa la interfaz Registrable,
 * agregando el atributo de especialidad médica que distingue
 * a cada profesional.
 */
public class Medico extends Empleado implements Registrable {

    //  Atributos 
    private Especialidad especialidad;   // Especialidad médica del profesional

    /**
     * Constructor vacío
     * 
     * Crea un médico sin datos iniciales.
     */
    public Medico() {}

    /**
     * Constructor principal
     * 
     * Crea un médico con todos sus datos personales, laborales y de especialidad.
     * @param nombre Nombre del médico
     * @param apellido Apellido del médico
     * @param dni Documento del médico
     * @param fechaNacimiento Edad del médico
     * @param fechaInicio Fecha de inicio en la clínica
     * @param especialidad Especialidad médica del profesional
     */
    public Medico(String nombre, String apellido, String dni, LocalDate fechaNacimiento, String fechaInicio, Especialidad especialidad) {
        super(nombre, apellido, dni, fechaNacimiento, fechaInicio);
        this.especialidad = especialidad;
}

    //  Métodos Getters y Setters
    //  Devuelven o modifican el valor de la especialidad

    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }

    /**
     * Método registrar
     * 
     * Implementación del método de la interfaz Registrable.
     * Muestra un mensaje indicando que el médico fue registrado en el sistema.
     */
    @Override
    public void registrar() {
        System.out.println("✅ Médico registrado: " + getNombre() + " " + getApellido());
    }
    /**
     * Método toString
     * 
     * Devuelve una representación textual del médico,
     * incluyendo su información laboral y su especialidad.
     * @return Cadena con los datos principales del médico.
     */
    @Override
    public String toString() {
        return "Dr. " + getNombre() + " " + getApellido() + 
               " (DNI: " + getDni() + ") - " + 
               especialidad;
    }
}
