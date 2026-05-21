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

    // 🌟 COMPONENTES DEL PANEL DE ESTADO SUPERIOR
    private JLabel lblNombreJ1, lblNombreJ2;
    private JLabel lblContadorJ1, lblContadorJ2;
    private IndicadorTurnoPanel indicadorJ1, indicadorJ2;

    public VentanaJuego() {
        setTitle("⚡ Pokémon Triple Triad - Battle Arena ⚡");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1150, 900); // Subimos un poco el alto para los contadores de abajo
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(17, 24, 39));
        setLayout(new BorderLayout(25, 10));

        // --- 1. PANEL SUPERIOR DE ESTADO (Nombres e Indicadores) ---
        JPanel panelSuperior = new JPanel(new GridLayout(1, 2, 50, 0));
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        // Bloque Jugador 1 (Izquierda)
        JPanel bloqueJ1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bloqueJ1.setOpaque(false);
        indicadorJ1 = new IndicadorTurnoPanel();
        lblNombreJ1 = new JLabel("Jugador 1");
        lblNombreJ1.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblNombreJ1.setForeground(new Color(59, 130, 246)); // Azul
        bloqueJ1.add(indicadorJ1);
        bloqueJ1.add(lblNombreJ1);

        // Bloque Jugador 2 (Derecha)
        JPanel bloqueJ2 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bloqueJ2.setOpaque(false);
        lblNombreJ2 = new JLabel("Jugador 2");
        lblNombreJ2.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblNombreJ2.setForeground(new Color(239, 68, 68)); // Rojo
        indicadorJ2 = new IndicadorTurnoPanel();
        bloqueJ2.add(lblNombreJ2);
        bloqueJ2.add(indicadorJ2); // Triángulo a la derecha del nombre de J2

        panelSuperior.add(bloqueJ1);
        panelSuperior.add(bloqueJ2);
        add(panelSuperior, BorderLayout.NORTH);

        // --- 2. MANOS LATERALES CON CONTADORES ABAJO ---
        // Contenedor Izquierdo (Mano J1 + Contador)
        JPanel contenedorIzquierdo = new JPanel(new BorderLayout());
        contenedorIzquierdo.setOpaque(false);
        manoIzquierdaJ1 = crearPanelMano();
        lblContadorJ1 = new JLabel("Casillas: 0", SwingConstants.CENTER);
        lblContadorJ1.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblContadorJ1.setForeground(Color.LIGHT_GRAY);
        lblContadorJ1.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        contenedorIzquierdo.add(manoIzquierdaJ1, BorderLayout.CENTER);
        contenedorIzquierdo.add(lblContadorJ1, BorderLayout.SOUTH);

        // Contenedor Derecho (Mano J2 + Contador)
        JPanel contenedorDerecho = new JPanel(new BorderLayout());
        contenedorDerecho.setOpaque(false);
        manoDerechaJ2 = crearPanelMano();
        lblContadorJ2 = new JLabel("Casillas: 0", SwingConstants.CENTER);
        lblContadorJ2.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblContadorJ2.setForeground(Color.LIGHT_GRAY);
        lblContadorJ2.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        contenedorDerecho.add(manoDerechaJ2, BorderLayout.CENTER);
        contenedorDerecho.add(lblContadorJ2, BorderLayout.SOUTH);

        add(contenedorIzquierdo, BorderLayout.WEST);
        add(contenedorDerecho, BorderLayout.EAST);

        // --- 3. TABLERO CENTRAL 3x3 ---
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

    /**
     * 🔄 Método para configurar los nombres iniciales y encender el primer triángulo
     */
    public void inicializarMarcadores(String nombreJ1, String nombreJ2, int turnoInicial) {
        lblNombreJ1.setText(nombreJ1);
        lblNombreJ2.setText(nombreJ2);
        actualizarTurnoVisual(turnoInicial);
    }

    /**
     * 🔄 Cambia visualmente cuál triángulo está activo
     */
    public void actualizarTurnoVisual(int turnoActual) {
        indicadorJ1.setActivo(turnoActual == 1);
        indicadorJ2.setActivo(turnoActual == 2);
    }

    /**
     * 🔄 Actualiza los números del marcador de casillas
     */
    public void actualizarContadoresCasillas(int scoreJ1, int scoreJ2) {
        lblContadorJ1.setText("Casillas Conquistadas: " + scoreJ1);
        lblContadorJ2.setText("Casillas Conquistadas: " + scoreJ2);
    }

    // Métodos de sincronización de mano que ya teníamos
    public void actualizarManoVisual(String jugadorKey) {
        JPanel panelMano = jugadorKey.equals("J1") ? manoIzquierdaJ1 : manoDerechaJ2;
        panelMano.removeAll();
        java.util.List<Model.Entities.Carta> mazoLogico = jugadorKey.equals("J1")
                ? controlador.getPartidaService().getMazoJ1()
                : controlador.getPartidaService().getMazoJ2();

        if (mazoLogico != null) {
            for (Model.Entities.Carta carta : mazoLogico) {
                panelMano.add(new VistaCarta(carta.getNombre(), carta.getTipo().toString(), carta.mostrarRareza(),
                        carta.getAtkF(), carta.getDE(), carta.getAtkE(), carta.getDF(), jugadorKey, carta.getSprite()));
            }
        }
        panelMano.revalidate(); panelMano.repaint();
    }

    public int obtenerIndiceManoActual(VistaCarta cartaVisual) {
        JPanel panelActivo = cartaVisual.getJugador().equals("J1") ? manoIzquierdaJ1 : manoDerechaJ2;
        Component[] componentes = panelActivo.getComponents();
        for (int i = 0; i < componentes.length; i++) {
            if (componentes[i] == cartaVisual) return i;
        }
        return -1;
    }
    /**
     * Muestra un cartel gigante en el centro de la pantalla anunciando al ganador
     * de la batalla de cartas.
     * @param ganador El objeto Jugador que se llevó la victoria (puede ser null si es empate).
     */
    public void mostrarCartelGanador(Jugador ganador) {
        // 1. Creamos un panel contenedor que cubrirá el centro de la pantalla
        JPanel panelGanador = new JPanel();
        panelGanador.setLayout(new BoxLayout(panelGanador, BoxLayout.Y_AXIS));
        panelGanador.setBackground(new Color(15, 23, 42, 235)); // Fondo gris oscuro semi-transparente (Slate 900)
        panelGanador.setBorder(BorderFactory.createLineBorder(new Color(245, 158, 11), 4, true)); // Borde dorado

        // 2. Definimos los textos decorativos del cartel
        JLabel lblTitulo = new JLabel("¡BATALLA TERMINADA!", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(245, 158, 11)); // Dorado
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblResultado = new JLabel("", SwingConstants.CENTER);
        lblResultado.setFont(new Font("SansSerif", Font.BOLD, 48)); // Letras gigantes
        lblResultado.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 3. Evaluamos si hubo un ganador o si terminó en tablas (empate)
        if (ganador == null) {
            lblResultado.setText("¡EMPATE TÉCNICO!");
            lblResultado.setForeground(Color.LIGHT_GRAY);
        } else {
            // Averiguamos si el ganador es J1 o J2 comparando con los componentes de tu ventana
            // O si preferís, podés comparar por ID: (ganador.getId() == 1)
            String tagJugador = (ganador.getNombre().equals(lblNombreJ1.getText())) ? "J1" : "J2";

            lblResultado.setText("HA GANADO " + tagJugador);

            // Le asignamos el color de su facción para que quede más pro
            if (tagJugador.equals("J1")) {
                lblResultado.setForeground(new Color(59, 130, 246)); // Azul J1
            } else {
                lblResultado.setForeground(new Color(239, 68, 68)); // Rojo J2
            }
        }

        JLabel lblNombre = new JLabel(ganador != null ? ganador.getNombre().toUpperCase() : "", SwingConstants.CENTER);
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 20));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 4. Estructuramos el diseño del cartel con espaciadores rígidos
        panelGanador.add(Box.createVerticalStrut(25));
        panelGanador.add(lblTitulo);
        panelGanador.add(Box.createVerticalStrut(15));
        panelGanador.add(lblResultado);
        if (ganador != null) {
            panelGanador.add(Box.createVerticalStrut(10));
            panelGanador.add(lblNombre);
        }
        panelGanador.add(Box.createVerticalStrut(25));

        // 5. Lo metemos en un contenedor absoluto intermedio para poder centrarlo sin importar el tamaño de ventana
        JPanel capaSuperpuesta = new JPanel(new GridBagLayout());
        capaSuperpuesta.setOpaque(false); // Transparente para que se vea el tablero por detrás
        panelGanador.setPreferredSize(new Dimension(550, 220)); // Tamaño fijo del cartel central
        capaSuperpuesta.add(panelGanador);

        // 6. Cambiamos el contenido central del BorderLayout de la ventana por este cartelazo
        // Quitamos el tablero central viejo para congelar el juego visualmente
        BorderLayout layoutActual = (BorderLayout) getContentPane().getLayout();
        Component componenteCentralViejo = layoutActual.getLayoutComponent(BorderLayout.CENTER);

        if (componenteCentralViejo != null) {
            remove(componenteCentralViejo);
        }

        add(capaSuperpuesta, BorderLayout.CENTER);

        // 7. Forzamos el redibujado de Swing en caliente
        revalidate();
        repaint();
    }
    public void setControlador(PartidaController controlador) { this.controlador = controlador; }
    public PartidaController getControlador() { return controlador; }
    public JPanel getTablero3x3() { return tablero3x3; }
    public VistaCarta getCartaSeleccionada() { return cartaSeleccionada; }
    public void setCartaSeleccionada(VistaCarta carta) { this.cartaSeleccionada = carta; }
}