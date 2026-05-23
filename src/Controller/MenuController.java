package Controller;

import Model.Entities.Jugador;
import Model.DAO.JugadorDAOImpl;
import Model.Service.PartidaService;
import View.Combate.Combate;
import View.MenuPrincipal.VentanaMenuPrincipal;
import View.Combate.VentanaJuego;
import View.MenuMazos.VentanaGestionMazo;
import View.MenuSobres.VentanaTiendaSobres;

import java.util.Random;

public class MenuController {
    private Jugador jugador;
    private final JugadorDAOImpl jugadorDAO = new JugadorDAOImpl();

    public MenuController(int idJugador) {
        // 🔄 El dinero NO viene del constructor, lo pillamos directo de la BD
        this.jugador = jugadorDAO.obtenerJugadorPorId(idJugador);
    }

    public void ElegirCombate(){

    }
    public void abrirCombate(VentanaMenuPrincipal menu) {
        // Aquí instanciarías tu PartidaService y abrirías la arena
        // VentanaJuego arena = new VentanaJuego(new PartidaController(...));
        Random rand = new Random();
        Combate c = new Combate();
        c.iniciarPartida(getJugador().getId(),rand.nextInt(2,7));
    }

    public void abrirGestionMazos() {
        MazoController mazoCtrl = new MazoController(jugador);
        VentanaGestionMazo vistaMazo = new VentanaGestionMazo(mazoCtrl);
        vistaMazo.setVisible(true);
    }

    public void abrirTienda(VentanaMenuPrincipal menu) {
        SobreController sobreCtrl = new SobreController(jugador);
        VentanaTiendaSobres tienda = new VentanaTiendaSobres(sobreCtrl);

        // Al cerrar la tienda, actualizamos el dinero en el menú principal por si compró algo
        tienda.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                // Refrescamos al jugador desde la BD para ver el nuevo saldo
                jugador = jugadorDAO.obtenerJugadorPorId(jugador.getId());
                menu.actualizarDatos(jugador.getNombre(), jugador.getPuntosBatalla());
            }
        });
        tienda.setVisible(true);
    }

    public Jugador getJugador() { return jugador; }
}