package subsistemarrhh;

import java.time.LocalDate;
/**
 * Clase Administrativo
 * 
 * Representa a un empleado administrativo dentro de la clínica.
 * Hereda de Empleado e implementa la interfaz Registrable,
 * añadiendo información específica del sector en el que trabaja.
 */
public class Administrativo extends Empleado implements Registrable {

    //  Atributos 
    private Sector sector;   // Sector o área en la que trabaja el administrativo

    /**
     * Constructor principal
     * 
     * Crea un nuevo empleado administrativo con todos sus datos personales y laborales.
     * @param nombre Nombre del administrativo
     * @param apellido Apellido del administrativo
     * @param dni Documento del administrativo
     * @param fechaNacimiento Edad del administrativo
     * @param fechaInicio Fecha de inicio en la clínica
     * @param sector Sector en el que trabaja
     */
    public Administrativo(String nombre, String apellido, String dni, LocalDate fechaNacimiento, String fechaInicio, Sector sector) {
        super(nombre, apellido, dni, fechaNacimiento, fechaInicio);
        this.sector = sector;
    }

    //  Métodos Getters y Setters
    //  Devuelven o modifican el sector del administrativo

    public Sector getSector() { return sector; }
    public void setSector(Sector sector) { this.sector = sector; }

    /**
     * Método registrar
     * 
     * Implementación del método de la interfaz Registrable.
     * Muestra un mensaje indicando que el administrativo fue registrado en el sistema.
     */
    @Override
    public void registrar() {
        System.out.println("✅ Administrativo registrado: " + getNombre() + " " + getApellido());
    }

    /**
     * Método toString
     * 
     * Devuelve una representación textual del administrativo,
     * incluyendo su información personal y el sector en el que trabaja.
     * @return Cadena con los datos principales del administrativo.
     */
    @Override
    public String toString() {
        return getNombre() + " " + getApellido() + 
               " (DNI: " + getDni() + ") - Sector: " + 
               sector;
    }
}
