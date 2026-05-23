package Model.Service;

import Model.Entities.*;
import java.util.List;

public class IAService {

    private final ReglasJuegoService reglasService;

    // ── Pesos ────────────────────────────────────────────────────────────────
    // Ofensivos (altos para que la IA siempre prefiera atacar)
    private static final int PESO_CAPTURA_PROPIA     = 100; // Capturar una carta rival vale mucho
    private static final int BONUS_VENTAJA_TIPO      =  50; // x2.0 tipo → captura casi segura
    private static final int PESO_CENTRO             =  35; // El centro es poder
    private static final int PESO_LADO_MEDIO         =  18;

    // Defensivos (bajos, la IA los ignora casi siempre salvo peligro grave)
    private static final int PESO_EXPOSICION_PELIGRO =  -8; // Penalización muy suave por lado vulnerable
    private static final int MALUS_DESVENTAJA_TIPO   = -15; // El rival tiene x2.0 contra nosotros
    private static final int MALUS_INMUNIDAD_RIVAL   = -10; // Somos inmunes al rival (no podemos capturar)
    private static final int BONUS_SOMOS_INMUNES     =  12; // El rival no nos puede capturar desde ese lado

    public IAService() {
        this.reglasService = new ReglasJuegoService();
    }

    // =========================================================================
    // PUNTO DE ENTRADA PRINCIPAL
    // =========================================================================

    public DecisionIA calcularMejorMovimiento(Tablero tablero, List<Carta> manoIA,
                                              Jugador jugadorIA, Jugador jugadorHumano) {
        int mejorFila            = -1;
        int mejorColumna         = -1;
        int indiceMejorCarta     = -1;
        int mejorPuntuacionTotal = Integer.MIN_VALUE;

        for (int f = 0; f < 3; f++) {
            for (int c = 0; c < 3; c++) {
                Casilla casilla = tablero.getCasilla(f, c);
                if (casilla != null && !casilla.isVacia()) continue;

                for (int i = 0; i < manoIA.size(); i++) {
                    Carta carta = manoIA.get(i);
                    Tablero tableroTrasMovimiento = tablero.clonar();

                    boolean valido = reglasService.ejecutarMovimiento(
                            tableroTrasMovimiento, carta, jugadorIA, f, c
                    );
                    if (!valido) continue;

                    // ── A: Capturas inmediatas (peso dominante) ───────────
                    int casillasIAAntes    = tablero.contarCasillasPorJugador(jugadorIA);
                    int casillasIADespues  = tableroTrasMovimiento.contarCasillasPorJugador(jugadorIA);
                    int capturasInmediatas = casillasIADespues - casillasIAAntes - 1;
                    int puntuacion = capturasInmediatas * PESO_CAPTURA_PROPIA;

                    // ── B: Control del tablero (diferencia de casillas) ───
                    // Premiamos jugadas que maximizan la ventaja total de casillas
                    int casillasHumanoDespues = tableroTrasMovimiento.contarCasillasPorJugador(jugadorHumano);
                    int casillasHumanoAntes   = tablero.contarCasillasPorJugador(jugadorHumano);
                    int ventajaCasillas = (casillasIADespues - casillasHumanoDespues)
                            - (casillasIAAntes   - casillasHumanoAntes);
                    puntuacion += ventajaCasillas * 20;

                    // ── C: Valor posicional + tipos ───────────────────────
                    puntuacion += calcularValorOfensivo(carta, f, c, tablero, jugadorIA);

                    // ── D: Exposición defensiva (peso muy suave) ──────────
                    puntuacion += evaluarExposicion(carta, f, c, tableroTrasMovimiento, jugadorIA);

                    System.out.println("🧠 [IA EVAL] " + carta.getNombre()
                            + " [" + f + "," + c + "] → " + puntuacion
                            + " (capturas=" + capturasInmediatas + ")");

                    if (puntuacion > mejorPuntuacionTotal) {
                        mejorPuntuacionTotal = puntuacion;
                        mejorFila            = f;
                        mejorColumna         = c;
                        indiceMejorCarta     = i;
                    }
                }
            }
        }

        // Fallback
        if (indiceMejorCarta == -1 && !manoIA.isEmpty()) {
            System.out.println("⚠️ [IA WARNING] Sin jugada válida. Activando apertura automática.");
            for (int f = 0; f < 3; f++) {
                for (int c = 0; c < 3; c++) {
                    Casilla cas = tablero.getCasilla(f, c);
                    if (cas == null || cas.isVacia()) return new DecisionIA(0, f, c);
                }
            }
        }

        System.out.println("✅ [IA DECISION] idx=" + indiceMejorCarta
                + " [" + mejorFila + "," + mejorColumna + "] pts=" + mejorPuntuacionTotal);
        return new DecisionIA(indiceMejorCarta, mejorFila, mejorColumna);
    }

