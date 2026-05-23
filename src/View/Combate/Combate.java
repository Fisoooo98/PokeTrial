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
        SwingUtilities.invokeLater(() -> {

            Model.Entities.Jugador j1 = jugadorDAO.obtenerJugadorPorId(idJugador1);
            Model.Entities.Jugador j2 = jugadorDAO.obtenerJugadorPorId(idJugador2);

            // 1. Inicializamos el service (carga mazos y tablero lógico)
            PartidaService partidaService = new PartidaService();
            try {
                partidaService.iniciarPartida(j1, j2);
            } catch (Model.Exceptions.MazoException e) {
                System.err.println("Error de mazos: " + e.getMessage());
                return;
            }

            // 2. Creamos controlador y ventana
            Controller.PartidaController controlador = new Controller.PartidaController(partidaService);
            VentanaJuego ventana = new VentanaJuego(j1, j2);
            ventana.setControlador(controlador);

            // 3. Hacemos la ventana visible ANTES de iniciarPartida
            //    para que Swing construya todos los paneles físicamente
            ventana.setVisible(true);

            // 4. Ahora sí arrancamos la partida:
            //    actualizarManoVisual() leerá getMazoJ1/J2() del service (fuente única de verdad)
            //    y el Timer de la IA encontrará los componentes ya renderizados
            controlador.iniciarPartida(ventana);
        });
    }
}