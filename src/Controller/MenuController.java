package Controller;

import Model.Entities.Dificultad;
import Model.Entities.Jugador;
import Model.DAO.JugadorDAOImpl;
import Model.Service.PartidaService;
import View.Combate.Combate;
import View.Combate.SelectorDificultadPanel;
import View.MenuPrincipal.VentanaMenuPrincipal;
import View.Combate.VentanaJuego;
import View.MenuMazos.VentanaGestionMazo;
import View.MenuSobres.VentanaTiendaSobres;

import javax.swing.*;
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
        // 1. Instanciamos nuestro panel estético recién creado
        SelectorDificultadPanel panelSelector = new SelectorDificultadPanel();

        // 2. Creamos un JDialog (Ventana flotante) dedicada y elegante
        JDialog dialog = new JDialog(menu, "Rival Encontrado", true); // true hace que bloquee el fondo
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panelSelector);
        dialog.pack();
        dialog.setLocationRelativeTo(menu); // La centra respecto al menú principal
        dialog.setResizable(false);

        // Variable contenedora para la dificultad final elegida
        final Dificultad[] dificultadElegida = { null };

        // 3. Listeners de los botones del panel personalizado
        panelSelector.getBtnCancelar().addActionListener(e -> {
            dialog.dispose(); // Cierra la ventanita sin hacer nada
        });

        panelSelector.getBtnAceptar().addActionListener(e -> {
            // Guardamos la dificultad seleccionada en el combo
            dificultadElegida[0] = (Dificultad) panelSelector.getComboDificultad().getSelectedItem();
            dialog.dispose(); // Cerramos el panel
        });

        // Mostramos el panel en pantalla (El código se detiene aquí hasta que se cierre el diálogo)
        dialog.setVisible(true);

        // 4. Control de salida: Si el usuario dio a Cancelar o cerró en la 'X', abortamos
        if (dificultadElegida[0] == null) {
            System.out.println("❌ Combate cancelado en el selector de dificultad.");
            return;
        }

        // 5. Lógica de asignación de IDs de la Base de Datos según la estética elegida
        System.out.println("⚔️ Cargando arena en dificultad: " + dificultadElegida[0]);
        Random rand = new Random();
        int randomIdBase = 0;

        switch (dificultadElegida[0]) {
            case FACIL ->   randomIdBase = rand.nextInt(1, 11);  // Genera del 1 al 10 (el 11 queda fuera)
            case NORMAL ->  randomIdBase = rand.nextInt(11, 21); // Genera del 11 al 20 (el 21 queda fuera)
            case DIFICIL -> randomIdBase = rand.nextInt(21, 31); // Genera del 21 al 30 (el 31 queda fuera)
        }

        System.out.println("Dificulatad elegida" + dificultadElegida[0]);

        // 6. Arrancamos el combate con la semilla de dificultad calculada de forma segura
        Combate c = new Combate();
        c.iniciarPartida(getJugador().getId(), randomIdBase);
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