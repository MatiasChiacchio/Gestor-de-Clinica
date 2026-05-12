package appclinica;

import com.toedter.calendar.JDateChooser;
import subsistemalogueo.Clinica;
import subsistemalogueo.JsonClinica;
import subsistemarrhh.Administrativo;
import subsistemarrhh.Empleado;
import subsistemarrhh.Especialidad;
import subsistemarrhh.Medico;
import subsistemarrhh.Sector;
import servicios.EmpleadoServicios; 

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class PanelEmpleados extends JPanel {

    private Clinica clinica;
    private EmpleadoServicios empleadoService;

    // Campos de Texto y Calendarios
    private JTextField txtNombre, txtApellido, txtDni;
    private JDateChooser txtFechaNac, txtFechaInicio; // Dos calendarios
    private JComboBox<String> cbTipo;
    private JComboBox<Especialidad> cbEspecialidad;
    private JComboBox<Sector> cbSector;
    
    // Tabla y Buscador
    private JTable tabla;
    private DefaultTableModel modelo;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtBuscar;
    
    private Empleado empleadoSeleccionado = null;

    public PanelEmpleados(EmpleadoServicios service, Clinica clinica) {
        this.empleadoService = service;
        this.clinica = clinica;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);

        // === 1. CONTENEDOR SUPERIOR (Formulario + Botones) ===
        JPanel panelContenedorSuperior = new JPanel();
        panelContenedorSuperior.setLayout(new BoxLayout(panelContenedorSuperior, BoxLayout.Y_AXIS));
        panelContenedorSuperior.setBackground(Color.WHITE);

        // --- FORMULARIO ---
        // Aumentamos filas para acomodar los dos calendarios
        JPanel form = new JPanel(new GridLayout(8, 2, 6, 6)); 
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder("Datos del Empleado"));

        cbTipo = new JComboBox<>(new String[]{"Administrativo", "Médico"});
        cbEspecialidad = new JComboBox<>(Especialidad.values());
        cbSector = new JComboBox<>(Sector.values());

        txtNombre = crearCampo();
        txtApellido = crearCampo();
        txtDni = crearCampo();
        
        // Configuración de Calendarios
        txtFechaNac = new JDateChooser();
        txtFechaNac.setDateFormatString("dd/MM/yyyy");
        txtFechaNac.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtFechaNac.setMaxSelectableDate(new Date()); // No puede nacer en el futuro

        txtFechaInicio = new JDateChooser();
        txtFechaInicio.setDateFormatString("dd/MM/yyyy");
        txtFechaInicio.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        // Agregado de componentes
        form.add(new JLabel("Tipo:")); form.add(cbTipo);
        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        form.add(new JLabel("Apellido:")); form.add(txtApellido);
        form.add(new JLabel("DNI:")); form.add(txtDni);
        form.add(new JLabel("Fecha de Nacimiento:")); form.add(txtFechaNac); // Nuevo campo
        form.add(new JLabel("Fecha Inicio Laboral:")); form.add(txtFechaInicio);
        form.add(new JLabel("Sector/Especialidad:")); form.add(cbSector); 

        // --- BOTONES Y BUSCADOR ---
        JPanel panelBotones = new JPanel(new BorderLayout());
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel flujoBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        flujoBotones.setBackground(Color.WHITE);

        JButton btnAgregar = crearBoton("Guardar", new Color(66, 135, 245));
        JButton btnEditar = crearBoton("Editar", new Color(255, 165, 0));
        JButton btnEliminar = crearBoton("Eliminar", new Color(200, 70, 70));

        flujoBotones.add(btnAgregar);
        flujoBotones.add(btnEditar);
        flujoBotones.add(btnEliminar);

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

        // === 2. CENTRO (TABLA) ===
        // Mostramos la EDAD calculada automáticamente
        String[] columnas = {"DNI", "Nombre", "Apellido", "Edad", "Fecha Inicio", "Tipo", "Sector/Esp"};
        
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

        // === EVENTOS ===
        cbTipo.addActionListener(e -> alternarCombos(form));
        
        txtBuscar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { filtrar(); }
        });
        
        txtDni.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) e.consume();
            }
        });

        btnAgregar.addActionListener(e -> agregarOModificarEmpleado());
        btnEliminar.addActionListener(e -> eliminarEmpleado());
        btnEditar.addActionListener(e -> cargarParaEditar());

        alternarCombos(form);
        actualizarTabla();
    }

    private void filtrar() {
        String texto = txtBuscar.getText();
        if (texto.trim().length() == 0) sorter.setRowFilter(null);
        else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
    }

    private void actualizarTabla() {
        modelo.setRowCount(0);
        for (Empleado e : clinica.getEmpleados()) {
            String tipo = (e instanceof Medico) ? "Médico" : "Administrativo";
            String detalle = (e instanceof Medico) ? ((Medico) e).getEspecialidad().toString() : ((Administrativo) e).getSector().toString();
            
            // Usamos e.getEdad() que ahora calcula los años basado en la fecha de nacimiento
            modelo.addRow(new Object[]{
                e.getDni(), e.getNombre(), e.getApellido(),
                e.getEdad() + " años", // <--- Calculado automáticamente
                e.getFechaInicio(), 
                tipo, 
                detalle
            });
        }
    }

    private void agregarOModificarEmpleado() {
        try {
            String tipoSeleccionado = (String) cbTipo.getSelectedItem();
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String dni = txtDni.getText().trim();
            Date dateNac = txtFechaNac.getDate();
            Date dateIni = txtFechaInicio.getDate();
            
            if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() || dateNac == null || dateIni == null) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!dni.matches("\\d{7,8}")) {
                JOptionPane.showMessageDialog(this, "DNI inválido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Conversiones de Fecha
            LocalDate fechaNac = dateNac.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            // Fecha Inicio sigue como String en tu modelo Empleado, así que formateamos
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaInicioStr = sdf.format(dateIni);

            // --- MODIFICACIÓN ---
            if (empleadoSeleccionado != null) {
                boolean eraMedico = empleadoSeleccionado instanceof Medico;
                boolean quiereSerMedico = tipoSeleccionado.equals("Médico");

                if (eraMedico != quiereSerMedico) {
                    // Cambio de Rol: Borrar y Recrear
                    clinica.getEmpleados().remove(empleadoSeleccionado);
                    if (quiereSerMedico) {
                        Especialidad esp = (Especialidad) cbEspecialidad.getSelectedItem();
                        empleadoService.registrarEmpleado(new Medico(nombre, apellido, dni, fechaNac, fechaInicioStr, esp));
                    } else {
                        Sector sec = (Sector) cbSector.getSelectedItem();
                        empleadoService.registrarEmpleado(new Administrativo(nombre, apellido, dni, fechaNac, fechaInicioStr, sec));
                    }
                    JOptionPane.showMessageDialog(this, "Rol actualizado.");
                } else {
                    // Actualización Normal
                    empleadoSeleccionado.setNombre(nombre);
                    empleadoSeleccionado.setApellido(apellido);
                    empleadoSeleccionado.setFechaNacimiento(fechaNac); // Actualizamos fecha nac
                    empleadoSeleccionado.setFechaInicio(fechaInicioStr);
                    // empleadoSeleccionado.setDni(dni);

                    if (empleadoSeleccionado instanceof Medico) {
                        ((Medico) empleadoSeleccionado).setEspecialidad((Especialidad) cbEspecialidad.getSelectedItem());
                    } else {
                        ((Administrativo) empleadoSeleccionado).setSector((Sector) cbSector.getSelectedItem());
                    }
                    JOptionPane.showMessageDialog(this, "Datos actualizados.");
                }
                
                JsonClinica.guardarClinica(clinica);
                empleadoSeleccionado = null;
                limpiarCampos();
                actualizarTabla();
                return;
            }

            // --- ALTA NUEVA (Usando Servicio) ---
            if (tipoSeleccionado.equals("Administrativo")) {
                Sector sector = (Sector) cbSector.getSelectedItem();
                empleadoService.registrarEmpleado(new Administrativo(nombre, apellido, dni, fechaNac, fechaInicioStr, sector));
            } else {
                Especialidad esp = (Especialidad) cbEspecialidad.getSelectedItem();
                empleadoService.registrarEmpleado(new Medico(nombre, apellido, dni, fechaNac, fechaInicioStr, esp));
            }

            JsonClinica.guardarClinica(clinica);
            JOptionPane.showMessageDialog(this, "Agregado correctamente.");
            limpiarCampos();
            actualizarTabla();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEmpleado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila.");
            return;
        }
        int filaModelo = tabla.convertRowIndexToModel(fila);
        String dni = (String) modelo.getValueAt(filaModelo, 0);

        try {
            empleadoService.eliminarEmpleado(dni);
            JsonClinica.guardarClinica(clinica);
            JOptionPane.showMessageDialog(this, "Eliminado.");
            actualizarTabla();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarParaEditar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una fila.");
            return;
        }
        int filaModelo = tabla.convertRowIndexToModel(fila);
        String dni = (String) modelo.getValueAt(filaModelo, 0);

        Empleado e = empleadoService.buscarPorDni(dni);
        if (e == null) return;

        empleadoSeleccionado = e;
        txtNombre.setText(e.getNombre());
        txtApellido.setText(e.getApellido());
        txtDni.setText(e.getDni());
        
        // Cargar fechas a los calendarios
        try {
            if (e.getFechaNacimiento() != null) {
                Date fechaNac = Date.from(e.getFechaNacimiento().atStartOfDay(ZoneId.systemDefault()).toInstant());
                txtFechaNac.setDate(fechaNac);
            }
            
            Date fechaIni = new SimpleDateFormat("dd/MM/yyyy").parse(e.getFechaInicio());
            txtFechaInicio.setDate(fechaIni);
        } catch (Exception ex) {}

        if (e instanceof Medico) {
            cbTipo.setSelectedItem("Médico");
            cbEspecialidad.setSelectedItem(((Medico) e).getEspecialidad());
        } else {
            cbTipo.setSelectedItem("Administrativo");
            cbSector.setSelectedItem(((Administrativo) e).getSector());
        }
        JOptionPane.showMessageDialog(this, "Modifique y presione 'Guardar'.");
    }

    private void limpiarCampos() {
        txtNombre.setText(""); txtApellido.setText(""); txtDni.setText("");
        txtFechaNac.setDate(null); txtFechaInicio.setDate(null);
        cbTipo.setSelectedIndex(0); cbSector.setSelectedIndex(0); cbEspecialidad.setSelectedIndex(0);
        empleadoSeleccionado = null;
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), BorderFactory.createEmptyBorder(3, 6, 3, 6)));
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

    private void alternarCombos(JPanel form) {
        try { form.remove(13); } catch (Exception e) {}
        if (cbTipo.getSelectedItem().equals("Administrativo")) form.add(cbSector, 13);
        else form.add(cbEspecialidad, 13);
        form.revalidate();
        form.repaint();
    }
}