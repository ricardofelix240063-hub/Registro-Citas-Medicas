package citas.modelo;

// Guardo los datos básicos de cada paciente.
public class Paciente {
    private String nss;
    private String nombre;
    private int edad;

    public Paciente(String nss, String nombre, int edad) {
        this.nss = nss;
        this.nombre = nombre;
        this.edad = edad;
    }

    public String getNss() { return nss; }
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
}
