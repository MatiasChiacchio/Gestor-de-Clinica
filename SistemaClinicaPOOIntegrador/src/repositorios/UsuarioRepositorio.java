package repositorios;

import java.util.List;
import subsistemalogueo.Usuarios;

public class UsuarioRepositorio {
    private List<Usuarios> lista;

    public UsuarioRepositorio(List<Usuarios> lista) {
        this.lista = lista;
    }

    public void guardar(Usuarios u) {
        lista.add(u);
    }

    public Usuarios buscarPorNombreUsuario(String nombreUsuario) {
        return lista.stream()
                .filter(u -> u.getNombreUsuario().equalsIgnoreCase(nombreUsuario))
                .findFirst()
                .orElse(null);
    }

    public Usuarios buscarParaLogin(String usuario, String contrasena) {
        return lista.stream()
                .filter(u -> u.autenticar(usuario, contrasena))
                .findFirst()
                .orElse(null);
    }

    public boolean existeUsuario(String nombreUsuario) {
        return buscarPorNombreUsuario(nombreUsuario) != null;
    }
    
    public List<Usuarios> listarTodos() {
        return lista;
    }
}
