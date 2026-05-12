package repositorios;

import java.util.List;
import subsistemapacientes.Paciente;

public class PacienteRepositorio {
    private List<Paciente> lista;

    public PacienteRepositorio(List<Paciente> lista) {
        this.lista = lista;
    }

    public void guardar(Paciente p) {
        lista.add(p);
    }

    public Paciente buscarPorDni(String dni) {
        return lista.stream()
                .filter(p -> p.getDni().equals(dni))
                .findFirst().orElse(null);
    }

    public boolean eliminar(String dni) {
        return lista.removeIf(p -> p.getDni().equals(dni));
    }
    
    public List<Paciente> listarTodos() {
        return lista;
    }
}