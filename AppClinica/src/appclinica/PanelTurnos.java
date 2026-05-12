package appclinica;

import com.toedter.calendar.JDateChooser;
import subsistemalogueo.Clinica;
import subsistemalogueo.JsonClinica;
import subsistematurnos.Turno;
import subsistemapacientes.Paciente;
import subsistemarrhh.Medico;
import servicios.TurnoServicios; // <--- Servicio

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PanelTurnos extends JPanel {

    private Clinica clinica;
    private TurnoServicios turnoService; // <--- Servicio

    private JTextField txtHora;
    private JDateChooser txtFecha;
    private JComboBox<Medico> comboMedicos;
    private JComboBox<Paciente> comboPacientes;
    
    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtBuscar;

    public PanelTurnos(TurnoServicios service, Clinica clinica) {
        this.turnoService = service;
        this.clinica = clinica;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // --- 1. SUPERIOR ---
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridLayout(4, 2, 6, 6));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder("Agendar Turno"));

        txtHora = crearCampo();
        comboMedicos = new JComboBox<>();
        comboPacientes = new JComboBox<>();

        for (Medico m : clinica.getMedicos()) comboMedicos.addItem(m);
        for (Paciente p : clinica.getPacientes()) comboPacientes.addItem(p);

        txtFecha = new JDateChooser();
        txtFecha.setDateFormatString("dd/MM/yyyy");
        txtFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtFecha.setMinSelectableDate(new Date()); 

        form.add(new JLabel("Paciente:")); form.add(comboPacientes);
        form.add(new JLabel("Fecha:")); form.add(txtFecha);
        form.add(new JLabel("Hora (HH:MM):")); form.add(txtHora);
        form.add(new JLabel("Médico:")); form.add(comboMedicos);

        // Botones
        JPanel panelBotones = new JPanel(new BorderLayout());
        panelBotones.setBackground(Color.WHITE);
        
        JPanel flujoBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        flujoBotones.setBackground(Color.WHITE);
        JButton btnAgregar = crearBoton("Agendar Turno", new Color(66, 135, 245));
        JButton btnEliminar = crearBoton("Cancelar Turno", new Color(200, 70, 70));
        flujoBotones.add(btnAgregar);
        flujoBotones.add(btnEliminar);

        // Buscador
        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBuscar.setBackground(Color.WHITE);
        JLabel lblLupa = new JLabel("Buscar/Filtrar");
        lblLupa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtBuscar = new JTextField(5); 
        txtBuscar.setPreferredSize(new Dimension(60, 25));
        panelBuscar.add(lblLupa);
        panelBuscar.add(txtBuscar);

        panelBotones.add(flujoBotones, BorderLayout.WEST);
        panelBotones.add(panelBuscar, BorderLayout.EAST);

        panelSuperior.add(form);
        panelSuperior.add(panelBotones);
        add(panelSuperior, BorderLayout.NORTH);

        // --- 2. CENTRO (TABLA) ---
        String[] columnas = {"N°", "Fecha", "Hora", "Paciente", "Médico", "Estado"};
        modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // EVENTOS
        txtBuscar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { filtrar(); }
        });

        btnAgregar.addActionListener(e -> agregarTurno());
        btnEliminar.addActionListener(e -> eliminarTurno());

        actualizarTabla();
    }

    private void filtrar() {
        String texto = txtBuscar.getText();
        if (texto.trim().length() == 0) sorter.setRowFilter(null);
        else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
    }

private void actualizarTabla() {
        modelo.setRowCount(0); // Limpia la tabla visual
        
        // Obtenemos la fecha del sistema una sola vez
        LocalDate hoy = LocalDate.now(); 

        for (Turno t : clinica.getTurnos()) {
            
            // --- FILTRO NUEVO ---
            // Solo mostramos el turno si su fecha es HOY o FUTURA.
            // Los turnos viejos siguen en la base de datos (JSON), pero no ensucian esta vista.
            if (!t.getFecha().isBefore(hoy)) { 
                
                modelo.addRow(new Object[]{
                    t.getNumeroTurno(), 
                    t.getFecha(), 
                    t.getHora(), 
                    t.getPaciente().getNombre() + " " + t.getPaciente().getApellido(),
                    t.getMedico().getNombre() + " " + t.getMedico().getApellido(),
                    t.getEstado()
                });
            }
        }
    }

    private void agregarTurno() {
        String hora = txtHora.getText().trim();
        Medico medico = (Medico) comboMedicos.getSelectedItem();
        Paciente paciente = (Paciente) comboPacientes.getSelectedItem();
        Date fechaDate = txtFecha.getDate();

        if (hora.isEmpty() || medico == null || paciente == null || fechaDate == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            LocalDate fechaLd = fechaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            // LÓGICA DELEGADA AL SERVICIO (Validaciones de fecha y disponibilidad)
            turnoService.registrarTurno(fechaLd, hora, paciente, medico);
            
            JsonClinica.guardarClinica(clinica);
            JOptionPane.showMessageDialog(this, "Turno agendado.");
            limpiarCampos();
            actualizarTabla();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Turno", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarTurno() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un turno.");
            return;
        }
        int filaModelo = tabla.convertRowIndexToModel(fila);
        int numero = (int) modelo.getValueAt(filaModelo, 0);

        boolean eliminado = clinica.getTurnos().removeIf(t -> t.getNumeroTurno() == numero);
        if (eliminado) {
            JsonClinica.guardarClinica(clinica);
            JOptionPane.showMessageDialog(this, "Eliminado.");
            actualizarTabla();
        }
    }

    private void limpiarCampos() {
        txtFecha.setDate(null);
        txtHora.setText("");
        comboMedicos.setSelectedIndex(-1);
        comboPacientes.setSelectedIndex(-1);
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        return campo;
    }

    private JButton crearBoton(String texto, Color colorFondo) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        b.setFocusPainted(false);
        b.setBackground(colorFondo);
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        return b;
    }
}