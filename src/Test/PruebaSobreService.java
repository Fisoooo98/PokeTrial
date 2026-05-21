package Test;


import Model.Entities.*;
import Model.Exceptions.MonedasInsuficientesException;
import Model.Service.TiendaService;

import java.util.List;

public class PruebaSobreService {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO TEST DEL SISTEMA DE SOBRES Y GACHA ===");

        // 1. Inicializamos los servicios
        TiendaService sobreService = new TiendaService();
        Jugador jugador = new Jugador(1, "Fisooo", 1500); // Arranca con 1500 PB

        System.out.println("Jugador inicial: " + jugador.getNombre() + " con " + jugador.getPuntosBatalla() + " PB.");
        System.out.println("----------------------------------------------------------------");

        // --- ESCENARIO 1: COMPRA EXITOSA (SOBRE DE ORO) ---
        try {
            System.out.println("\n🛒 Intentando comprar sobre de ORO (Costo: 1000 PB)...");
            List<Carta> cartasObtenidas = sobreService.comprarSobre(jugador, TipoSobre.ORO);

            System.out.println("\n✅ ¡Compra realizada con éxito!");
            System.out.println("PB restantes del jugador: " + jugador.getPuntosBatalla() + " (Esperado: 500)");
            System.out.println("Cantidad de cartas obtenidas: " + cartasObtenidas.size() + " (Esperado: 5)");

            System.out.println("\n=== CARTAS OBTENIDAS EN EL SOBRE ===");
            for (int i = 0; i < cartasObtenidas.size(); i++) {
                Carta carta = cartasObtenidas.get(i);
                Pokemon p = carta.getPokemon();
                System.out.println((i + 1) + ". [" + carta.getRareza() + "] " + p.getNombre()
                        + " -> [AtkF: " + p.getAtkF() + " | De: " + p.getdE()
                        + " | AtkE: " + p.getAtkE() + " | Df: " + p.getdF() + "]");
            }

        } catch (MonedasInsuficientesException e) {
            System.out.println("❌ ERROR INESPERADO EN CASO 1: El jugador sí tenía dinero pero saltó la excepción.");
            e.printStackTrace();
        }

        System.out.println("----------------------------------------------------------------");

        // --- ESCENARIO 2: CONTROL DE EXCEPCIÓN (COMPRA INSUFICIENTE) ---
        System.out.println("\n🛒 Intentando comprar un segundo sobre de ORO (Costo: 1000 PB, Saldo: 500 PB)...");
        try {
            // Esto debería fallar y lanzar la excepción directa al catch
            sobreService.comprarSobre(jugador, TipoSobre.ORO);
            System.out.println("❌ TEST FALLIDO: El programa permitió la compra aunque el jugador no tenía suficientes puntos.");

        } catch (MonedasInsuficientesException e) {
            System.out.println("✅ TEST PASADO CON ÉXITO: La excepción saltó correctamente.");
            System.out.println("Mensaje capturado de la excepción: \"" + e.getMessage() + "\"");
        }

        System.out.println("\n=== FIN DEL TEST DEL SISTEMA DE SOBRES ===");
    }
}
