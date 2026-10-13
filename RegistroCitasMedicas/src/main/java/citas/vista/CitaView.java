package citas.vista;

import citas.controlador.CitaController;
import citas.modelo.Cita;
import citas.modelo.Medico;
import citas.modelo.Paciente;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// Esta es la ventana donde el paciente busca y aparta su cita.
public class CitaView extends JFrame {
    private CitaController controlador;
    private Paciente pacienteActual;
    private JTextField campoNss = new JTextField(14);
    private JLabel datosPaciente = new JLabel("Escriba el NSS y presione Buscar.");
    private DefaultListModel<Medico> modeloLista = new DefaultListModel<>();
    private JList<Medico> listaMedicos = new JList<>(modeloLista);
    private JPanel panelDerecho = new JPanel(new BorderLayout());
    private DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CitaView(CitaController controlador) {
        this.controlador = controlador;
        setTitle("Registro de citas médicas");
        setSize(740, 470);
        setMinimumSize(new Dimension(650, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        crearPantalla();
    }

    private void crearPantalla() {
        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(principal);

        JLabel titulo = new JLabel("REGISTRO DE CITAS MÉDICAS", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(17f));

        JPanel busqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        busqueda.add(new JLabel("Número de Seguridad Social:"));
        busqueda.add(campoNss);
        JButton botonBuscar = new JButton("Buscar");
        busqueda.add(botonBuscar);

        JPanel cabecera = new JPanel(new BorderLayout(0, 8));
        cabecera.add(titulo, BorderLayout.NORTH);
        cabecera.add(busqueda, BorderLayout.CENTER);
        cabecera.add(datosPaciente, BorderLayout.SOUTH);
        principal.add(cabecera, BorderLayout.NORTH);

        JPanel izquierda = new JPanel(new BorderLayout(5, 5));
        izquierda.setBorder(BorderFactory.createTitledBorder("Médicos disponibles"));
        izquierda.add(new JScrollPane(listaMedicos), BorderLayout.CENTER);
        izquierda.setPreferredSize(new Dimension(240, 250));

        panelDerecho.setBorder(BorderFactory.createTitledBorder("Información"));
        JPanel contenido = new JPanel(new GridLayout(1, 2, 10, 0));
        contenido.add(izquierda);
        contenido.add(panelDerecho);
        principal.add(contenido, BorderLayout.CENTER);

        // Estos eventos responden al botón Buscar y a la lista de médicos.
        botonBuscar.addActionListener(e -> buscar());
        campoNss.addActionListener(e -> buscar());
        listaMedicos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) mostrarMedico();
        });
        mostrarMensaje("Seleccione primero un paciente.");
    }

    private void buscar() {
        pacienteActual = controlador.buscarPaciente(campoNss.getText());
        modeloLista.clear();
        if (pacienteActual == null) {
            datosPaciente.setText("Paciente no encontrado. Revise el NSS.");
            mostrarMensaje("No se encontraron datos del paciente.");
            return;
        }
        datosPaciente.setText("Paciente: " + pacienteActual.getNombre() +
                "    |    Edad: " + pacienteActual.getEdad() + "    |    NSS: " + pacienteActual.getNss());
        for (Medico medico : controlador.getMedicos()) modeloLista.addElement(medico);
        mostrarMensaje("Seleccione un médico de la lista.");
    }

    private void mostrarMensaje(String mensaje) {
        panelDerecho.removeAll();
        panelDerecho.add(new JLabel(mensaje, SwingConstants.CENTER), BorderLayout.CENTER);
        panelDerecho.revalidate();
        panelDerecho.repaint();
    }

    private void mostrarMedico() {
        Medico medico = listaMedicos.getSelectedValue();
        if (pacienteActual == null || medico == null) return;

        panelDerecho.removeAll();
        JPanel informacion = new JPanel(new GridLayout(0, 1, 5, 5));
        informacion.add(new JLabel("Médico: " + medico.getNombre()));
        informacion.add(new JLabel("Especialidad: " + medico.getEspecialidad()));
        informacion.add(new JLabel("Consultorio: " + medico.getConsultorio()));
        informacion.add(new JLabel("Días: " + medico.getDias()));
        informacion.add(new JLabel("Horario: " + medico.getHorario()));

        JComboBox<LocalDate> fechas = new JComboBox<>();
        for (int i = 0; i < 15; i++) {
            LocalDate fecha = LocalDate.now().plusDays(i);
            if (medico.atiende(fecha)) fechas.addItem(fecha);
        }
        fechas.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> lista, Object valor,
                    int indice, boolean seleccionado, boolean tieneFoco) {
                super.getListCellRendererComponent(lista, valor, indice, seleccionado, tieneFoco);
                if (valor instanceof LocalDate) setText(((LocalDate) valor).format(formato));
                return this;
            }
        });
        JComboBox<String> horas = new JComboBox<>(new String[]{"09:00", "10:00", "11:00", "12:00"});
        informacion.add(new JLabel("Fecha de la cita:"));
        informacion.add(fechas);
        informacion.add(new JLabel("Hora de la cita:"));
        informacion.add(horas);

        JButton apartar = new JButton("Apartar cita");
        apartar.addActionListener(e -> {
            LocalDate fecha = (LocalDate) fechas.getSelectedItem();
            String hora = (String) horas.getSelectedItem();
            if (controlador.registrarCita(pacienteActual, medico, fecha, hora)) {
                mostrarCita(controlador.getUltimaCita());
            } else {
                JOptionPane.showMessageDialog(this,
                        "Ese horario ya está ocupado o no está disponible. Elija otro.");
            }
        });
        panelDerecho.add(informacion, BorderLayout.CENTER);
        panelDerecho.add(apartar, BorderLayout.SOUTH);
        panelDerecho.revalidate();
        panelDerecho.repaint();
    }

    private void mostrarCita(Cita cita) {
        // Aquí cambio la información del médico por los datos de la cita.
        panelDerecho.removeAll();
        JPanel detalle = new JPanel(new GridLayout(0, 1, 5, 8));
        detalle.add(new JLabel("¡Cita registrada correctamente!"));
        detalle.add(new JLabel("Paciente: " + cita.getPaciente().getNombre()));
        detalle.add(new JLabel("NSS: " + cita.getPaciente().getNss()));
        detalle.add(new JLabel("Médico: " + cita.getMedico().getNombre()));
        detalle.add(new JLabel("Consultorio: " + cita.getMedico().getConsultorio()));
        detalle.add(new JLabel("Fecha: " + cita.getFecha().format(formato)));
        detalle.add(new JLabel("Hora: " + cita.getHora()));
        panelDerecho.add(detalle, BorderLayout.NORTH);
        panelDerecho.revalidate();
        panelDerecho.repaint();
    }
}
