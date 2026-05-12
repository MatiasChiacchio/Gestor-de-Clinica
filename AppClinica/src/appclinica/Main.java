package appclinica;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import subsistemalogueo.Clinica;
import subsistemalogueo.JsonClinica;
import repositorios.*;
import servicios.*;

/**
 * 
 * Clase Main (Punto de Entrada)
 * Configura la arquitectura de capas e inicia la aplicación.
 */
public class Main {

    public static void main(String[] args) {
        
        // 1. MEJORA VISUAL: Activar diseño moderno "Nimbus"
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el diseño Nimbus. Se usará el por defecto.");
        }

        // 2. CARGA DE DATOS (MODELO)
        // Intentamos cargar del JSON, si no existe, creamos una clínica vacía
        Clinica clinicaCargada = JsonClinica.cargarClinica();
        if (clinicaCargada == null) {
            clinicaCargada = new Clinica("Clínica Central");
        }
        
        // Variable final para usar en el hilo gráfico
        final Clinica clinica = clinicaCargada;

        // 3. CAPA DE DATOS (REPOSITORIOS)
        // Conectamos los repositorios a las listas de la clínica
        UsuarioRepositorio   usuarioRepo   = new UsuarioRepositorio(clinica.getUsuarios());
        PacienteRepositorio  pacienteRepo  = new PacienteRepositorio(clinica.getPacientes());
        EmpleadoRepositorio  empleadoRepo  = new EmpleadoRepositorio(clinica.getEmpleados());
        TurnoRepositorio     turnoRepo     = new TurnoRepositorio(clinica.getTurnos());
        HistorialRepositorio historialRepo = new HistorialRepositorio(clinica.getHistoriales());

        // 4. CAPA LÓGICA (SERVICIOS)
        // Inyectamos los repositorios necesarios a cada servicio
        // Nota: Algunos servicios necesitan más de un repo para validar integridad
        UsuarioServicios   usuarioService   = new UsuarioServicios(usuarioRepo);
        PacienteServicios  pacienteService  = new PacienteServicios(pacienteRepo, turnoRepo); // Valida borrado si tiene turnos
        TurnoServicios    turnoService     = new TurnoServicios(turnoRepo);
        EmpleadoServicios  empleadoService  = new EmpleadoServicios(empleadoRepo); // Valida borrado de médicos
        HistorialServicios historialService = new HistorialServicios(historialRepo);

        // 5. INICIO DE LA INTERFAZ GRÁFICA (VISTA)
        SwingUtilities.invokeLater(() -> {
            // Creamos la ventana de Login inyectándole TODOS los servicios y el modelo
            new VentanaLogin(
                clinica, 
                usuarioService, 
                pacienteService, 
                turnoService, 
                empleadoService, 
                historialService
            ).setVisible(true);
        });
    }
}