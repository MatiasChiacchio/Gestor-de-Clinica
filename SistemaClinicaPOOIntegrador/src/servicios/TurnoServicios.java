package servicios;

import java.time.LocalDate;
import repositorios.TurnoRepositorio;
import subsistematurnos.Turno;
import subsistematurnos.EstadoTurno;
import subsistemapacientes.Paciente;
import subsistemarrhh.Medico;

public class TurnoServicios {
    private TurnoRepositorio turnoRepo;

    public TurnoServicios(TurnoRepositorio turnoRepo) {
        this.turnoRepo = turnoRepo;
    }

    public void registrarTurno(LocalDate fecha, String hora, Paciente p, Medico m) throws Exception {
        if (fecha.isBefore(LocalDate.now())) {
            throw new Exception("No se puede asignar turno en una fecha pasada.");
        }
        if (turnoRepo.existeTurno(m.getDni(), fecha, hora)) {
            throw new Exception("El médico ya tiene un turno en ese horario.");
        }
        // Creamos y guardamos el nuevo turno (Nace como PENDIENTE)
        turnoRepo.guardar(new Turno(fecha, hora, p, m));
    }

    /**
     * Cambia el estado a CONFIRMADO.
     */
    public void confirmarTurno(int numeroTurno) throws Exception {
        Turno t = turnoRepo.buscarPorId(numeroTurno);
        
        if (t == null) {
            throw new Exception("El turno no existe.");
        }
        
        if (t.getEstado() == EstadoTurno.CANCELADO) {
            throw new Exception("No se puede confirmar un turno que ya fue cancelado.");
        }

        t.setEstado(EstadoTurno.CONFIRMADO);
    }

    /**
     * Baja Lógica: Marca el turno como CANCELADO pero no lo borra.
     * Ideal para mantener el historial.
     */
    public void cancelarTurno(int numeroTurno) throws Exception {
        Turno t = turnoRepo.buscarPorId(numeroTurno);
        
        if (t == null) {
            throw new Exception("El turno no existe.");
        }

        t.setEstado(EstadoTurno.CANCELADO);
    }

    /**
     * Baja Física: Elimina el turno definitivamente.
     * Úsalo solo si fue un error de carga.
     */
    public void eliminarTurno(int numeroTurno) {
        turnoRepo.eliminar(numeroTurno);
    }
}