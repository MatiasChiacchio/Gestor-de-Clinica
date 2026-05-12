package repositorios;

import java.util.List;
import subsistematurnos.Turno;
import subsistematurnos.EstadoTurno;

public class TurnoRepositorio {
    private List<Turno> lista;

    public TurnoRepositorio(List<Turno> lista) {
        this.lista = lista;
    }

    public void guardar(Turno t) {
        lista.add(t);
    }

    /**
     * Elimina físicamente un turno de la lista (Borrado total).
     */
    public void eliminar(int numeroTurno) {
        lista.removeIf(t -> t.getNumeroTurno() == numeroTurno);
    }

    /**
     * Busca un turno por su ID único.
     * Necesario para confirmar o cancelar.
     * @param numero
     * @return 
     */
    public Turno buscarPorId(int numero) {
        return lista.stream()
                .filter(t -> t.getNumeroTurno() == numero)
                .findFirst()
                .orElse(null);
    }

    public boolean existeTurno(String dniMedico, java.time.LocalDate fecha, String hora) {
        return lista.stream().anyMatch(t -> 
            t.getMedico().getDni().equals(dniMedico) &&
            t.getFecha().equals(fecha) &&
            t.getHora().equals(hora) &&
            t.getEstado() != EstadoTurno.CANCELADO
        );
    }
    
    public boolean pacienteTieneTurnos(String dniPaciente) {
        return lista.stream().anyMatch(t -> t.getPaciente().getDni().equals(dniPaciente));
    }

    public List<Turno> listarTodos() {
        return lista;
    }
}