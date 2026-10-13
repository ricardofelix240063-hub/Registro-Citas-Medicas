package citas;

import citas.controlador.CitaController;
import citas.modelo.Medico;
import citas.modelo.Paciente;
import java.time.LocalDate;

// Estas pruebas revisan las búsquedas y el registro de citas.
public class Pruebas {
    private static int correctas = 0;

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError("FALLÓ: " + mensaje);
        correctas++;
        System.out.println("OK: " + mensaje);
    }

    public static void main(String[] args) {
        CitaController sistema = new CitaController();
        Paciente juan = sistema.buscarPaciente("12345678901");
        comprobar(juan != null, "Buscar NSS registrado");
        comprobar(sistema.buscarPaciente("00000000000") == null, "Rechazar NSS inexistente");
        comprobar(sistema.buscarPaciente(" 98765432109 ") != null, "Buscar NSS con espacios");
        comprobar(sistema.getMedicos().size() == 4, "Mostrar cuatro médicos");
        Medico medico = sistema.getMedicos().get(0);
        LocalDate fecha = LocalDate.now().plusDays(1);
        while (!medico.atiende(fecha)) fecha = fecha.plusDays(1);
        comprobar(sistema.registrarCita(juan, medico, fecha, "10:00"), "Registrar cita válida");
        comprobar(sistema.getUltimaCita().getMedico() == medico, "Guardar médico de la cita");
        comprobar(sistema.getUltimaCita().getPaciente() == juan, "Guardar paciente de la cita");
        comprobar(!sistema.registrarCita(juan, medico, fecha, "10:00"), "Evitar cita duplicada");
        comprobar(!sistema.registrarCita(null, medico, fecha, "11:00"), "Evitar cita sin paciente");
        comprobar(!sistema.registrarCita(juan, medico, fecha.minusYears(1), "11:00"), "Rechazar fechas pasadas");
        comprobar(!sistema.registrarCita(juan, medico, fecha, "22:00"), "Rechazar hora fuera de horario");
        LocalDate domingo = fecha;
        while (domingo.getDayOfWeek().getValue() != 7) domingo = domingo.plusDays(1);
        comprobar(!sistema.registrarCita(juan, medico, domingo, "11:00"), "Rechazar domingo sin consulta");
        Paciente maria = sistema.buscarPaciente("98765432109");
        comprobar(!sistema.registrarCita(maria, medico, fecha, "10:00"), "Evitar dos pacientes con mismo médico y horario");
        comprobar(sistema.registrarCita(maria, medico, fecha, "11:00"), "Aceptar otro horario libre");
        System.out.println("PRUEBAS CORRECTAS: " + correctas + "/" + correctas);
    }
}
