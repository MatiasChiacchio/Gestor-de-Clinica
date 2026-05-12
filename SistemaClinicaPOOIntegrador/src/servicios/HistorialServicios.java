package servicios;

import repositorios.HistorialRepositorio;
import subsistemahistorial.EntradaHistorial;
import subsistemahistorial.HistorialMedico;
import subsistemapacientes.Paciente;

public class HistorialServicios {
    private HistorialRepositorio historialRepo;

    public HistorialServicios(HistorialRepositorio hr) {
        this.historialRepo = hr;
    }

    public void agregarEntrada(Paciente p, EntradaHistorial entrada) {
        HistorialMedico historial = historialRepo.buscarPorDniPaciente(p.getDni());
        if (historial == null) {
            historial = new HistorialMedico(p);
            historialRepo.guardar(historial);
        }
        historial.agregarEntrada(entrada);
    }
}
