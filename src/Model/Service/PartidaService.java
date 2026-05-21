package Model.Service;

import Model.DAO.JugadorDAOImpl;
import Model.Entities.Carta;
import Model.Entities.Jugador;
import Model.Entities.Tablero;
import Model.Exceptions.MazoException;
import java.util.List;

public class PartidaService {
    private final ReglasJuegoService reglasJuego = new ReglasJuegoService();
    private final MazoService mazoService = new MazoService(); // Mantenemos tu mazoService
    private final JugadorDAOImpl jugadorDAO = new JugadorDAOImpl();


    private Tablero tablero;
    private Jugador jugador1;
    private Jugador jugador2;
    private List<Carta> mazoJ1;
    private List<Carta> mazoJ2;
    private int turnoActual; // 1 para Jugador 1, 2 para Jugador 2

    /**
     * 💡 MODIFICACIÓN: Hacemos este método público para que el Main pueda
     * inicializar el tablero lógico y los mazos del backend al arrancar el juego.
     */
    public void iniciarPartida(Jugador j1, Jugador j2) throws MazoException {
        this.tablero = new Tablero();
        this.jugador1 = j1;
        this.jugador2 = j2;

        // Cargamos los mazos lógicos en el backend (esencial para evitar el NullPointerException)
        this.mazoJ1 = mazoService.cargarMazoJugador(jugador1.getId());
        this.mazoJ2 = mazoService.cargarMazoJugador(jugador2.getId());

        // Decidimos el primer turno de forma aleatoria con tus reglas
        if (reglasJuego.decidirPrimerTurno()) {
            this.turnoActual = 1;
        } else {
            this.turnoActual = 2;
        }

        System.out.println("🎲 Partida Iniciada lógicamente. Primer turno para Jugador: " + this.turnoActual);
    }

    public boolean isBatallaTerminada() {
        return this.tablero.esLleno();
    }

    /**
     * ⚙️ CORRECCIÓN CLAVE: Quitamos la remoción de la carta y el cambiarTurno() de acá adentro.
     * ¿Por qué? Porque ahora el Controlador lo hace manualmente JUSTO DESPUÉS de actualizar
     * los colores visuales de las cartas volteadas en Swing. Si lo dejábamos acá, cambiaba el turno
     * antes de tiempo y el controlador refrescaba los colores usando el mazo del jugador equivocado.
     */
    public boolean jugarTurno(int indiceCartaMano, int fila, int columna) {
        List<Carta> manoActual = getMazoActual();
        Jugador jugadorActual = getJugadorActual();

        if (manoActual == null || indiceCartaMano < 0 || indiceCartaMano >= manoActual.size()) {
            System.out.println("⚠️ Error en PartidaService: Índice inválido o mazo no inicializado.");
            return false;
        }

        Carta cartaElegida = manoActual.get(indiceCartaMano);

        // Ejecutamos tu método exacto de reglas de combate vecino (UP, DOWN, LEFT, RIGHT)
        return reglasJuego.ejecutarMovimiento(tablero, cartaElegida, jugadorActual, fila, columna);
    }

    public Jugador getGanador() {
        // Validación de empate técnico primero
        int casillasJ1 = tablero.contarCasillasPorJugador(jugador1);
        int casillasJ2 = tablero.contarCasillasPorJugador(jugador2);

        if (casillasJ1 == casillasJ2) {
            return null; // Es un empate
        }

        Jugador ganador = (casillasJ1 > casillasJ2) ? jugador1 : jugador2;

        if (ganador != null) {
            try {
                ganador.setPuntosBatalla(ganador.getPuntosBatalla() + 100);
                jugadorDAO.actualizarPuntosBatalla(ganador.getId(), ganador.getPuntosBatalla());
                System.out.println("💰 Puntos actualizados en BD para " + ganador.getNombre());
            } catch (Exception e) {
                System.out.println("No se pudieron actualizar los puntos en la BD: " + e.getMessage());
            }
        }
        return ganador;
    }

    /**
     * 🔄 Cambia el turno de forma matemática entre 1 y 2.
     * Ahora es público para que el PartidaController lo invoque en el momento exacto.
     */
    public void cambiarTurno() {
        this.turnoActual = (this.turnoActual == 1) ? 2 : 1;
        System.out.println("🎲 El turno lógico cambió al Jugador: " + this.turnoActual + " (" + getJugadorActual().getNombre() + ")");
    }

    // --- GETTERS MANTENIDOS EXACTAMENTE IGUAL ---

    public Jugador getJugadorActual() {
        return (this.turnoActual == 1) ? this.jugador1 : this.jugador2;
    }

    public List<Carta> getMazoActual() {
        return (this.turnoActual == 1) ? this.mazoJ1 : this.mazoJ2;
    }

    public Tablero getTablero() {
        return this.tablero;
    }

    public void actualizarPuntosBatalla(int idJugador,int puntosBatalla) {
        jugadorDAO.actualizarPuntosBatalla(idJugador,puntosBatalla);
    }
    public List<Carta> getMazoJ1() { return this.mazoJ1; }
    public List<Carta> getMazoJ2() { return this.mazoJ2; }
    public Jugador getJugador1() { return this.jugador1; }
    public Jugador getJugador2() { return this.jugador2; }
    public int getTurnoActualNum() { return this.turnoActual; } // Devuelve 1 o 2
}