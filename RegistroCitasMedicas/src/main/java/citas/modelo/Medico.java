package citas.modelo;

import java.time.DayOfWeek;
import java.time.LocalDate;

// Aquí están los datos y días de atención de los médicos.
public class Medico {
    private String nombre;
    private String especialidad;
    private String consultorio;
    private String dias;
    private String horario;
    private boolean atiendeSabado;

    public Medico(String nombre, String especialidad, String consultorio,
                  String dias, String horario, boolean atiendeSabado) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.consultorio = consultorio;
        this.dias = dias;
        this.horario = horario;
        this.atiendeSabado = atiendeSabado;
    }

    public String getNombre() { return nombre; }
    public String getEspecialidad() { return especialidad; }
    public String getConsultorio() { return consultorio; }
    public String getDias() { return dias; }
    public String getHorario() { return horario; }

    public boolean atiende(LocalDate fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia != DayOfWeek.SUNDAY && (atiendeSabado || dia != DayOfWeek.SATURDAY);
    }

    @Override
    public String toString() { return nombre; }
}
