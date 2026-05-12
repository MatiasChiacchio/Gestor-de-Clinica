package servicios;

import repositorios.PacienteRepositorio;
import repositorios.TurnoRepositorio;
import subsistemapacientes.Paciente;

public class PacienteServicios {
    private PacienteRepositorio pacienteRepo;
    private TurnoRepositorio turnoRepo;

    public PacienteServicios(PacienteRepositorio pr, TurnoRepositorio tr) {
        this.pacienteRepo = pr;
        this.turnoRepo = tr;
    }

    public void registrarPaciente(Paciente p) throws Exception {
        if (pacienteRepo.buscarPorDni(p.getDni()) != null) {
            throw new Exception("Ya existe un paciente con ese DNI.");
        }
        pacienteRepo.guardar(p);
    }

    public void eliminarPaciente(String dni) throws Exception {
        if (turnoRepo.pacienteTieneTurnos(dni)) {
            throw new Exception("No se puede eliminar: El paciente tiene turnos asociados.");
        }
        if (!pacienteRepo.eliminar(dni)) {
            throw new Exception("Paciente no encontrado.");
        }
    }
}