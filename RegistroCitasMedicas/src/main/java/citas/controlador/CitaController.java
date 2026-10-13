package citas.controlador;

import citas.modelo.Cita;
import citas.modelo.Medico;
import citas.modelo.Paciente;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Aquí busco a los pacientes y guardo sus citas.
public class CitaController {
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Medico> medicos = new ArrayList<>();
    private List<Cita> citas = new ArrayList<>();

    public CitaController() {
        // Agregué estos datos de ejemplo para probar el programa.
        pacientes.add(new Paciente("12345678901", "Juan Pérez López", 24));
        pacientes.add(new Paciente("98765432109", "María García Ruiz", 30));
        pacientes.add(new Paciente("11223344556", "Luis Martínez Soto", 19));

        medicos.add(new Medico("Dr. Carlos López", "Medicina general", "102", "Lunes a viernes", "09:00 a 13:00", false));
        medicos.add(new Medico("Dra. Ana García", "Pediatría", "104", "Lunes a viernes", "09:00 a 13:00", false));
        medicos.add(new Medico("Dr. Luis Martínez", "Dermatología", "201", "Lunes a sábado", "09:00 a 13:00", true));
        medicos.add(new Medico("Dra. Laura Torres", "Medicina familiar", "203", "Lunes a viernes", "09:00 a 13:00", false));
    }

    public Paciente buscarPaciente(String nss) {
        for (Paciente p : pacientes) {
            if (p.getNss().equals(nss.trim())) return p;
        }
        return null;
    }

    public List<Medico> getMedicos() { return medicos; }

    // Antes de registrar una cita reviso que el horario esté disponible.
    public boolean registrarCita(Paciente paciente, Medico medico, LocalDate fecha, String hora) {
        if (paciente == null || medico == null || fecha == null || hora == null) return false;
        if (fecha.isBefore(LocalDate.now()) || !medico.atiende(fecha)) return false;
        if (!hora.equals("09:00") && !hora.equals("10:00") &&
            !hora.equals("11:00") && !hora.equals("12:00")) return false;
        for (Cita c : citas) {
            if (c.getMedico() == medico && c.getFecha().equals(fecha) && c.getHora().equals(hora)) return false;
            if (c.getPaciente().getNss().equals(paciente.getNss()) && c.getFecha().equals(fecha) && c.getHora().equals(hora)) return false;
        }
        citas.add(new Cita(paciente, medico, fecha, hora));
        return true;
    }

    public Cita getUltimaCita() {
        if (citas.isEmpty()) return null;
        return citas.get(citas.size() - 1);
    }
}
