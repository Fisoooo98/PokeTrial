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

    /**
     * Intenta realizar la compra y apertura de un sobre consumiendo la lógica del backend.
     * @param ventana Referencia de la vista para poder lanzar los modales de aviso.
     * @param sobreElegido El enumerado oficial TipoSobre (BRONCE, PLATA, ORO).
     * @return La lista de 5 cartas mejoradas si la compra fue exitosa, o null si falló.
     */
    public List<Carta> procesarCompraSobre(VentanaTiendaSobres ventana, TipoSobre sobreElegido) {
        try {
            // 🛒 Llamamos a tu función exacta del servicio
            List<Carta> cartasGanadas = tiendaService.comprarSobre(jugador, sobreElegido);

            System.out.println("💰 Compra exitosa. Puntos restantes de " + jugador.getNombre() + ": " + jugador.getPuntosBatalla());
            return cartasGanadas;

        } catch (MonedasInsuficientesException ex) {
            // 🛑 Capturamos tu excepción personalizada y se la mostramos al usuario de forma pro
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