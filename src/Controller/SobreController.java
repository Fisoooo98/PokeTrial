package Controller;

import Model.Service.TiendaService;
import Model.Entities.Carta;
import Model.Entities.Jugador;
import Model.Entities.TipoSobre;
import Model.Exceptions.MonedasInsuficientesException;
import View.MenuSobres.VentanaTiendaSobres;
import javax.swing.*;
import java.util.List;

public class SobreController {

    private final Jugador jugador;
    private final TiendaService tiendaService;

    public SobreController(Jugador jugador) {
        this.jugador = jugador;
        // Instanciamos tu servicio oficial
        this.tiendaService = new TiendaService();
    }

    public List<Carta> procesarCompraSobre(VentanaTiendaSobres ventana, TipoSobre sobreElegido, int regionSeleccionada) {
        try {
            // 🛒 ARREGLADO: Ahora le pasamos la regionSeleccionada a tu servicio
            List<Carta> cartasGanadas = tiendaService.comprarSobre(jugador, sobreElegido, regionSeleccionada);

            System.out.println("💰 Compra exitosa. Puntos restantes de " + jugador.getNombre() + ": " + jugador.getPuntosBatalla());
            return cartasGanadas;

        } catch (MonedasInsuficientesException ex) {
            JOptionPane.showMessageDialog(
                    ventana,
                    ex.getMessage() + "\n¡Ganá más batallas para acumular puntos!",
                    "Fondos Insuficientes",
                    JOptionPane.WARNING_MESSAGE
            );
            return null;
        }
    }

    public Jugador getJugador() {
        return this.jugador;
    }
}