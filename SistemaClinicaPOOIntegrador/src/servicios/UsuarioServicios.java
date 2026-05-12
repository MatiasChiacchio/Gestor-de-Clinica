package servicios;

import repositorios.UsuarioRepositorio;
import subsistemalogueo.Usuarios;

public class UsuarioServicios {
    private UsuarioRepositorio usuarioRepo;

    public UsuarioServicios(UsuarioRepositorio ur) {
        this.usuarioRepo = ur;
    }

    public Usuarios login(String user, String pass) throws Exception {
        Usuarios u = usuarioRepo.buscarParaLogin(user, pass);
        if (u == null) throw new Exception("Usuario o contraseña incorrectos.");
        return u;
    }

    public void registrarUsuario(Usuarios nuevo) throws Exception {
        if (usuarioRepo.existeUsuario(nuevo.getNombreUsuario())) {
            throw new Exception("El usuario ya existe.");
        }
        usuarioRepo.guardar(nuevo);
    }
}