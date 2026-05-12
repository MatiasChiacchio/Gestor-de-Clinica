package servicios;

import repositorios.EmpleadoRepositorio;
import subsistemarrhh.Administrativo;
import subsistemarrhh.Empleado;
import subsistemarrhh.Especialidad;
import subsistemarrhh.Medico;
import subsistemarrhh.Sector;

public class EmpleadoServicios {
    
    private EmpleadoRepositorio empleadoRepo;

    // CONSTRUCTOR 
    public EmpleadoServicios(EmpleadoRepositorio er) {
        this.empleadoRepo = er;
    }

    // --- LÓGICA DE REGISTRO Y BAJA ---

    public void registrarEmpleado(Empleado e) throws Exception {
        if (empleadoRepo.buscarPorDni(e.getDni()) != null) {
            throw new Exception("Ya existe un empleado con ese DNI.");
        }
        empleadoRepo.guardar(e);
    }

    public void eliminarEmpleado(String dni) throws Exception {
        Empleado e = empleadoRepo.buscarPorDni(dni);
        if (e == null) throw new Exception("Empleado no encontrado.");
        
        empleadoRepo.eliminar(e);
    }
    
    public Empleado buscarPorDni(String dni) {
        return empleadoRepo.buscarPorDni(dni);
    }

    // --- SOBRECARGA PARA MÉDICOS ---

    public Medico buscarMedico(String dni) {
        Empleado e = empleadoRepo.buscarPorDni(dni);
        return (e instanceof Medico) ? (Medico) e : null;
    }

    public Medico buscarMedico(Especialidad especialidad) {
        // Obtenemos todos y filtramos (sin usar Collectors)
        return empleadoRepo.listarTodos().stream()
                .filter(e -> e instanceof Medico)
                .map(e -> (Medico) e)
                .filter(m -> m.getEspecialidad() == especialidad)
                .findFirst()
                .orElse(null);
    }

    // --- SOBRECARGA PARA ADMINISTRATIVOS ---

    public Administrativo buscarAdministrativo(String dni) {
        Empleado e = empleadoRepo.buscarPorDni(dni);
        return (e instanceof Administrativo) ? (Administrativo) e : null;
    }

    public Administrativo buscarAdministrativo(Sector sector) {
        return empleadoRepo.listarTodos().stream()
                .filter(e -> e instanceof Administrativo)
                .map(e -> (Administrativo) e)
                .filter(a -> a.getSector() == sector)
                .findFirst()
                .orElse(null);
    }
}