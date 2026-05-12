package subsistematurnos;

import subsistemapacientes.Paciente;
import subsistemarrhh.Medico;
import java.time.LocalDate; // Importante para manejar fechas reales

public class Turno {
    
    // 1. Variable estática para generar IDs únicos automáticamente
    private static int contadorTurnos = 0; 

    private int numeroTurno;
    private LocalDate fecha; // Ahora es LocalDate, no String
    private String hora;
    private Paciente paciente;
    private Medico medico;
    private EstadoTurno estado;

    // Constructor vacío
    public Turno() {
    }

    // 2. CONSTRUCTOR QUE USA TU PANEL (Coincide con los parámetros que envías)
    public Turno(LocalDate fecha, String hora, Paciente paciente, Medico medico) {
        this.numeroTurno = ++contadorTurnos; // Se autoincrementa el ID
        this.fecha = fecha;
        this.hora = hora;
        this.paciente = paciente;
        this.medico = medico;
        this.estado = EstadoTurno.PENDIENTE; // Estado por defecto
    }

    // --- Getters y Setters ---

    public int getNumeroTurno() {
        return numeroTurno;
    }

    public void setNumeroTurno(int numeroTurno) {
        this.numeroTurno = numeroTurno;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public void setEstado(EstadoTurno estado) {
        this.estado = estado;
    }
    
    // Método estático para sincronizar el contador al cargar desde JSON
    public static void actualizarUltimoId(int maxId) {
        contadorTurnos = maxId;
    }

    @Override
    public String toString() {
        // Validación para evitar errores si el médico o paciente son nulos
        String nombrePac = (paciente != null) ? paciente.getNombre() + " " + paciente.getApellido() : "Sin Paciente";
        String nombreMed = (medico != null) ? medico.getNombre() + " " + medico.getApellido() : "Sin Médico";
        
        return "Turno N°: " + numeroTurno + 
               " | Fecha: " + fecha + 
               " | Hora: " + hora + 
               " | Paciente: " + nombrePac + 
               " | Médico: " + nombreMed + 
               " | Estado: " + estado;
    }
}
