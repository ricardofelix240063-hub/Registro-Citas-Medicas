package citas.modelo;

import java.time.LocalDate;

// Esta clase guarda los datos de una cita.
public class Cita {
    private Paciente paciente;
    private Medico medico;
    private LocalDate fecha;
    private String hora;

    public Cita(Paciente paciente, Medico medico, LocalDate fecha, String hora) {
        this.paciente = paciente;
        this.medico = medico;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Paciente getPaciente() { return paciente; }
    public Medico getMedico() { return medico; }
    public LocalDate getFecha() { return fecha; }
    public String getHora() { return hora; }
}
