package appclinica;

import com.toedter.calendar.JDateChooser;
import subsistemalogueo.Clinica;
import subsistemalogueo.JsonClinica;
import subsistemapacientes.ObraSocial;
import subsistemapacientes.Paciente;
import servicios.PacienteServicios;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PanelPacientes extends JPanel {

    private Clinica clinica;
    private PacienteServicios pacienteService;
    
    private JTextField txtNombre, txtApellido, txtDni;
    private JDateChooser txtFechaNac; // <--- CAMBIO: Calendario para nacimiento
    private JComboBox<ObraSocial> cbObra;
    
    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtBuscar;

    public PanelPacientes(PacienteServicios service, Clinica clinica) {
        this.pacienteService = service;
        this.clinica = clinica;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // --- SUPERIOR ---
        JPanel panelContenedorSuperior = new JPanel();
        panelContenedorSuperior.setLayout(new BoxLayout(panelContenedorSuperior, BoxLayout.Y_AXIS));
        panelContenedorSuperior.setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder("Datos del Paciente"));

        txtNombre = crearCampo();
        txtApellido = crearCampo();
        txtDni = crearCampo();
        
        // Configuración Fecha Nacimiento
        txtFechaNac = new JDateChooser();
        txtFechaNac.setDateFormatString("dd/MM/yyyy");
        txtFechaNac.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtFechaNac.setMaxSelectableDate(new Date()); // No pueden nacer en el futuro

        cbObra = new JComboBox<>(ObraSocial.values());
        cbObra.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        form.add(new JLabel("Apellido:")); form.add(txtApellido);
        form.add(new JLabel("DNI:")); form.add(txtDni);
        form.add(new JLabel("Fecha de Nacimiento:")); form.add(txtFechaNac); // Agregamos calendario
        form.add(new JLabel("Obra Social:")); form.add(cbObra);
        
        // --- BOTONES ---
        JPanel panelBotones = new JPanel(new BorderLayout());
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel flujoBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        flujoBotones.setBackground(Color.WHITE);

        JButton btnAgregar = crearBoton("Agregar", new Color(66, 135, 245));
        JButton btnEliminar = crearBoton("Eliminar", new Color(200, 70, 70));

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

        panelContenedorSuperior.add(form);
        panelContenedorSuperior.add(panelBotones);
        add(panelContenedorSuperior, BorderLayout.NORTH);

        // --- TABLA ---
        // Seguimos mostrando "Edad" en la tabla porque es más útil leer "30 años" que "12/05/1994"
        String[] columnas = {"DNI", "Nombre", "Apellido", "Edad", "Obra Social"};
        
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
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
        txtDni.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });

        btnAgregar.addActionListener(e -> agregarPaciente());
        btnEliminar.addActionListener(e -> eliminarPaciente());

        actualizarTabla();
    }

    private void filtrar() {
        String texto = txtBuscar.getText();
        if (texto.trim().length() == 0) sorter.setRowFilter(null);
        else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
    }

    private void actualizarTabla() {
        modelo.setRowCount(0);
        for (Paciente p : clinica.getPacientes()) {
            modelo.addRow(new Object[]{
                p.getDni(), 
                p.getNombre(), 
                p.getApellido(), 
                p.getEdad(), // <--- Esto llama al método mágico que calcula la edad
                p.getObraSocial()
            });
        }
    }

    private void agregarPaciente() {
        String nombre = txtNombre.getText().trim();
        String apellido = txtApellido.getText().trim();
        String dni = txtDni.getText().trim();
        Date fechaDate = txtFechaNac.getDate(); // Obtenemos fecha del calendario
        ObraSocial obra = (ObraSocial) cbObra.getSelectedItem();

        if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || fechaDate == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!dni.matches("\\d{7,8}")) {
            JOptionPane.showMessageDialog(this, "DNI inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Conversión Date -> LocalDate
            LocalDate fechaNac = fechaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            // Validación lógica básica (ej: no nacer hoy mismo)
            if (fechaNac.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Fecha inválida (Futuro).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int nroHistoria = clinica.getPacientes().size() + 1;
            
            // Creamos con la FECHA, no la edad
            Paciente nuevo = new Paciente(nombre, apellido, dni, fechaNac, nroHistoria, obra);
            
            pacienteService.registrarPaciente(nuevo);
            JsonClinica.guardarClinica(clinica);

            JOptionPane.showMessageDialog(this, "Paciente agregado.");
            limpiarCampos();
            actualizarTabla();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPaciente() {
        int fila = tabla.getSelectedRow();
        String dni;
        if (fila != -1) {
            int filaModelo = tabla.convertRowIndexToModel(fila);
            dni = (String) modelo.getValueAt(filaModelo, 0); 
        } else {
            dni = JOptionPane.showInputDialog(this, "Ingrese el DNI a eliminar:");
        }

        if (dni == null || dni.isEmpty()) return;

        try {
            pacienteService.eliminarPaciente(dni);
            JsonClinica.guardarClinica(clinica);
            JOptionPane.showMessageDialog(this, "Paciente eliminado.");
            actualizarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtFechaNac.setDate(null);
        cbObra.setSelectedIndex(0);
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), BorderFactory.createEmptyBorder(3, 6, 3, 6)));
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