package subsistemahistorial;

import java.time.LocalDate;
import subsistemarrhh.Medico;

/**
 * Clase EntradaHistorial
 * Representa un registro de atención médica dentro del historial de un paciente.
 */
public class EntradaHistorial {

    // Atributos
    private LocalDate fecha; // <--- CAMBIO: Ahora es LocalDate
    private String diagnostico;
    private String observaciones;
    private Medico medico;

    public EntradaHistorial() {
    }

    /**
     * Constructor principal
     * @param fecha Fecha de la atención (LocalDate)
     * @param diagnostico Diagnóstico realizado
     * @param observaciones Notas adicionales
     * @param medico Médico que atendió
     */
    public EntradaHistorial(LocalDate fecha, String diagnostico, String observaciones, Medico medico) {
        this.fecha = fecha;
        this.diagnostico = diagnostico;
        this.observaciones = observaciones;
        this.medico = medico;
    }

    // --- Getters y Setters ---

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    @Override
    public String toString() {
        // Validación por si se borró el médico
        String nombreMedico = (medico != null) ? medico.getNombre() + " " + medico.getApellido() : "Sin médico";
        
        return "Fecha: " + fecha + 
               " | Médico: " + nombreMedico + 
               " | Diagnóstico: " + diagnostico + 
               " | Obs: " + observaciones;
    }
}
