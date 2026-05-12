package subsistemahistorial;

import subsistemapacientes.Paciente;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase HistorialMedico
 * 
 * Representa el historial médico completo de un paciente.
 * Contiene una lista de entradas que registran las distintas
 * consultas, diagnósticos y atenciones que recibió el paciente.
 */
public class HistorialMedico {

    //  Atributos 
    private Paciente paciente;                     // Paciente al que pertenece el historial
    private List<EntradaHistorial> entradas;       // Lista de entradas médicas asociadas al paciente

    /**
     * Constructor principal
     * 
     * Crea un historial médico vacío para un paciente determinado.
     * @param paciente Paciente al cual se asocia este historial
     */
    public HistorialMedico(Paciente paciente) {
        this.paciente = paciente;
        this.entradas = new ArrayList<>();         // Inicializa la lista vacía de entradas
    }

    /**
     * Agrega una nueva entrada médica al historial.
     * 
     * @param e Objeto de tipo EntradaHistorial que se agregará
     */
    public void agregarEntrada(EntradaHistorial e) {
        if (e != null) entradas.add(e);            // Se agrega la entrada si no es nula
    }

    // Métodos Getters
    // Devuelven los valores de los atributos principales

    public List<EntradaHistorial> getEntradas() {
        return entradas;                           // Retorna la lista completa de entradas
    }

    public Paciente getPaciente() {
        return paciente;                           // Retorna el paciente asociado al historial
    }

    /**
     * Método toString
     * 
     * Devuelve una representación textual del historial del paciente,
     * mostrando la cantidad de entradas y los datos de cada una.
     * @return Texto con la información resumida del historial.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Historial de ")
          .append(paciente.getNombre())
          .append(" ")
          .append(paciente.getApellido())
          .append(" (")
          .append(entradas.size())
          .append(" entradas)\n");

        // Recorre todas las entradas y muestra sus datos principales
        for (EntradaHistorial e : entradas) {
            sb.append("- Fecha: ").append(e.getFecha())
              .append(" | Diagnóstico: ").append(e.getDiagnostico())
              .append(" | Médico: ").append(e.getMedico().getNombre()).append("\n");
        }

        return sb.toString();
    }
}
