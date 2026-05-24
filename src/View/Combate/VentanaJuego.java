package View.Combate;

import Controller.PartidaController;
import Model.Entities.Jugador;

import javax.swing.*;
import java.awt.*;

public class VentanaJuego extends JFrame {

    private JPanel tablero3x3;
    public JPanel manoIzquierdaJ1;
    public JPanel manoDerechaJ2;
    private VistaCarta cartaSeleccionada = null;
    private PartidaController controlador;

    private JLabel lblNombreJ1, lblNombreJ2;
    private JLabel lblContadorJ1, lblContadorJ2;
    private IndicadorTurnoPanel indicadorJ1, indicadorJ2;

    public VentanaJuego(Jugador j1, Jugador j2) {
        setTitle("⚡ Pokémon Triple Triad - Battle Arena ⚡");
        // 🔑 FIX: DISPOSE_ON_CLOSE en vez de EXIT_ON_CLOSE para no matar la JVM
        // al cerrar esta ventana cuando venimos del menú principal
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1150, 900);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(17, 24, 39));
        setLayout(new BorderLayout(25, 10));

        // --- PANEL SUPERIOR ---
        JPanel panelSuperior = new JPanel(new GridLayout(1, 2, 50, 0));
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        JPanel bloqueJ1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bloqueJ1.setOpaque(false);
        indicadorJ1 = new IndicadorTurnoPanel();
        lblNombreJ1 = new JLabel(j1.getNombre());
        lblNombreJ1.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblNombreJ1.setForeground(new Color(59, 130, 246));
        bloqueJ1.add(indicadorJ1);
        bloqueJ1.add(lblNombreJ1);

        JPanel bloqueJ2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bloqueJ2.setOpaque(false);
        lblNombreJ2 = new JLabel(j2.getNombre());
        lblNombreJ2.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblNombreJ2.setForeground(new Color(239, 68, 68));
        indicadorJ2 = new IndicadorTurnoPanel();
        bloqueJ2.add(lblNombreJ2);
        bloqueJ2.add(indicadorJ2);

        panelSuperior.add(bloqueJ1);
        panelSuperior.add(bloqueJ2);
        add(panelSuperior, BorderLayout.NORTH);

        // --- MANOS LATERALES ---
        JPanel contenedorIzquierdo = new JPanel(new BorderLayout());
        contenedorIzquierdo.setOpaque(false);
        manoIzquierdaJ1 = crearPanelMano();
        lblContadorJ1 = new JLabel("Casillas Conquistadas: 0", SwingConstants.CENTER);
        lblContadorJ1.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblContadorJ1.setForeground(Color.LIGHT_GRAY);
        lblContadorJ1.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        contenedorIzquierdo.add(manoIzquierdaJ1, BorderLayout.CENTER);
        contenedorIzquierdo.add(lblContadorJ1, BorderLayout.SOUTH);

        JPanel contenedorDerecho = new JPanel(new BorderLayout());
        contenedorDerecho.setOpaque(false);
        manoDerechaJ2 = crearPanelMano();
        lblContadorJ2 = new JLabel("Casillas Conquistadas: 0", SwingConstants.CENTER);
        lblContadorJ2.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblContadorJ2.setForeground(Color.LIGHT_GRAY);
        lblContadorJ2.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        contenedorDerecho.add(manoDerechaJ2, BorderLayout.CENTER);
        contenedorDerecho.add(lblContadorJ2, BorderLayout.SOUTH);

        add(contenedorIzquierdo, BorderLayout.WEST);
        add(contenedorDerecho, BorderLayout.EAST);

        // --- TABLERO 3x3 ---
        JPanel contenedorTablero = new JPanel(new GridBagLayout());
        contenedorTablero.setOpaque(false);
        tablero3x3 = new JPanel(new GridLayout(3, 3, 15, 15));
        tablero3x3.setPreferredSize(new Dimension(520, 520));
        tablero3x3.setBackground(new Color(31, 41, 55));
        tablero3x3.setBorder(BorderFactory.createLineBorder(new Color(245, 158, 11), 3, true));

        for (int i = 0; i < 9; i++) {
            tablero3x3.add(new CasillaPanel(this, i / 3, i % 3));
        }
        contenedorTablero.add(tablero3x3);
        add(contenedorTablero, BorderLayout.CENTER);
    }

    private JPanel crearPanelMano() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        panel.setPreferredSize(new Dimension(240, 0));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return panel;
    }

    public void inicializarMarcadores(String nombreJ1, String nombreJ2, int turnoInicial) {
        lblNombreJ1.setText(nombreJ1);
        lblNombreJ2.setText(nombreJ2);
        actualizarTurnoVisual(turnoInicial);
    }

    public void actualizarTurnoVisual(int turnoActual) {
        indicadorJ1.setActivo(turnoActual == 1);
        indicadorJ2.setActivo(turnoActual == 2);
    }

    public void actualizarContadoresCasillas(int scoreJ1, int scoreJ2) {
        lblContadorJ1.setText("Casillas Conquistadas: " + scoreJ1);
        lblContadorJ2.setText("Casillas Conquistadas: " + scoreJ2);
    }

    /**
     * Repuebla los paneles laterales filtrando cartas con Pokémon nulo
     * para evitar NullPointerException al renderizar.
     */
    public void actualizarManoVisual(String jugadorKey) {
        JPanel panelMano = jugadorKey.equals("J1") ? manoIzquierdaJ1 : manoDerechaJ2;
        panelMano.removeAll();

        java.util.List<Model.Entities.Carta> mazoLogico = jugadorKey.equals("J1")
                ? controlador.getPartidaService().getMazoJ1()
                : controlador.getPartidaService().getMazoJ2();

        if (mazoLogico != null) {
            for (Model.Entities.Carta carta : mazoLogico) {
                // 🔑 FIX: Saltamos cartas con Pokémon nulo (error de carga de BD/API)
                if (carta.getPokemon() == null) {
                    System.err.println("⚠️ Carta con Pokémon nulo omitida del mazo visual: ID=" + carta.getId());
                    continue;
                }
                panelMano.add(new VistaCarta(
                        carta.getNombre(),
                        carta.getTipo().toString(),
                        carta.mostrarRareza(),
                        carta.getAtkF(),
                        carta.getDE(),
                        carta.getAtkE(),
                        carta.getDF(),
                        jugadorKey,
                        carta.getSprite()
                ));
            }
        }
        panelMano.revalidate();
        panelMano.repaint();
    }

    public int obtenerIndiceManoActual(VistaCarta cartaVisual) {
        JPanel panelActivo = cartaVisual.getJugador().equals("J1") ? manoIzquierdaJ1 : manoDerechaJ2;
        Component[] componentes = panelActivo.getComponents();
        for (int i = 0; i < componentes.length; i++) {
            if (componentes[i] == cartaVisual) return i;
        }
        return -1;
    }

    public void mostrarCartelGanador(Jugador ganador) {
        JPanel panelGanador = new JPanel();
        panelGanador.setLayout(new BoxLayout(panelGanador, BoxLayout.Y_AXIS));
        panelGanador.setBackground(new Color(15, 23, 42, 235));
        panelGanador.setBorder(BorderFactory.createLineBorder(new Color(245, 158, 11), 4, true));

        JLabel lblTitulo = new JLabel("¡BATALLA TERMINADA!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(245, 158, 11));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblResultado = new JLabel("", SwingConstants.CENTER);
        lblResultado.setFont(new Font("SansSerif", Font.BOLD, 48));
        lblResultado.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (ganador == null) {
            lblResultado.setText("¡EMPATE TÉCNICO!");
            lblResultado.setForeground(Color.LIGHT_GRAY);
        } else {
            String tagJugador = (ganador.getNombre().equals(lblNombreJ1.getText())) ? "J1" : "J2";
            lblResultado.setText("HA GANADO " + tagJugador);
            lblResultado.setForeground(tagJugador.equals("J1")
                    ? new Color(59, 130, 246)
                    : new Color(239, 68, 68));
        }

        JLabel lblNombre = new JLabel(
                ganador != null ? ganador.getNombre().toUpperCase() : "",
                SwingConstants.CENTER);
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 20));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelGanador.add(Box.createVerticalStrut(25));
        panelGanador.add(lblTitulo);
        panelGanador.add(Box.createVerticalStrut(15));
        panelGanador.add(lblResultado);
        if (ganador != null) {
            panelGanador.add(Box.createVerticalStrut(10));
            panelGanador.add(lblNombre);
        }
        panelGanador.add(Box.createVerticalStrut(25));

        JPanel capaSuperpuesta = new JPanel(new GridBagLayout());
        capaSuperpuesta.setOpaque(false);
        panelGanador.setPreferredSize(new Dimension(550, 220));
        capaSuperpuesta.add(panelGanador);

        BorderLayout layoutActual = (BorderLayout) getContentPane().getLayout();
        Component componenteCentralViejo = layoutActual.getLayoutComponent(BorderLayout.CENTER);
        if (componenteCentralViejo != null) remove(componenteCentralViejo);

        add(capaSuperpuesta, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void setControlador(PartidaController controlador) { this.controlador = controlador; }
    public PartidaController getControlador() { return controlador; }
    public JPanel getTablero3x3() { return tablero3x3; }
    public VistaCarta getCartaSeleccionada() { return cartaSeleccionada; }
    public void setCartaSeleccionada(VistaCarta carta) { this.cartaSeleccionada = carta; }
}