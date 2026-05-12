package appclinica;

import subsistemalogueo.Clinica;
import subsistemalogueo.JsonClinica;
import subsistemahistorial.EntradaHistorial;
import subsistemahistorial.HistorialMedico;
import subsistemarrhh.Medico;
import subsistemapacientes.Paciente;
import servicios.HistorialServicios;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

/**
 * Clase PanelHistorial (Corregida)
 * Compatible con EntradaHistorial usando LocalDate.
 */
public class PanelHistorial extends JPanel {

    private Clinica clinica;
    private HistorialServicios historialService;

    private JComboBox<Paciente> comboPacientes;
    private JComboBox<Medico> comboMedicos;
    private JTextField txtDiagnostico;
    private JTextArea txtObservaciones;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PanelHistorial(HistorialServicios service, Clinica clinica) {
        this.historialService = service;
        this.clinica = clinica;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // --- SUPERIOR ---
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridLayout(5, 2, 6, 6));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder("Nueva Entrada Histórica"));

        comboPacientes = new JComboBox<>();
        comboMedicos = new JComboBox<>();
        
        // Cargar combos usando las listas de la clínica
        for (Paciente p : clinica.getPacientes()) comboPacientes.addItem(p);
        // Usamos el helper getMedicos() que re-agregamos a Clinica
        for (Medico m : clinica.getMedicos()) comboMedicos.addItem(m);

        txtDiagnostico = crearCampo();
        txtObservaciones = new JTextArea(3, 15);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        form.add(new JLabel("Paciente:")); form.add(comboPacientes);
        form.add(new JLabel("Médico:")); form.add(comboMedicos);
        form.add(new JLabel("Diagnóstico:")); form.add(txtDiagnostico);
        form.add(new JLabel("Observaciones:")); form.add(new JScrollPane(txtObservaciones));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botones.setBackground(Color.WHITE);
        JButton btnAgregar = crearBoton("Agregar Entrada", new Color(66, 135, 245));
        JButton btnVer = crearBoton("Ver Historial del Paciente", new Color(100, 160, 80));
        botones.add(btnAgregar);
        botones.add(btnVer);

        panelSuperior.add(form);
        panelSuperior.add(botones);
        add(panelSuperior, BorderLayout.NORTH);

        // --- CENTRO (TABLA) ---
        String[] columnas = {"Fecha", "Médico", "Diagnóstico", "Observaciones"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // EVENTOS
        btnAgregar.addActionListener(e -> agregarEntrada());
        btnVer.addActionListener(e -> mostrarHistorialEspecifico());
    }

    private void agregarEntrada() {
        Paciente p = (Paciente) comboPacientes.getSelectedItem();
        Medico m = (Medico) comboMedicos.getSelectedItem();
        String diag = txtDiagnostico.getText().trim();
        String obs = txtObservaciones.getText().trim();

        if (p == null || m == null || diag.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Faltan datos obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // --- CORRECCIÓN AQUÍ: Pasamos LocalDate.now() sin .toString() ---
            EntradaHistorial entrada = new EntradaHistorial(LocalDate.now(), diag, obs, m);
            
            // Delegamos al servicio
            historialService.agregarEntrada(p, entrada);
            
            JsonClinica.guardarClinica(clinica);
            JOptionPane.showMessageDialog(this, "Entrada guardada correctamente.");
            
            txtDiagnostico.setText("");
            txtObservaciones.setText("");
            mostrarHistorialEspecifico(); // Refrescar tabla

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al guardar: " + e.getMessage());
        }
    }

    private void mostrarHistorialEspecifico() {
        Paciente p = (Paciente) comboPacientes.getSelectedItem();
        modelo.setRowCount(0); // Limpiar tabla
        if (p == null) return;

        HistorialMedico h = clinica.getHistoriales().stream()
                .filter(hm -> hm.getPaciente().getDni().equals(p.getDni()))
                .findFirst().orElse(null);

        if (h != null) {
            for (EntradaHistorial e : h.getEntradas()) {
                modelo.addRow(new Object[]{
                    e.getFecha(), // La tabla llama a toString() automáticamente, así que LocalDate se ve bien
                    e.getMedico().getApellido(), 
                    e.getDiagnostico(), 
                    e.getObservaciones()
                });
            }
        }
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                BorderFactory.createEmptyBorder(4, 6, 4, 6))
        );
        return campo;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setFocusPainted(false);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        return b;
    }
}