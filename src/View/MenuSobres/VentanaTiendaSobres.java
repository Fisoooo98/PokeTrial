package View.MenuSobres;

import Controller.SobreController;
import Model.Entities.Carta;
import Model.Entities.TipoSobre;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaTiendaSobres extends JFrame {

    private final SobreController controlador;
    private JLabel lblPuntosActuales;
    private JComboBox<String> comboRegiones; // Selector de región añadido

    public VentanaTiendaSobres(SobreController controlador) {
        this.controlador = controlador;

        setTitle("🏪 Mercado Gacha Pokémon - " + controlador.getJugador().getNombre());
        setSize(1100, 720); // Un poco más de alto para acomodar el JComboBox
        setMinimumSize(new Dimension(1000, 620));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(17, 24, 39)); // Fondo Slate 900
        setLayout(new BorderLayout(20, 20));

        // --- PANEL SUPERIOR: Título, Selector de Región y Puntos de Batalla ---
        JPanel panelSuperior = new JPanel(new BorderLayout(20, 0));
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(25, 40, 10, 40));

        JLabel lblTitulo = new JLabel("ADQUISICIÓN DE SOBRES DE EXPANSIÓN");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(245, 158, 11)); // Dorado
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        // --- SUB-PANEL CENTRAL SUPERIOR: Filtro de Región ---
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelFiltro.setOpaque(false);

        JLabel lblFiltrar = new JLabel("Región:");
        lblFiltrar.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblFiltrar.setForeground(Color.WHITE);

        // Las opciones del desplegable se alinean con tus cases del Service (1, 2, 3, 4)
        String[] regiones = {"Gen 1 - Kanto", "Gen 2 - Johto", "Gen 3 - Hoenn", "Gen 4 - Sinnoh"};
        comboRegiones = new JComboBox<>(regiones);
        comboRegiones.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboRegiones.setBackground(new Color(31, 41, 55)); // Slate 800
        comboRegiones.setForeground(Color.WHITE);
        comboRegiones.setPreferredSize(new Dimension(160, 30));

        panelFiltro.add(lblFiltrar);
        panelFiltro.add(comboRegiones);
        panelSuperior.add(panelFiltro, BorderLayout.CENTER);

        // Contador de monedas/puntos en tiempo real
        lblPuntosActuales = new JLabel("Tus Puntos: " + controlador.getJugador().getPuntosBatalla() + " PB");
        lblPuntosActuales.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblPuntosActuales.setForeground(new Color(16, 185, 129)); // Verde esmeralda
        panelSuperior.add(lblPuntosActuales, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // --- PANEL CENTRAL: Los 3 Exhibidores de Sobres ---
        JPanel panelSobresGrid = new JPanel(new GridLayout(1, 3, 30, 0));
        panelSobresGrid.setOpaque(false);
        panelSobresGrid.setBorder(BorderFactory.createEmptyBorder(15, 40, 40, 40));

        // Añadir las tres tarjetas de sobres a la interfaz
        panelSobresGrid.add(crearTarjetaSobre(TipoSobre.BRONCE, "Costo: 100 PB\nProbabilidades altas de comunes.", new Color(146, 64, 14), new Color(252, 211, 77)));
        panelSobresGrid.add(crearTarjetaSobre(TipoSobre.PLATA, "Costo: 400 PB\nEstadísticas mejoradas garantizadas.", new Color(75, 85, 99), new Color(243, 244, 246)));
        panelSobresGrid.add(crearTarjetaSobre(TipoSobre.ORO, "Costo: 1000 PB\nAlta probabilidad de Legendarios (+50%).", new Color(239, 191, 4), new Color(254, 240, 138)));

        add(panelSobresGrid, BorderLayout.CENTER);
    }

    /**
     * Construye el diseño visual de cada sobre de la tienda de forma dinámica.
     */
    private JPanel crearTarjetaSobre(TipoSobre tipo, String descripcion, Color colorSobre, Color colorTexto) {
        JPanel panelCard = new JPanel();
        panelCard.setLayout(new BoxLayout(panelCard, BoxLayout.Y_AXIS));
        panelCard.setBackground(new Color(31, 41, 55)); // Slate 800
        panelCard.setBorder(BorderFactory.createLineBorder(colorSobre, 3, true));

        // El paquete o envoltorio visual del sobre
        JPanel renderSobre = new JPanel(new GridBagLayout());
        renderSobre.setMaximumSize(new Dimension(170, 230));
        renderSobre.setBackground(colorSobre);
        renderSobre.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel lblTextoSobre = new JLabel("POKÉMON");
        lblTextoSobre.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTextoSobre.setForeground(Color.WHITE);
        renderSobre.add(lblTextoSobre);

        // Textos descriptivos
        JLabel lblNombre = new JLabel("SOBRE " + tipo.toString());
        lblNombre.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblNombre.setForeground(colorTexto);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Área de texto para soportar saltos de línea (\n)
        JTextArea txtDesc = new JTextArea(descripcion);
        txtDesc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtDesc.setForeground(Color.LIGHT_GRAY);
        txtDesc.setEditable(false);
        txtDesc.setOpaque(false);
        txtDesc.setFocusable(false);
        txtDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtDesc.setMaximumSize(new Dimension(220, 45));

        JButton btnComprar = new JButton("ADQUIRIR");
        btnComprar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnComprar.setBackground(colorSobre);
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFocusPainted(false);
        btnComprar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnComprar.setMaximumSize(new Dimension(170, 40));

        // Evento de click para comprar con paso de región
        btnComprar.addActionListener(e -> {
            // El índice seleccionado va de 0 a 3. Sumamos 1 para ajustarlo a tus casos del service (1, 2, 3, 4).
            int regionSeleccionada = comboRegiones.getSelectedIndex() + 1;

            // Disparamos la compra pasando la ventana de contexto, el tipo de sobre y la región capturada
            List<Carta> cartasNuevas = controlador.procesarCompraSobre(this, tipo, regionSeleccionada);

            if (cartasNuevas != null) {
                // 1. Refrescamos el marcador superior de puntos inmediatamente
                lblPuntosActuales.setText("Tus Puntos: " + controlador.getJugador().getPuntosBatalla() + " PB");

                // 2. Abrimos la interfaz modal de premios pasándole las cartas generadas
                PanelRevelarCartas dialogPremios = new PanelRevelarCartas(this, cartasNuevas);
                dialogPremios.setVisible(true);
            }
        });

        // Ensamblado estructurado de componentes con márgenes
        panelCard.add(Box.createVerticalStrut(25));
        panelCard.add(renderSobre);
        panelCard.add(Box.createVerticalStrut(20));
        panelCard.add(lblNombre);
        panelCard.add(Box.createVerticalStrut(10));
        panelCard.add(txtDesc);
        panelCard.add(Box.createVerticalStrut(25));
        panelCard.add(btnComprar);
        panelCard.add(Box.createVerticalStrut(20));

        return panelCard;
    }
}