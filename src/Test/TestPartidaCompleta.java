package Test;

import Model.Entities.Carta;
import Model.Entities.Jugador;
import Model.Service.PartidaService;
import Model.Exceptions.MazoException;
import java.util.List;

public class TestPartidaCompleta {

    public static void main(String[] args) {
        System.out.println("=== 🎮 INICIANDO TEST DE PARTIDA COMPLETA ===");

        PartidaService partida = new PartidaService();

        // 1. Simulamos dos jugadores de la BD (Asegúrate de que existan estas IDs)
        Jugador j1 = new Jugador();
        j1.setId(1);
        j1.setNombre("Fisooo");
        j1.setPuntosBatalla(500);

        Jugador j2 = new Jugador();
        j2.setId(2);
        j2.setNombre("Antonio");
        j2.setPuntosBatalla(500);

        try {
            // 2. Inicializamos la batalla (Carga mazos de PostgreSQL y decide el turno)
            partida.iniciarPartida(j1, j2);
            System.out.println("✅ Partida inicializada con éxito.");
            System.out.println("🎲 Primer turno asignado a: " + partida.getJugadorActual().getNombre());

            // 3. Simulamos un bucle de juego automatizado hasta que se llene el tablero
            // Forzamos jugadas en cascada en el tablero (filas 0 a 2, columnas 0 a 2)
            int fila = 0;
            int columna = 0;

            while (!partida.isBatallaTerminada()) {
                Jugador activo = partida.getJugadorActual();
                List<Carta> mano = partida.getMazoActual();

                System.out.println("\n👉 Turno de: " + activo.getNombre() + " (Cartas en mano: " + mano.size() + ")");

                // El jugador siempre intentará tirar la primera carta que le quede en la mano (índice 0)
                boolean jugadaOk = partida.jugarTurno(0, fila, columna);

                if (jugadaOk) {
                    System.out.println("🎯 Carta colocada con éxito en [" + fila + "," + columna + "]");

                    // Avanzamos las coordenadas para la siguiente jugada del bucle
                    columna++;
                    if (columna > 2) {
                        columna = 0;
                        fila++;
                    }
                } else {
                    System.out.println("❌ Error al colocar en [" + fila + "," + columna + "]. Avanzando casilla...");
                    break;
                }
            }

            // 4. Finalización del juego y verificación de resultados
            System.out.println("\n=== 🏁 FIN DE LA BATALLA ===");
            System.out.println("¿El tablero está lleno?: " + partida.isBatallaTerminada());

            Jugador ganador = partida.getGanador();
            if (ganador == null) {
                System.out.println("⚖️ Resultado: ¡Empate técnico!");
            } else {
                System.out.println("🏆 ¡El ganador es " + ganador.getNombre() + "!");
                System.out.println("💰 Sus nuevos puntos deberían estar guardados en la BD.");
            }

        } catch (MazoException e) {
            System.out.println("❌ ERROR CRÍTICO: Uno de los jugadores no tiene un mazo de 5 cartas válido en la BD.");
        } catch (Exception e) {
            System.out.println("❌ ERROR INESPERADO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}