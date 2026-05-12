package appclinica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import subsistemalogueo.Clinica;
import subsistemalogueo.JsonClinica;
import subsistemalogueo.Roles;
import subsistemalogueo.Usuarios;
import servicios.*; // Importamos los servicios

public class VentanaLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContraseña;
    private JButton btnLogin, btnSalir, btnRecuperar, btnRegistrar;
    
    // DEPENDENCIAS (Modelo y Servicios)
    private Clinica clinica; 
    private UsuarioServicios usuarioService;
    private PacienteServicios pacienteService;
    private TurnoServicios turnoService;
    private EmpleadoServicios empleadoService;
    private HistorialServicios historialService;

    // Constructor que recibe TODAS las dependencias desde el Main
    public VentanaLogin(Clinica clinica, 
                        UsuarioServicios us, 
                        PacienteServicios ps, 
                        TurnoServicios ts, 
                        EmpleadoServicios es, 
                        HistorialServicios hs) {
        
        this.clinica = clinica;
        this.usuarioService = us;
        this.pacienteService = ps;
        this.turnoService = ts;
        this.empleadoService = es;
        this.historialService = hs;

        // Crear Admin por defecto si el sistema está vacío (Usando el Servicio)
        try {
            if (clinica.getUsuarios().isEmpty()) {
                usuarioService.registrarUsuario(new Usuarios("admin", "1234", Roles.ADMIN, "color favorito", "azul"));
                JsonClinica.guardarClinica(clinica);
            }
        } catch (Exception e) {
            // Si ya existe o falla, no pasa nada en el arranque
        }

        initUI();
    }

    private void initUI() {
        setTitle("Inicio de sesión - Sistema de Clínica");
        setSize(800, 500);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // --- LAYOUT ---
        JPanel base = new JPanel(new BorderLayout(10, 10));
        base.setBackground(new Color(245, 250, 255));
        base.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        add(base);

        JLabel lblTitulo = new JLabel("Sistema de Gestión de Clínica", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(30, 50, 90));
        base.add(lblTitulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(new Color(245, 250, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        base.add(centro, BorderLayout.CENTER);

        JLabel lblUsuario = new JLabel("Usuario:");
        JLabel lblPass = new JLabel("Contraseña:");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        txtUsuario = new JTextField(20);
        txtContraseña = new JPasswordField(20);

        gbc.gridx = 0; gbc.gridy = 0; centro.add(lblUsuario, gbc);
        gbc.gridx = 1; centro.add(txtUsuario, gbc);
        gbc.gridx = 0; gbc.gridy = 1; centro.add(lblPass, gbc);
        gbc.gridx = 1; centro.add(txtContraseña, gbc);

        btnLogin = new JButton("Ingresar al sistema");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(220, 40));

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        centro.add(btnLogin, gbc);

        JPanel abajo = new JPanel(new GridLayout(2, 1, 10, 10));
        abajo.setBackground(new Color(245, 250, 255));
        base.add(abajo, BorderLayout.SOUTH);

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        fila1.setBackground(new Color(245, 250, 255));
        btnRecuperar = new JButton("¿Olvidaste tu contraseña?");
        btnRegistrar = new JButton("Registrar nuevo usuario");
        fila1.add(btnRecuperar);
        fila1.add(btnRegistrar);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        fila2.setBackground(new Color(245, 250, 255));
        btnSalir = new JButton("Salir");
        fila2.add(btnSalir);

        abajo.add(fila1);
        abajo.add(fila2);

        // --- LISTENERS ---

        btnLogin.addActionListener((ActionEvent e) -> autenticar());
        
        // Enter en los campos para loguear
        txtUsuario.addActionListener(e -> autenticar());
        txtContraseña.addActionListener(e -> autenticar());

        // BOTÓN SALIR CON CONFIRMACIÓN
        btnSalir.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(
                    this, 
                    "¿Estás seguro de que quieres salir de la aplicación?", 
                    "Salir", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        btnRecuperar.addActionListener(e -> recuperarContraseña());
        btnRegistrar.addActionListener(e -> registrarNuevoUsuario());
    }

    private void autenticar() {
        String u = txtUsuario.getText().trim();
        String p = new String(txtContraseña.getPassword());

        if (u.isEmpty() || p.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete usuario y contraseña.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Delegamos la autenticación al Servicio
            Usuarios usuario = usuarioService.login(u, p);
            
            dispose(); // Cerramos login
            
            // Abrimos la principal inyectando TODOS los servicios
            new VentanaPrincipal(clinica, usuario, pacienteService, turnoService, empleadoService, historialService).setVisible(true);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Acceso", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarNuevoUsuario() {
        JTextField tfUsuario = new JTextField();
        JTextField tfPass = new JTextField();
        JTextField tfPregunta = new JTextField();
        JTextField tfRespuesta = new JTextField();
        
        // --- CAMBIO AQUÍ: Filtramos los roles ---
        // Antes: JComboBox<Roles> cbRoles = new JComboBox<>(Roles.values());
        
        // Ahora: Solo permitimos Médico y Administrativo
        JComboBox<Roles> cbRoles = new JComboBox<>(new Roles[]{
            Roles.MEDICO, 
            Roles.ADMINISTRATIVO
        });
        // ----------------------------------------

        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.add(new JLabel("Usuario:"));
        panel.add(tfUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(tfPass);
        panel.add(new JLabel("Rol:"));
        panel.add(cbRoles);
        panel.add(new JLabel("Pregunta secreta:"));
        panel.add(tfPregunta);
        panel.add(new JLabel("Respuesta:"));
        panel.add(tfRespuesta);

        int r = JOptionPane.showConfirmDialog(this, panel, "Registrar nuevo usuario",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (r != JOptionPane.OK_OPTION) return;

        try {
            Usuarios nuevo = new Usuarios(
                    tfUsuario.getText().trim(),
                    tfPass.getText().trim(),
                    (Roles) cbRoles.getSelectedItem(), // Rol seleccionado (ya filtrado)
                    tfPregunta.getText().trim(),
                    tfRespuesta.getText().trim()
            );

            // Usamos el servicio para registrar
            usuarioService.registrarUsuario(nuevo);
            JsonClinica.guardarClinica(clinica);
            
            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void recuperarContraseña() {
        String usuario = JOptionPane.showInputDialog(this, "Por favor, ingrese su nombre de usuario:");
        if (usuario == null || usuario.trim().isEmpty()) return;

        // Buscamos directamente en la lista (acceso de lectura)
        Usuarios encontrado = clinica.getUsuarios().stream()
                .filter(u -> u.getNombreUsuario().equalsIgnoreCase(usuario))
                .findFirst().orElse(null);

        if (encontrado == null) {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField tfRespuesta = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1, 6, 6));
        panel.add(new JLabel("Hola " + encontrado.getNombreUsuario() + ", responde tu pregunta de seguridad:"));
        
        JLabel lblPregunta = new JLabel("¿" + encontrado.getPreguntaSecreta() + "?");
        lblPregunta.setFont(new Font("Segoe UI", Font.BOLD, 13)); 
        lblPregunta.setForeground(new Color(0, 50, 150));
        
        panel.add(lblPregunta);
        panel.add(new JLabel("Respuesta:"));
        panel.add(tfRespuesta);

        int r = JOptionPane.showConfirmDialog(this, panel, "Recuperar Acceso", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (r == JOptionPane.OK_OPTION) {
            String resp = tfRespuesta.getText().trim();
            if (encontrado.recuperarConPregunta(encontrado.getPreguntaSecreta(), resp)) {
                String nuevaPass = JOptionPane.showInputDialog(this, "¡Correcto! Ingrese su NUEVA contraseña:", "Restablecer", JOptionPane.QUESTION_MESSAGE);
                
                if (nuevaPass != null && !nuevaPass.trim().isEmpty()) {
                    encontrado.setContraseña(nuevaPass); // Se encripta sola
                    JsonClinica.guardarClinica(clinica);
                    JOptionPane.showMessageDialog(this, "Contraseña actualizada. Puede ingresar.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Respuesta incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}