package subsistemalogueo;

/**
 * Clase Usuarios
 * 
 * Representa un usuario dentro del sistema de la clínica.
 * Cada usuario tiene un nombre, contraseña, rol y datos de seguridad
 * que permiten su autenticación y recuperación de cuenta.
 */
public class Usuarios {

    //  Atributos 
    private String nombreUsuario;       // Nombre de usuario para iniciar sesión
    private String contraseña;          // Contraseña del usuario
    private Roles rol;                  // Rol asignado (Administrador, Médico, etc.)
    private String preguntaSecreta;     // Pregunta para recuperación de cuenta
    private String respuestaSecreta;    // Respuesta asociada a la pregunta secreta

    /**
     * Constructor principal
     * 
     * Crea un usuario con todos los datos necesarios, incluyendo la pregunta secreta.
     * @param nombreUsuario Nombre del usuario
     * @param contraseña Contraseña del usuario
     * @param rol Rol asignado (tipo Roles)
     * @param preguntaSecreta Pregunta de seguridad
     * @param respuestaSecreta Respuesta de seguridad
     */
   
    
    public Usuarios(String nombreUsuario, String contraseña, Roles rol, String preguntaSecreta, String respuestaSecreta) {
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.rol = rol;
        this.preguntaSecreta = preguntaSecreta;
        this.respuestaSecreta = respuestaSecreta;
    }

    /**
     * Constructor alternativo
     * 
     * Permite crear un usuario sin definir pregunta secreta,
     * asignando valores por defecto.
     * @param nombreUsuario Nombre del usuario
     * @param contraseña Contraseña del usuario
     * @param rol Rol asignado
     */
    public Usuarios(String nombreUsuario, String contraseña, Roles rol) {
        this(nombreUsuario, contraseña, rol, "color favorito", "azul");
    }

    //  Métodos Getters y Setters
    //  Devuelven o actualizan los valores de los atributos principales

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }

    public Roles getRol() { return rol; }
    public void setRol(Roles rol) { this.rol = rol; }

    public String getPreguntaSecreta() { return preguntaSecreta; }
    public void setPreguntaSecreta(String preguntaSecreta) { this.preguntaSecreta = preguntaSecreta; }

    public String getRespuestaSecreta() { return respuestaSecreta; }
    public void setRespuestaSecreta(String respuestaSecreta) { this.respuestaSecreta = respuestaSecreta; }

    /**
     * Método autenticar
     * 
     * Verifica si el nombre de usuario y la contraseña ingresados coinciden con los registrados.
     * @param usuario Nombre de usuario ingresado
     * @param pass Contraseña ingresada
     * @return true si las credenciales son correctas, false en caso contrario
     */
    public boolean autenticar(String usuario, String pass) {
        return this.nombreUsuario.equalsIgnoreCase(usuario) && this.contraseña.equals(pass);
    }

    /**
     * Método recuperarConPregunta
     * 
     * Verifica si la pregunta y respuesta de seguridad coinciden con las guardadas.
     * @param pregunta Pregunta de seguridad ingresada
     * @param respuesta Respuesta ingresada
     * @return true si la validación es correcta, false en caso contrario
     */
    public boolean recuperarConPregunta(String pregunta, String respuesta) {
        return this.preguntaSecreta.equalsIgnoreCase(pregunta)
                && this.respuestaSecreta.equalsIgnoreCase(respuesta);
    }

    /**
     * Método toString
     * 
     * Devuelve una representación en texto del usuario.
     * @return Cadena con el nombre de usuario y su rol.
     */
    @Override
    public String toString() {
        return "Usuario: " + nombreUsuario + " | Rol: " + rol;
    }
}
