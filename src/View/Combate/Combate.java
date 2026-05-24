package View.Combate;

import Model.DAO.JugadorDAOImpl;
import Model.Service.PartidaService;

import javax.swing.*;

public class Combate {

    JugadorDAOImpl jugadorDAO = new JugadorDAOImpl();

    public static void main(String[] args) {
        Combate c = new Combate();
        c.iniciarPartida(1, 2);
    }

    public void iniciarPartida(int idJugador1, int idJugador2) {
        // Si ya estamos en el EDT (llamada desde un ActionListener), ejecutamos
        // directamente sin anidar otro invokeLater, lo que evita conflictos de
        // foco y eventos de ratón con la ventana que nos llamó.
        Runnable tarea = () -> {
            Model.Entities.Jugador j1 = jugadorDAO.obtenerJugadorPorId(idJugador1);
            Model.Entities.Jugador j2 = jugadorDAO.obtenerJugadorPorId(idJugador2);

            PartidaService partidaService = new PartidaService();
            try {
                partidaService.iniciarPartida(j1, j2);
            } catch (Model.Exceptions.MazoException e) {
                System.err.println("Error de mazos: " + e.getMessage());
                return;
            }

            Controller.PartidaController controlador = new Controller.PartidaController(partidaService);
            VentanaJuego ventana = new VentanaJuego(j1, j2);
            ventana.setControlador(controlador);
            ventana.setVisible(true);
            controlador.iniciarPartida(ventana);
        };

        if (SwingUtilities.isEventDispatchThread()) {
            tarea.run(); // Ya estamos en el EDT, ejecutamos directo
        } else {
            SwingUtilities.invokeLater(tarea); // Venimos de otro hilo, lo encolamos
        }
    }
}