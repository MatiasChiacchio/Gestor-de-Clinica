package subsistemapacientes;

import subsistemarrhh.Persona;
import java.time.LocalDate;

public class Paciente extends Persona { 

    private int numeroHistoriaClinica;
    private ObraSocial obraSocial;
    private boolean activo;

    // Constructor vacío (Necesario para GSON)
    public Paciente() {
        super();
        this.activo = true;
    }

    // Contructor completo
    public Paciente(String nombre, String apellido, String dni, LocalDate fechaNacimiento, int numeroHistoriaClinica, ObraSocial obraSocial) {
        super(nombre, apellido, dni, fechaNacimiento);
        this.numeroHistoriaClinica = numeroHistoriaClinica;
        this.obraSocial = obraSocial;
        this.activo = true;
    }   

    // --- Getters y Setters ---

    public int getNumeroHistoriaClinica() {
        return numeroHistoriaClinica;
    }

    public void setNumeroHistoriaClinica(int numeroHistoriaClinica) {
        this.numeroHistoriaClinica = numeroHistoriaClinica;
    }

    public ObraSocial getObraSocial() {
        return obraSocial;
    }

    public void setObraSocial(ObraSocial obraSocial) {
        this.obraSocial = obraSocial;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        // Usamos StringBuilder como buena práctica de memoria
        StringBuilder sb = new StringBuilder();
        sb.append(getNombre()).append(" ").append(getApellido());
        sb.append(" (DNI: ").append(getDni()).append(")");
        sb.append(" - ").append(getEdad()).append(" años");
        
        // Si quieres mostrar si está inactivo en el combo:
        if (!activo) {
            sb.append(" [INACTIVO]");
        }
        
        return sb.toString();
    }
}
