package Controller;

import Model.Service.PartidaService;
import Model.Service.ReglasJuegoService;
import Model.Service.IAService;
import Model.Entities.*;
import View.Combate.VentanaJuego;
import View.Combate.VistaCarta;
import View.Combate.CasillaPanel;
import javax.swing.*;
import java.awt.*;

public class PartidaController {

    private final PartidaService partidaService;
    private final ReglasJuegoService reglasService;
    private final IAService iaService;

    // 🛡️ Candado anti-clicks: congela la interfaz del humano mientras la IA procesa su jugada
    private boolean bloqueoTurnoIA = false;

    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
        this.reglasService = new ReglasJuegoService();
        this.iaService = new IAService();
    }

    public PartidaService getPartidaService() {
        return this.partidaService;
    }

    // =========================================================================
    // 🚀 ARRANQUE
    // =========================================================================

    /**
     * Inicializa la arena gráfica de combate.
     * Debe llamarse justo después de inyectar este controlador en la VentanaJuego
     * y de hacer la ventana visible (setVisible(true)).
     */
    public void iniciarPartida(VentanaJuego ventana) {
        ventana.actualizarManoVisual("J1");
        ventana.actualizarManoVisual("J2");

        ventana.inicializarMarcadores(
                partidaService.getJugador1().getNombre(),
                partidaService.getJugador2().getNombre(),
                partidaService.getTurnoActualNum()
        );

        // 🔑 FIX: Si J2 (IA) gana la iniciativa, esperamos a que Swing termine de
        // renderizar todos los paneles antes de que la IA intente leer manoDerechaJ2.
        // Sin este retardo, getComponentCount() devuelve 0 y la IA no puede tirar.
        if (partidaService.getTurnoActualNum() == 2) {
            SwingUtilities.invokeLater(() -> {
                Timer arranqueIA = new Timer(400, e -> {
                    System.out.println("🤖 La IA gana la iniciativa. Turno 1 para J2.");
                    ejecutarTurnoIA(ventana);
                });
                arranqueIA.setRepeats(false);
                arranqueIA.start();
            });
        }
    }

    // =========================================================================
    // ⚔️ MOVIMIENTO PRINCIPAL
    // =========================================================================

    /**
     * Procesa la colocación de una carta en el tablero tanto en lógica como en vista.
     */
    public void EjecutarMoviento(int fila, int columna, VentanaJuego ventana, CasillaPanel casillaVisual) {
        try {
            System.out.println("\n===== [DEBUG START: EJECUTAR MOVIMIENTO] =====");
            System.out.println("-> Coordenadas clickeadas: Fila [" + fila + "], Columna [" + columna + "]");

            if (bloqueoTurnoIA) {
                System.out.println("⏳ [DEBUG] Movimiento bloqueado: La IA está procesando.");
                return;
            }

            VistaCarta cartaVisualSel = ventana.getCartaSeleccionada();
            if (cartaVisualSel == null) {
                System.out.println("❌ [DEBUG] ERROR: 'cartaSeleccionada' en la ventana es NULA.");
                return;
            }

            System.out.println("-> Carta Visual detectada: " + cartaVisualSel.getName()
                    + " | Dueño en vista: " + cartaVisualSel.getJugador());
            System.out.println("-> Elementos físicos en la casilla visual: " + casillaVisual.getComponentCount());

            if (casillaVisual.getComponentCount() > 0) {
                System.out.println("❌ [DEBUG] ERROR: Casilla ya ocupada visualmente.");
                return;
            }

            String dueñoCartaVisual = cartaVisualSel.getJugador();
            Jugador jugadorActivo = dueñoCartaVisual.equals("J1")
                    ? partidaService.getJugador1()
                    : partidaService.getJugador2();

            System.out.println("-> Jugador mapeado como ACTIVO: "
                    + jugadorActivo.getNombre() + " (ID: " + jugadorActivo.getId() + ")");
            System.out.println("-> Turno lógico en el Backend: " + partidaService.getTurnoActualNum());

            // Validación de turno
            int turnoSincronizado = dueñoCartaVisual.equals("J1") ? 1 : 2;
            if (partidaService.getTurnoActualNum() != turnoSincronizado) {
                System.out.println("❌ [DEBUG] ERROR: Desfase de turnos. Carta de "
                        + dueñoCartaVisual + " pero turno backend es: " + partidaService.getTurnoActualNum());
                JOptionPane.showMessageDialog(ventana,
                        "No es el turno de este jugador.", "Turno Incorrecto", JOptionPane.WARNING_MESSAGE);
                cartaVisualSel.marcarSeleccionada(false);
                ventana.setCartaSeleccionada(null);
                ventana.repaint();
                return;
            }

            int indiceCartaMano = ventana.obtenerIndiceManoActual(cartaVisualSel);
            System.out.println("-> Índice de la carta en la mano visual: " + indiceCartaMano);

            if (indiceCartaMano == -1) {
                System.out.println("❌ [DEBUG] ERROR: No se encontró la carta en el array de la mano (índice -1).");
                return;
            }

            Tablero tableroLogico = partidaService.getTablero();

            Carta cartaLogica = dueñoCartaVisual.equals("J1")
                    ? partidaService.getMazoJ1().get(indiceCartaMano)
                    : partidaService.getMazoJ2().get(indiceCartaMano);

            System.out.println("-> [BACKEND] Intentando ejecutar reglasService.ejecutarMovimiento()...");
            System.out.println("   Carta Lógica: " + cartaLogica.getNombre()
                    + " | Atks -> N:" + cartaLogica.getAtkE()
                    + " S:" + cartaLogica.getDE()
                    + " O:" + cartaLogica.getAtkF()
                    + " E:" + cartaLogica.getDF());

            boolean movimientoValido = reglasService.ejecutarMovimiento(
                    tableroLogico, cartaLogica, jugadorActivo, fila, columna
            );

            System.out.println("-> [BACKEND] ¿Resultado del movimiento?: " + movimientoValido);

            if (!movimientoValido) {
                System.out.println("❌ [DEBUG] ERROR: El backend denegó el movimiento.");
                JOptionPane.showMessageDialog(ventana, "¡Movimiento inválido según las reglas!");
                return;
            }

            // --- Movimiento aceptado: actualizamos lógica y vista ---

            String jugadorQueTiro = (jugadorActivo.getId() == 1) ? "J1" : "J2";

            // Eliminar carta del mazo lógico
            if (dueñoCartaVisual.equals("J1")) {
                partidaService.getMazoJ1().remove(indiceCartaMano);
            } else {
                partidaService.getMazoJ2().remove(indiceCartaMano);
            }
            System.out.println("-> [BACKEND] Carta removida del mazo lógico.");

            // Mover la VistaCarta de su panel de mano a la casilla del tablero
            Container parent = cartaVisualSel.getParent();
            if (parent != null) parent.remove(cartaVisualSel);
            casillaVisual.add(cartaVisualSel, BorderLayout.CENTER);
            cartaVisualSel.marcarSeleccionada(false);
            ventana.setCartaSeleccionada(null);
            System.out.println("-> [SWING] Carta añadida al panel de la casilla.");

            ventana.actualizarManoVisual(jugadorQueTiro);
            ventana.getTablero3x3().revalidate();
            ventana.getTablero3x3().repaint();

            System.out.println("-> [REFRESH] Lanzando refrescarYAnimarTablero()...");
            refrescarYAnimarTablero(ventana, tableroLogico);

            int casillasJ1 = tableroLogico.contarCasillasPorJugador(partidaService.getJugador1());
            int casillasJ2 = tableroLogico.contarCasillasPorJugador(partidaService.getJugador2());
            System.out.println("-> [SCORE] Casillas actuales -> J1: " + casillasJ1 + " | J2: " + casillasJ2);
            ventana.actualizarContadoresCasillas(casillasJ1, casillasJ2);

            partidaService.cambiarTurno();
            System.out.println("-> [BACKEND] Turno cambiado. Nuevo turno num: " + partidaService.getTurnoActualNum());
            ventana.actualizarTurnoVisual(partidaService.getTurnoActualNum());

            ventana.revalidate();
            ventana.repaint();

            // Comprobar fin de partida o turno de la IA
            if (tableroLogico.esLleno()) {
                System.out.println("🏁 [FIN] Tablero lleno detectado.");
                Jugador ganador = partidaService.getGanador();
                if (ganador != null) {
                    partidaService.actualizarPuntosBatalla(ganador.getId(), ganador.getPuntosBatalla() + 500);
                }
                ventana.mostrarCartelGanador(ganador);
            } else if (partidaService.getTurnoActualNum() == 2) {
                System.out.println("🤖 [IA TRIGGER] El nuevo turno es de J2. Invocando IA...");
                ejecutarTurnoIA(ventana);
            }

            System.out.println("===== [DEBUG END: EJECUTAR MOVIMIENTO] =====\n");

        } catch (Exception ex) {
            System.out.println("💥 [CRITICAL EXCEPTION] Error en el flujo de EjecutarMoviento:");
            ex.printStackTrace();
            this.bloqueoTurnoIA = false; // Desbloqueo de emergencia
        }
    }

    // =========================================================================
    // 🤖 LÓGICA DE LA IA
    // =========================================================================

    /**
     * Orquesta el turno de la IA:
     * - Bloquea la interfaz del humano.
     * - Lanza el cálculo en un hilo de fondo para no congelar Swing.
     * - Un Timer árbitro fuerza una jugada de emergencia si la IA tarda más de 2.2 s.
     */
    private void ejecutarTurnoIA(VentanaJuego ventana) {
        this.bloqueoTurnoIA = true;

        // ⏳ Delay visual: la IA "piensa" 1 segundo antes de actuar
        Timer delayInicio = new Timer(1000, null);
        delayInicio.setRepeats(false);
        delayInicio.addActionListener(inicio -> {

            Timer timeoutArbitro = new Timer(2200, null);

            Thread hiloPensamientoIA = new Thread(() -> {
                System.out.println("🤖 [IA HILO] Iniciando cálculo inteligente de fondo...");

                Tablero tableroLogico = partidaService.getTablero();
                java.util.List<Carta> manoIA = partidaService.getMazoJ2();

                if (manoIA.isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        System.out.println("❌ [IA ABORT] La mano lógica de la IA está vacía.");
                        this.bloqueoTurnoIA = false;
                        timeoutArbitro.stop();
                    });
                    return;
                }

                Jugador jugadorIA = partidaService.getJugador2();
                Jugador jugadorHumano = partidaService.getJugador1();

                IAService.DecisionIA decision = iaService.calcularMejorMovimiento(
                        tableroLogico, manoIA, jugadorIA, jugadorHumano
                );

                SwingUtilities.invokeLater(() -> {
                    if (!bloqueoTurnoIA) {
                        System.out.println("💤 [IA HILO] Cálculo terminó tarde. Ignorando resultado.");
                        return;
                    }

                    timeoutArbitro.stop();

                    if (decision.idCartaMano != -1) {
                        System.out.println("🤖 [IA CONTROL] Movimiento calculado a tiempo. Aplicando...");
                        forzarTiroIA(decision.fila, decision.columna, decision.idCartaMano, ventana);
                    } else {
                        System.out.println("⚠️ [IA CONTROL] Sin movimiento calculado. Activando plan de emergencia.");
                        fuerzaBrutaCentroOHueco(ventana);
                    }
                });
            });

            timeoutArbitro.addActionListener(e -> {
                timeoutArbitro.stop();
                if (!bloqueoTurnoIA) return;
                System.out.println("🚨 [ÁRBITRO TIMEOUT] IA tardó más de 2.2 s. Forzando tiro inmediato.");
                hiloPensamientoIA.interrupt();
                fuerzaBrutaCentroOHueco(ventana);
            });

            timeoutArbitro.start();
            hiloPensamientoIA.start();
        });

        delayInicio.start();
    }

    /**
     * Fallback: coloca la primera carta de la IA en el centro o, si está ocupado,
     * en el primer hueco libre del tablero.
     */
    private void fuerzaBrutaCentroOHueco(VentanaJuego ventana) {
        Tablero tableroLogico = partidaService.getTablero();

        int filaDestino = 1;
        int columnaDestino = 1;

        Casilla centro = tableroLogico.getCasilla(filaDestino, columnaDestino);
        if (centro != null && !centro.isVacia()) {
            buscarHueco:
            for (int f = 0; f < 3; f++) {
                for (int c = 0; c < 3; c++) {
                    Casilla casilla = tableroLogico.getCasilla(f, c);
                    if (casilla == null || casilla.isVacia()) {
                        filaDestino = f;
                        columnaDestino = c;
                        break buscarHueco;
                    }
                }
            }
        }

        forzarTiroIA(filaDestino, columnaDestino, 0, ventana);
    }

    /**
     * Método atómico que enlaza los componentes visuales de Swing y dispara el movimiento de la IA.
     * Si la mano visual está vacía la reconstruye antes de intentar leer de ella.
     */
    private void forzarTiroIA(int fila, int columna, int indiceCarta, VentanaJuego ventana) {
        JPanel panelManoJ2 = ventana.manoDerechaJ2;

        // Si la mano visual está vacía la reconstruimos antes de leerla
        if (panelManoJ2.getComponentCount() == 0) {
            System.out.println("⚠️ [IA INFLATER] Mano visual de J2 vacía. Forzando reconstrucción...");
            ventana.actualizarManoVisual("J2");
            panelManoJ2.revalidate();
            panelManoJ2.repaint();
        }

        if (panelManoJ2.getComponentCount() == 0) {
            System.out.println("❌ [IA ERROR CRÍTICO] La mano de J2 sigue vacía tras reconstrucción.");
            this.bloqueoTurnoIA = false;
            return;
        }

        int indiceSeguro = Math.min(indiceCarta, panelManoJ2.getComponentCount() - 1);
        Component comp = panelManoJ2.getComponent(indiceSeguro);

        if (!(comp instanceof VistaCarta)) {
            System.out.println("❌ [IA ERROR CRÍTICO] El componente obtenido no es una VistaCarta.");
            this.bloqueoTurnoIA = false;
            return;
        }

        VistaCarta cartaVisualIA = (VistaCarta) comp;
        int indiceComponenteTablero = (fila * 3) + columna;
        CasillaPanel casillaPanelVisual = (CasillaPanel) ventana.getTablero3x3().getComponent(indiceComponenteTablero);

        ventana.setCartaSeleccionada(cartaVisualIA);
        this.bloqueoTurnoIA = false; // 🔓 Abrimos el candado justo antes de ejecutar

        System.out.println("🎯 [EJECUCIÓN IA] Colocando en [" + fila + "," + columna
                + "] la carta: " + cartaVisualIA.getName());
        EjecutarMoviento(fila, columna, ventana, casillaPanelVisual);
    }

    // =========================================================================
    // 🔄 REFRESCO Y ANIMACIONES DEL TABLERO
    // =========================================================================

    /**
     * Compara el estado lógico del tablero con el visual.
     * Si detecta un cambio de propietario en alguna casilla, lanza la animación de captura.
     */
    public void refrescarYAnimarTablero(VentanaJuego ventana, Tablero tableroLogico) {
        JPanel tableroVisual = ventana.getTablero3x3();
        Component[] casillas = tableroVisual.getComponents();

        for (int i = 0; i < 9; i++) {
            int fila = i / 3;
            int columna = i % 3;

            Casilla casillaLogica = tableroLogico.getCasilla(fila, columna);
            CasillaPanel casillaVisual = (CasillaPanel) casillas[i];

            if (casillaLogica == null || casillaLogica.getCarta() == null) continue;

            Jugador dueñoLogico = casillaLogica.getPropietario();
            String tagDueñoBackend = dueñoLogico.getId() == partidaService.getJugador1().getId() ? "J1" : "J2";

            if (casillaVisual.getComponentCount() == 0) continue;

            Component comp = casillaVisual.getComponent(0);
            if (!(comp instanceof VistaCarta)) continue;

            VistaCarta cartaVisual = (VistaCarta) comp;

            if (!cartaVisual.getJugador().equals(tagDueñoBackend)) {
                System.out.println("🔄 [ANIMACIÓN] Volteando carta en ["
                        + fila + "," + columna + "] → " + tagDueñoBackend);
                cartaVisual.setJugador(tagDueñoBackend);
                animarCapturaCarta(cartaVisual, tagDueñoBackend);
            }
        }

        tableroVisual.revalidate();
        tableroVisual.repaint();
    }

    /**
     * Animación de parpadeo dorado (6 tics × 100 ms = 600 ms) que indica
     * visualmente el volteo/captura de una carta.
     */
    private void animarCapturaCarta(VistaCarta carta, String nuevoDueño) {
        final Color colorOriginalFondo = carta.getBackground();
        final int[] tics = {0};

        Timer timerAnimacion = new Timer(100, null);
        timerAnimacion.addActionListener(e -> {
            tics[0]++;

            if (tics[0] % 2 == 0) {
                carta.setBackground(new Color(251, 191, 36)); // Destello dorado
                carta.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4, true));
            } else {
                carta.setBackground(new Color(31, 41, 55));   // Fondo oscuro de contraste
            }
            carta.repaint();

            if (tics[0] >= 6) {
                timerAnimacion.stop();
                carta.setBackground(colorOriginalFondo);
                carta.cambiarPropietario(nuevoDueño); // Azul J1 / Rojo J2
            }
        });

        timerAnimacion.start();
    }
}