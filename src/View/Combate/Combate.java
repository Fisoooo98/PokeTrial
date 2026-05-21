package View.Combate;

import Model.DAO.InventarioDAOImpl;
import Model.DAO.JugadorDAOImpl;
import Model.Entities.Carta;
import Model.Service.PartidaService;

import javax.swing.*;
import java.util.List;

public class Combate {
    // Tus DAOs e instancias originales
    InventarioDAOImpl inventarioDAO = new InventarioDAOImpl();
    JugadorDAOImpl jugadorDAO = new JugadorDAOImpl();

    public static void main(String[] args) {
        Combate c = new Combate();
        c.iniciarPartida(1, 2);
    }

    public void iniciarPartida(int idJugador1, int idJugador2) {
        SwingUtilities.invokeLater(() -> {
            // 1. Creamos la vista, el servicio lógico y el controlador
            VentanaJuego ventana = new VentanaJuego();

            // Creamos los objetos Jugador que pide tu PartidaService para setear el mazo actual
            Model.Entities.Jugador j1 = jugadorDAO.obtenerJugadorPorId(idJugador1); // O new Jugador() seteando su ID
            Model.Entities.Jugador j2 = jugadorDAO.obtenerJugadorPorId(idJugador2);

            // Instanciamos el servicio y lo inicializamos con tus jugadores reales de la BD
            Model.Service.PartidaService partidaService = new PartidaService();
            try {
                // 🎲 ¡ESTO ES LO QUE FALTA! Así el service inicializa 'manoActual' y el tablero lógico
                partidaService.iniciarPartida(j1, j2);
            } catch (Model.Exceptions.MazoException e) {
                System.err.println("Error de mazos: " + e.getMessage());
            }

            // Le pasamos el servicio ya inicializado al controlador para que no dé NullPointer
            Controller.PartidaController controlador = new Controller.PartidaController(partidaService);
            ventana.setControlador(controlador);

            // 2. Tu carga visual con DAOs (Se mantiene idéntica)
            List<Carta> mazoJ1 = inventarioDAO.obtenerMazo(idJugador1);
            List<Carta> mazoJ2 = inventarioDAO.obtenerMazo(idJugador2);

            for (Carta carta : mazoJ1) {
                ventana.manoIzquierdaJ1.add(new VistaCarta(
                        carta.getNombre(), carta.getTipo().toString(), carta.mostrarRareza(),
                        carta.getAtkF(), carta.getDE(), carta.getAtkE(), carta.getDF(), "J1", carta.getSprite()
                ));
            }

            for (Carta carta : mazoJ2) {
                ventana.manoDerechaJ2.add(new VistaCarta(
                        carta.getNombre(), carta.getTipo().toString(), carta.mostrarRareza(),
                        carta.getAtkF(), carta.getDE(), carta.getAtkE(), carta.getDF(), "J2", carta.getSprite()
                ));
            }

            ventana.revalidate();
            ventana.repaint();
            ventana.setVisible(true);
        });
    }
}