package subsistemalogueo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import subsistemahistorial.HistorialMedico;
import subsistemapacientes.Paciente;
import subsistemarrhh.Empleado;
import subsistemarrhh.Medico;
import subsistematurnos.Turno;

public class Clinica {

    // --- ATRIBUTOS (ESTADO DEL SISTEMA) ---
    private String nombre;
    private List<Empleado> empleados;
    private List<Paciente> pacientes;
    private List<Turno> turnos;
    private List<HistorialMedico> historiales;
    private List<Usuarios> usuarios;

    // --- CONSTRUCTORES ---
    
    // Constructor vacío necesario para GSON
    public Clinica() {
        this("Clínica Central");
    }

    public Clinica(String nombre) {
        this.nombre = nombre;
        // Inicializamos las listas para evitar NullPointerException
        this.empleados = new ArrayList<>();
        this.pacientes = new ArrayList<>();
        this.turnos = new ArrayList<>();
        this.historiales = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }

    // --- GETTERS DE LISTAS (Acceso para los Repositorios) ---
    
    public List<Empleado> getEmpleados() { return empleados; }
    public List<Paciente> getPacientes() { return pacientes; }
    public List<Turno> getTurnos() { return turnos; }
    public List<HistorialMedico> getHistoriales() { return historiales; }
    public List<Usuarios> getUsuarios() { return usuarios; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    // --- MÉTODOS DE UTILIDAD (HELPERS) ---

    /**
     * Filtra la lista de empleados para devolver solo los Médicos.
     * Útil para llenar ComboBoxes en la interfaz gráfica.
     * Demuestra Polimorfismo por Inclusión (instanceof).
     */
    public List<Medico> getMedicos() {
        return empleados.stream()
                .filter(e -> e instanceof Medico)
                .map(e -> (Medico) e)
                .collect(Collectors.toList());
    }

    /**
     * Método genérico para imprimir reportes en consola.
     * Demuestra Polimorfismo Paramétrico (Generics).
     * @param <T> El tipo de dato de la lista.
     */
    public <T> void imprimirReporteGenerico(List<T> lista) {
        System.out.println("--- Reporte Genérico (" + lista.size() + " items) ---");
        for (T elemento : lista) {
            System.out.println(elemento.toString());
        }
    }
}
