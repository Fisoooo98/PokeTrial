package Test;

import Model.Entities.*;
import Model.Service.PokeApiService;
import Model.Service.ReglasJuegoService;

public class CombateServiceTest {

    public static void main(String[] args) {
        System.out.println("=== INICIANDO TEST DEL SISTEMA DE COMBATE ===");
        PokeApiService api = new PokeApiService();
        // 1. Creamos los servicios necesarios
        // (Asume que ReglasJuegoService ya tiene dentro tu calculadora y tabla de tipos)
        ReglasJuegoService reglasJuego = new ReglasJuegoService();
        Tablero tablero = new Tablero(); // Tu tablero vacío de 3x3

        // 2. Creamos dos jugadores de prueba
        Jugador jugador1 = new Jugador(1, "Fisooo");
        Jugador jugador2 = new Jugador(2, "Antonio");

        // 4. Envolvemos los Pokémon en Cartas
        Carta venusaur = new Carta(1,api.cargarDatosPokemon(3),Rareza.COMUN);
        Carta charizard = new Carta(2, api.cargarDatosPokemon(6),Rareza.COMUN);

        // 5. ESCENARIO: Antonio (Jugador 2) ya tiene a Venusaur en la casilla de ARRIBA [0][1]
        tablero.setCasilla(new Casilla(venusaur, jugador2), 0, 1);
        System.out.println("Estado inicial: Casilla [0][1] pertenece a " + tablero.getCasilla(0, 1).getPropietario().getNombre());

        // 6. EJECUCIÓN: Fisooo (Jugador 1) coloca a Charizard en el CENTRO [1][1]
        System.out.println("\n-> Fisooo juega Charizard en la casilla [1][1]...");

        boolean movimientoValido = reglasJuego.ejecutarMovimiento(tablero, charizard, jugador1, 1, 1);

        // 7. VERIFICACIONES (Asserts manuales por consola)
        System.out.println("\n=== RESULTADOS DEL TEST ===");
        System.out.println("¿El movimiento fue válido?: " + movimientoValido + " (Esperado: true)");

        // Comprobamos si la carta del centro se colocó bien
        Casilla centro = tablero.getCasilla(1, 1);
        System.out.println("¿Carta en el centro?: " + (centro != null ? centro.getCarta().getPokemon().getNombre() : "Vacía") + " (Esperado: Charizard)");

        // ¡La prueba de fuego! Comprobamos si Venusaur cambió de dueño
        Casilla arriba = tablero.getCasilla(0, 1);
        String dueñoFinal = arriba.getPropietario().getNombre();
        System.out.println("Propietario final de la casilla de arriba [0][1]: " + dueñoFinal + " (Esperado: Fisooo)");

        if (dueñoFinal.equals("Fisooo") && movimientoValido) {
            System.out.println("\n✅ ¡TEST PASADO CON ÉXITO! La lógica de tipos, bordes y volteo funciona perfecta.");
        } else {
            System.out.println("\n❌ TEST FALLIDO. Revisa las condiciones o los getters.");
        }
    }
}