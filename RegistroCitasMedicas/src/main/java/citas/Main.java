package citas;

import citas.controlador.CitaController;
import citas.vista.CitaView;
import javax.swing.SwingUtilities;

// Aquí comienza el programa y se abre la ventana.
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CitaView ventana = new CitaView(new CitaController());
            ventana.setVisible(true);
        });
    }
}
