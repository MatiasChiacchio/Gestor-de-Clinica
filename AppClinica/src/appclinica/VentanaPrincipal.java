package appclinica;

import subsistemalogueo.Clinica;
import subsistemalogueo.JsonClinica;
import subsistemalogueo.Roles;
import subsistemalogueo.Usuarios;
import servicios.*; // Importamos los servicios

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Clase VentanaPrincipal (Versión Final - Arquitectura de Capas)
 */
public class VentanaPrincipal extends JFrame {

    private final Clinica clinica;
    private final Usuarios usuarioActual;
    private JPanel panelContenido;

    // Servicios inyectados
    private final PacienteServicios pacienteService;
    private final TurnoServicios turnoService;
    private final EmpleadoServicios empleadoService;
    private final HistorialServicios historialService;

    /**
     * CONSTRUCTOR ACTUALIZADO:
     * Ahora recibe todos los servicios (Service) para pasárselos a los paneles.
     */
    public VentanaPrincipal(Clinica c, Usuarios usuario, 
                            PacienteServicios ps, TurnoServicios ts, 
                            EmpleadoServicios es, HistorialServicios hs) {
        
        this.clinica = c;
        this.usuarioActual = usuario;
        
        // Guardamos las referencias a los servicios
        this.pacienteService = ps;
        this.turnoService = ts;
        this.empleadoService = es;
        this.historialService = hs;

        setTitle("Sistema de Gestión de Clínica - Usuario: " + usuario.getNombreUsuario() + " [" + usuario.getRol() + "]");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        
        // Seguridad al cerrar
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalida();
            }
        });

        // --- LAYOUT ---
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(root);

        JLabel titulo = new JLabel("Panel Principal", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        root.add(titulo, BorderLayout.NORTH);

        // --- MENÚ LATERAL ---
        JPanel menu = new JPanel(new GridLayout(6, 1, 5, 5));
        menu.setPreferredSize(new Dimension(160, 0));
        menu.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JButton bEmp = new JButton("Empleados");
        JButton bPac = new JButton("Pacientes");
        JButton bTur = new JButton("Turnos");
        JButton bHist = new JButton("Historial Médico");
        JButton bSalir = new JButton("Cerrar Sesión");

        Font f = new Font("Segoe UI", Font.PLAIN, 14);
        bEmp.setFont(f); bPac.setFont(f); bTur.setFont(f); bHist.setFont(f); 
        bSalir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bSalir.setForeground(new Color(200, 70, 70));

        menu.add(bEmp);
        menu.add(bPac);
        menu.add(bTur);
        menu.add(bHist);
        menu.add(Box.createVerticalStrut(20));
        menu.add(bSalir);

        root.add(menu, BorderLayout.WEST);

        // --- PANELES CON INYECCIÓN DE SERVICIOS ---
        panelContenido = new JPanel(new CardLayout());
        
        // Aquí pasamos el Servicio correspondiente a cada Panel
        // Asegúrate de haber actualizado también los constructores de PanelEmpleados y PanelHistorial
        panelContenido.add(new PanelEmpleados(empleadoService, clinica), "EMP");
        panelContenido.add(new PanelPacientes(pacienteService, clinica), "PAC");
        panelContenido.add(new PanelTurnos(turnoService, clinica), "TUR");
        panelContenido.add(new PanelHistorial(historialService, clinica), "HIS");

        root.add(panelContenido, BorderLayout.CENTER);

        // --- PERMISOS ---
        aplicarPermisos(bEmp, bPac, bTur, bHist);

        // --- EVENTOS ---
        bEmp.addActionListener(e -> showCard("EMP"));
        bPac.addActionListener(e -> showCard("PAC"));
        bTur.addActionListener(e -> showCard("TUR"));
        bHist.addActionListener(e -> showCard("HIS"));

        bSalir.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(
                    this, 
                    "¿Estás seguro de que quieres cerrar sesión?", 
                    "Cerrar Sesión", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.QUESTION_MESSAGE
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                JsonClinica.guardarClinica(clinica);
                dispose();
                // Al volver al login, necesitamos pasarle los servicios de nuevo o recrearlos
                // Para simplificar, recreamos la ventana de Login que se encargará
                // Nota: Lo ideal sería inyectar también el Login, pero esto funciona bien.
                // new VentanaLogin(...).setVisible(true); <--- Requeriría pasar servicios al login
                // Solución simple: Reiniciar la app o pasar nulls si el login los crea
                // Como VentanaLogin crea sus propios servicios en Main, podemos solo instanciarla:
                
                // OJO: Si VentanaLogin espera argumentos en el constructor, hay que pasarlos.
                // Si usaste el Main que te pasé, VentanaLogin recibe todo.
                // Aquí simplemente cerramos y dejamos que el usuario reinicie o pasamos los mismos:
                new VentanaLogin(clinica, 
                                 new UsuarioServicios(new repositorios.UsuarioRepositorio(clinica.getUsuarios())), 
                                 pacienteService, turnoService, empleadoService, historialService
                                ).setVisible(true);
            }
        });

        // Selección inicial inteligente
        if (bEmp.isVisible()) showCard("EMP");
        else if (bTur.isVisible()) showCard("TUR");
        else if (bPac.isVisible()) showCard("PAC");
        else showCard("HIS");
    }

    /**
     * Oculta botones del menú según el rol del usuario logueado.
     */
    private void aplicarPermisos(JButton bEmp, JButton bPac, JButton bTur, JButton bHist) {
        Roles rol = usuarioActual.getRol();

        // 1. ADMIN: Ve todo (No ocultamos nada)
        if (rol == Roles.ADMIN) {
            return;
        }

        // 2. MÉDICO: Solo ve su Agenda (Turnos) e Historiales.
        if (rol == Roles.MEDICO) {
            bEmp.setVisible(false); // No gestiona RRHH
            bPac.setVisible(false); // No gestiona altas de pacientes (lo hace administración)
        }

        // 3. ADMINISTRATIVO: Solo ve Pacientes y Turnos.
        if (rol == Roles.ADMINISTRATIVO) {
            bHist.setVisible(false); // Privacidad médica (No ve diagnósticos)
            bEmp.setVisible(false);  // CAMBIO AQUÍ: Ya no ve la gestión de empleados
        }
    }

    private void confirmarSalida() {
        Object[] opciones = {"Guardar y Salir", "Salir sin Guardar", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(this,"¿Desea guardar los cambios antes de salir?","Cerrar Sistema",
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

        if (seleccion == 0) {
            JsonClinica.guardarClinica(clinica);
            System.exit(0);
        } else if (seleccion == 1) {
            System.exit(0);
        }
    }

    private void showCard(String name) {
        CardLayout cl = (CardLayout) panelContenido.getLayout();
        cl.show(panelContenido, name);
    }
}