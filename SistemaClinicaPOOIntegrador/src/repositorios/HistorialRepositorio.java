package repositorios;

import java.util.List;
import subsistemahistorial.HistorialMedico;

public class HistorialRepositorio {
    private List<HistorialMedico> lista;

    public HistorialRepositorio(List<HistorialMedico> lista) {
        this.lista = lista;
    }

    public void guardar(HistorialMedico h) {
        lista.add(h);
    }

    public HistorialMedico buscarPorDniPaciente(String dniPaciente) {
        return lista.stream()
                .filter(h -> h.getPaciente().getDni().equals(dniPaciente))
                .findFirst()
                .orElse(null);
    }
    
    // Verifica si un paciente ya tiene historia clínica creada
    public boolean existeHistorial(String dniPaciente) {
        return buscarPorDniPaciente(dniPaciente) != null;
    }

    public List<HistorialMedico> listarTodos() {
        return lista;
    }
}
