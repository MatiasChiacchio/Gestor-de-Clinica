package repositorios;

import java.util.List;
import java.util.stream.Collectors;
import subsistemarrhh.Administrativo;
import subsistemarrhh.Empleado;
import subsistemarrhh.Medico;


public class EmpleadoRepositorio {
    private List<Empleado> lista;

    public EmpleadoRepositorio(List<Empleado> lista) {
        this.lista = lista;
    }

    public void guardar(Empleado e) {
        lista.add(e);
    }

    public void eliminar(Empleado e) {
        lista.remove(e);
    }

    public Empleado buscarPorDni(String dni) {
        return lista.stream()
                .filter(e -> e.getDni().equals(dni))
                .findFirst()
                .orElse(null);
    }

    public List<Empleado> listarTodos() {
        return lista;
    }

    // Métodos específicos para filtrar por tipo (Polimorfismo)
    public List<Medico> listarMedicos() {
        return lista.stream()
                .filter(e -> e instanceof Medico)
                .map(e -> (Medico) e)
                .collect(Collectors.toList());
    }

    public List<Administrativo> listarAdministrativos() {
        return lista.stream()
                .filter(e -> e instanceof Administrativo)
                .map(e -> (Administrativo) e)
                .collect(Collectors.toList());
    }
}
