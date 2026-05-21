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

    public VentanaTiendaSobres(SobreController controlador) {
        this.controlador = controlador;

        setTitle("🏪 Mercado Gacha Pokémon - " + controlador.getJugador().getNombre());
        setSize(1100, 680);
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(17, 24, 39)); // Fondo Slate 900
        setLayout(new BorderLayout(20, 20));

        // --- PANEL SUPERIOR: Título y Puntos de Batalla ---
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(25, 40, 10, 40));

        JLabel lblTitulo = new JLabel("ADQUISICIÓN DE SOBRES DE EXPANSIÓN");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(245, 158, 11)); // Dorado
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

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

        // Pasamos el TipoSobre correcto, descripción, color de sobre y color de texto decorativo
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

        // Área de texto para soportar saltos de línea (\n) en la descripción del sobre
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

        // Evento de click para comprar
        btnComprar.addActionListener(e -> {
            // Desparamos la compra lógica a través del controlador
            List<Carta> cartasNuevas = controlador.procesarCompraSobre(this, tipo);

            if (cartasNuevas != null) {
                // 1. Refrescamos el marcador superior de puntos inmediatamente
                lblPuntosActuales.setText("Tus Puntos: " + controlador.getJugador().getPuntosBatalla() + " PB");

                // 2. Abrimos la interfaz modal de recompensas (PanelRevelarCartas) para mostrar las 5 cartas ganadas
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