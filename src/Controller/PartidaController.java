package Controller;

import Model.Service.PartidaService;
import Model.Service.ReglasJuegoService;
import Model.Entities.Tablero;
import Model.Entities.Carta;
import Model.Entities.Jugador;
import Model.Entities.Casilla;
import View.Combate.VentanaJuego;
import View.Combate.VistaCarta;
import View.Combate.CasillaPanel;
import javax.swing.*;
import java.awt.*;

public class PartidaController {

    private final PartidaService partidaService;
    private final ReglasJuegoService reglasService;
    Jugador ganador = new  Jugador();
    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
        this.reglasService = new ReglasJuegoService();
    }

    public PartidaService getPartidaService() {
        return this.partidaService;
    }

    public void EjecutarMoviento(int fila, int columna, VentanaJuego ventana, CasillaPanel casillaVisual) {
        VistaCarta cartaVisualSel = ventana.getCartaSeleccionada();

        // 1. Validación básica de Swing
        if (cartaVisualSel != null && casillaVisual.getComponentCount() == 0) {

            // 🛑 BUG 1 SOLUCIONADO: Validar que la carta pertenezca al jugador del turno actual
            String turnoActualBackend = (partidaService.getJugadorActual().getId() == 1) ? "J1" : "J2";

            // Si el jugador intenta mover una carta que no es de su propiedad en su turno, lo frenamos
            if (!cartaVisualSel.getJugador().equals(turnoActualBackend)) {
                System.out.println("⚠️ ¡Ey! No es el turno de este jugador o la carta no le pertenece.");
                JOptionPane.showMessageDialog(ventana, "No podés jugar una carta del rival. Es el turno de: " + partidaService.getJugadorActual().getNombre(), "Turno Incorrecto", JOptionPane.WARNING_MESSAGE);
                cartaVisualSel.marcarSeleccionada(false);
                ventana.setCartaSeleccionada(null);
                ventana.repaint();
                return; // Cortamos la ejecución acá
            }

            int indiceCartaMano = ventana.obtenerIndiceManoActual(cartaVisualSel);

            if (indiceCartaMano != -1) {
                Tablero tableroLogico = partidaService.getTablero();
                Jugador jugadorActivo = partidaService.getJugadorActual();
                Carta cartaLogica = partidaService.getMazoActual().get(indiceCartaMano);

                // 2. ⚔️ Ejecutamos las reglas en el Backend
                boolean movimientoValido = reglasService.ejecutarMovimiento(
                        tableroLogico, cartaLogica, jugadorActivo, fila, columna
                );

                if (movimientoValido) {
                    String jugadorQueTiro = (jugadorActivo.getId() == 1) ? "J1" : "J2";

                    // 3. Sincronizamos el mazo lógico (removemos del backend)
                    partidaService.getMazoActual().remove(indiceCartaMano);

                    // 4. Movemos físicamente la carta al tablero visual
                    Container parent = cartaVisualSel.getParent();
                    if (parent != null) {
                        parent.remove(cartaVisualSel);
                        parent.repaint();
                    }
                    casillaVisual.add(cartaVisualSel, BorderLayout.CENTER);
                    cartaVisualSel.marcarSeleccionada(false);
                    ventana.setCartaSeleccionada(null);

                    // 5. Redibujamos la mano lateral limpia para evitar desfases de índices
                    ventana.actualizarManoVisual(jugadorQueTiro);

                    // 6. 🔄 Analizamos volteos lógicos y disparamos animaciones visuales
                    refrescarYAnimarTablero(ventana, tableroLogico);

                    // 📊 7. NUEVO: ACTUALIZACIÓN DE CONTADORES EN LA INTERFAZ
                    // Usamos tu método contarCasillasPorJugador pasándole las instancias del service
                    int casillasJ1 = tableroLogico.contarCasillasPorJugador(partidaService.getJugador1());
                    int casillasJ2 = tableroLogico.contarCasillasPorJugador(partidaService.getJugador2());
                    ventana.actualizarContadoresCasillas(casillasJ1, casillasJ2);

                    // 8. Pasamos el turno en el backend de forma segura para la siguiente jugada
                    partidaService.cambiarTurno();

                    // 🔺 9. NUEVO: Actualizamos la posición visual del triángulo rojo
                    ventana.actualizarTurnoVisual(partidaService.getTurnoActualNum());

                    // Refresco general de la UI
                    ventana.revalidate();
                    ventana.repaint();

                    if (tableroLogico.esLleno()){
                        Jugador ganador = partidaService.getGanador();
                        ventana.mostrarCartelGanador(ganador);
                    }
                } else {
                    JOptionPane.showMessageDialog(ventana, "¡Movimiento inválido según las reglas!");
                }
            }
        }
    }

    /**
     * Compara el backend con el frontend y ejecuta la animación en las cartas capturadas
     */
    private void refrescarYAnimarTablero(VentanaJuego ventana, Tablero tableroLogico) {
        Component[] casillasVisuales = ventana.getTablero3x3().getComponents();

        for (int i = 0; i < casillasVisuales.length; i++) {
            if (casillasVisuales[i] instanceof CasillaPanel) {
                CasillaPanel casillaPanelVisual = (CasillaPanel) casillasVisuales[i];
                int f = i / 3;
                int c = i % 3;

                Casilla casillaLogica = tableroLogico.getCasilla(f, c);

                // Si la casilla del modelo tiene un Pokémon y la casilla visual también tiene la carta...
                if (casillaLogica != null && !casillaLogica.isVacia() && casillaPanelVisual.getComponentCount() > 0) {
                    VistaCarta cartaVisual = (VistaCarta) casillaPanelVisual.getComponent(0);
                    Jugador propietarioLogico = casillaLogica.getPropietario();

                    // 🛑 BUG 2 SOLUCIONADO: Mapeo estricto del propietario real
                    String keyDueñoReal = (propietarioLogico.getId() == 1) ? "J1" : "J2";

                    // Si el dueño visual en la pantalla quedó desactualizado respecto al backend, se captura
                    if (!cartaVisual.getJugador().equals(keyDueñoReal)) {
                        System.out.println("🔥 ¡Captura detectada en [" + f + "," + c + "] -> Cambiando a " + keyDueñoReal);
                        animarCapturaCarta(cartaVisual, keyDueñoReal);
                    }
                }
            }
        }
    }

    /**
     * Animación de captura por parpadeo
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
                carta.setBackground(new Color(31, 41, 55));
            }
            carta.repaint();

            if (tics[0] >= 6) {
                timerAnimacion.stop();
                carta.setBackground(colorOriginalFondo);
                carta.cambiarPropietario(nuevoDueño); // 🎯 Aplica el color definitivo corregido
            }
        });

        timerAnimacion.start();
    }
}