    // =========================================================================
    // D: EXPOSICIÓN DEFENSIVA (peso suave, no paraliza a la IA)
    // =========================================================================

    private int evaluarExposicion(Carta carta, int fila, int columna,
                                  Tablero tablero, Jugador jugadorIA) {
        int penalizacion = 0;

        if (fila > 0)    penalizacion += ladoVulnerable(carta, tablero.getCasilla(fila-1, columna), jugadorIA, carta.getAtkF());
        if (fila < 2)    penalizacion += ladoVulnerable(carta, tablero.getCasilla(fila+1, columna), jugadorIA, carta.getDE());
        if (columna > 0) penalizacion += ladoVulnerable(carta, tablero.getCasilla(fila, columna-1), jugadorIA, carta.getAtkE());
        if (columna < 2) penalizacion += ladoVulnerable(carta, tablero.getCasilla(fila, columna+1), jugadorIA, carta.getDF());

        return penalizacion;
    }

    private int ladoVulnerable(Carta cartaIA, Casilla vecino,
                               Jugador jugadorIA, int statNuestraDefensa) {
        if (vecino == null || vecino.isVacia()) return 0;
        if (vecino.getPropietario().equals(jugadorIA)) return 0;

        Carta cartaRival = vecino.getCarta();
        double multRival = TablaTipos.obtenerMultiplicador(cartaRival.getTipo(), cartaIA.getTipo());

        // Solo penalizamos si el rival CLARAMENTE nos supera (márgen > 2)
        int statRivalEfectiva = (int)(obtenerMejorStatRival(cartaRival) * multRival);
        if (statRivalEfectiva > statNuestraDefensa + 2) {
            return PESO_EXPOSICION_PELIGRO;
        }
        return 0;
    }

    /** Devuelve la stat de ataque más alta del rival para estimar el peor caso. */
    private int obtenerMejorStatRival(Carta rival) {
        return Math.max(Math.max(rival.getAtkE(), rival.getAtkF()),
                Math.max(rival.getDE(),   rival.getDF()));
    }

    // =========================================================================
    // C: VALOR OFENSIVO POSICIONAL + TIPOS
    // =========================================================================

    private int calcularValorOfensivo(Carta carta, int fila, int columna,
                                      Tablero tablero, Jugador jugadorIA) {
        int puntuacion = 0;

        if (fila == 1 && columna == 1) puntuacion += PESO_CENTRO;
        else if (fila == 1 || columna == 1) puntuacion += PESO_LADO_MEDIO;

        if (fila > 0)    puntuacion += evaluarLado(carta, tablero.getCasilla(fila-1, columna), jugadorIA, carta.getAtkF());
        if (fila < 2)    puntuacion += evaluarLado(carta, tablero.getCasilla(fila+1, columna), jugadorIA, carta.getDE());
        if (columna > 0) puntuacion += evaluarLado(carta, tablero.getCasilla(fila, columna-1), jugadorIA, carta.getAtkE());
        if (columna < 2) puntuacion += evaluarLado(carta, tablero.getCasilla(fila, columna+1), jugadorIA, carta.getDF());

        return puntuacion;
    }

    private int evaluarLado(Carta cartaIA, Casilla vecino, Jugador jugadorIA, int statAtacante) {
        if (vecino == null || vecino.isVacia()) return statAtacante;
        if (vecino.getPropietario().equals(jugadorIA)) return 0;

        Carta cartaRival   = vecino.getCarta();
        double multNuestro = TablaTipos.obtenerMultiplicador(cartaIA.getTipo(), cartaRival.getTipo());
        double multRival   = TablaTipos.obtenerMultiplicador(cartaRival.getTipo(), cartaIA.getTipo());

        int bonus = 0;
        if      (multNuestro == 2.0) bonus += BONUS_VENTAJA_TIPO;
        else if (multNuestro == 1.0) bonus += statAtacante;
        else if (multNuestro == 0.5) bonus += statAtacante / 2;
        else if (multNuestro == 0.0) bonus += MALUS_INMUNIDAD_RIVAL;

        if      (multRival == 2.0) bonus += MALUS_DESVENTAJA_TIPO;
        else if (multRival == 0.0) bonus += BONUS_SOMOS_INMUNES;

        return bonus;
    }

    // =========================================================================
    // DTO
    // =========================================================================

    public static class DecisionIA {
        public final int idCartaMano;
        public final int fila;
        public final int columna;

        public DecisionIA(int idCartaMano, int fila, int columna) {
            this.idCartaMano = idCartaMano;
            this.fila        = fila;
            this.columna     = columna;
        }
    }
}