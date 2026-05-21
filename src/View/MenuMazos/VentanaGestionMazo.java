package View.MenuMazos;

import Controller.MazoController;
import Model.Entities.Carta;
import View.Combate.VistaCarta; // Reutilizamos tu componente visual de cartas
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaGestionMazo extends JFrame {

    private final MazoController controlador;

    // Paneles contenedores principales
    private JPanel panelInventario;
    private JPanel panelMazoActual;
    private JComboBox<String> comboFiltroTipos;
    private JLabel lblContadorMazo;

    public VentanaGestionMazo(MazoController controlador) {
        this.controlador = controlador;

        setTitle("🎴 Configuración de Mazo - " + controlador.getJugador().getNombre());
        setSize(1100, 800);
        setMinimumSize(new Dimension(950, 700));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra esta ventana sin tumbar el juego entero
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(17, 24, 39)); // Fondo oscuro Slate 900
        setLayout(new BorderLayout(20, 20));

        // --- 1. PANEL SUPERIOR: Filtros y Cabecera ---
        JPanel panelSuperior = new JPanel(new BorderLayout(20, 0));
        panelSuperior.setOpaque(false);
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel lblTitulo = new JLabel("GESTIÓN DE MAZO DE COMBATE");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(245, 158, 11)); // Color dorado
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        // Selector de Filtros por Tipo (Podés expandir este array con tus tipos reales de Pokémon)
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelFiltro.setOpaque(false);

        JLabel lblFiltro = new JLabel("Filtrar por Tipo:");
        lblFiltro.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblFiltro.setForeground(Color.LIGHT_GRAY);

        String[] tipos = {"TODOS", "FUEGO", "AGUA", "PLANTA", "ELECTRICO", "DRAGON", "NORMAL"};
        comboFiltroTipos = new JComboBox<>(tipos);
        comboFiltroTipos.setPreferredSize(new Dimension(150, 30));
        comboFiltroTipos.addActionListener(e -> refrescarPanelInventarioVisual());

        panelFiltro.add(lblFiltro);
        panelFiltro.add(comboFiltroTipos);
        panelSuperior.add(panelFiltro, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // --- 2. PANEL CENTRAL: Inventario de Cartas (Con Scrollbar) ---
        panelInventario = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        panelInventario.setBackground(new Color(31, 41, 55)); // Caja interna Slate 800

        JScrollPane scrollInventario = new JScrollPane(panelInventario);
        scrollInventario.setBorder(BorderFactory.createLineBorder(new Color(55, 65, 81), 2));
        scrollInventario.getVerticalScrollBar().setUnitIncrement(16); // Scroll suave

        JPanel contenedorCentral = new JPanel(new BorderLayout());
        contenedorCentral.setOpaque(false);
        contenedorCentral.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        contenedorCentral.add(scrollInventario, BorderLayout.CENTER);

        add(contenedorCentral, BorderLayout.CENTER);

        // --- 3. PANEL INFERIOR: Visualización del Mazo Temporal (Las 5 elegidas) ---
        JPanel panelInferiorContenedor = new JPanel(new BorderLayout(0, 10));
        panelInferiorContenedor.setOpaque(false);
        panelInferiorContenedor.setBorder(BorderFactory.createEmptyBorder(10, 30, 25, 30));

        // Cabecera del mazo inferior
        JPanel panelInfoMazo = new JPanel(new BorderLayout());
        panelInfoMazo.setOpaque(false);

        lblContadorMazo = new JLabel("Tu Mazo (0 / 5 Cartas seleccionadas)");
        lblContadorMazo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblContadorMazo.setForeground(Color.WHITE);
        panelInfoMazo.add(lblContadorMazo, BorderLayout.WEST);

        panelInferiorContenedor.add(panelInfoMazo, BorderLayout.NORTH);

        // Receptáculo horizontal para las 5 cartas del mazo
        panelMazoActual = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panelMazoActual.setPreferredSize(new Dimension(0, 180));
        panelMazoActual.setBackground(new Color(31, 41, 55));
        panelMazoActual.setBorder(BorderFactory.createLineBorder(new Color(245, 158, 11), 1, true));
        panelInferiorContenedor.add(panelMazoActual, BorderLayout.CENTER);

        // Botón de Guardado Definitivo
        JButton btnGuardar = new JButton("CONFIRMAR MAZO NUEVO");
        btnGuardar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(16, 185, 129)); // Verde Esmeralda vibrante
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(220, 40));
        btnGuardar.addActionListener(e -> accionGuardarMazo());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        panelBoton.setOpaque(false);
        panelBoton.add(btnGuardar);
        panelInferiorContenedor.add(panelBoton, BorderLayout.SOUTH);

        add(panelInferiorContenedor, BorderLayout.SOUTH);

        // Renderizado inicial al desplegar la ventana
        refrescarTodo();
    }

    /**
     * 🔄 Limpia y redibuja la grilla de cartas disponibles según el filtro seleccionado.
     */
    private void refrescarPanelInventarioVisual() {
        panelInventario.removeAll();
        String filtroActivo = (String) comboFiltroTipos.getSelectedItem();

        // Conseguimos la sublista filtrada desde el controlador
        List<Carta> cartasFiltradas = controlador.filtrarInventarioPorTipo(filtroActivo);

        for (Carta carta : cartasFiltradas) {
            // Instanciamos el componente visual que ya tenías desarrollado
            VistaCarta vistaCard = new VistaCarta(
                    carta.getNombre(), carta.getTipo().toString(), carta.mostrarRareza(),
                    carta.getAtkF(), carta.getDE(), carta.getAtkE(), carta.getDF(),
                    "INVENTARIO", carta.getSprite()
            );

            // Añadimos un listener de clics nativo para cuando se cliquee una carta en el almacén
            vistaCard.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    if (controlador.agregarCartaAlMazo(carta)) {
                        refrescarTodo();
                    } else {
                        JOptionPane.showMessageDialog(VentanaGestionMazo.this,
                                "No se pudo añadir. Comprobá que el mazo no esté lleno (máx 5) y que no tengas repetida esta carta.",
                                "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });
            panelInventario.add(vistaCard);
        }

        panelInventario.revalidate();
        panelInventario.repaint();
    }

    /**
     * 🔄 Limpia y redibuja el espacio inferior que contiene las 5 cartas seleccionadas.
     */
    private void refrescarMazoVisual() {
        panelMazoActual.removeAll();
        List<Carta> mazoTemporal = controlador.getMazoTemporal();

        // Actualizamos el contador dinámico del título
        lblContadorMazo.setText("Tu Mazo (" + mazoTemporal.size() + " / 5 Cartas seleccionadas)");

        for (Carta carta : mazoTemporal) {
            VistaCarta vistaCard = new VistaCarta(
                    carta.getNombre(), carta.getTipo().toString(), carta.mostrarRareza(),
                    carta.getAtkF(), carta.getDE(), carta.getAtkE(), carta.getDF(),
                    "MAZO_PROPIO", carta.getSprite()
            );

            // Le damos una estética de borde dorado suave a las que ya están en el mazo activo
            vistaCard.setBorder(BorderFactory.createLineBorder(new Color(245, 158, 11), 2, true));

            // Si hacen clic en una carta de la barra inferior, se remueve de la selección
            vistaCard.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    controlador.removerCartaDelMazo(carta);
                    refrescarTodo();
                }
            });
            panelMazoActual.add(vistaCard);
        }

        panelMazoActual.revalidate();
        panelMazoActual.repaint();
    }

    private void refrescarTodo() {
        refrescarPanelInventarioVisual();
        refrescarMazoVisual();
    }

    /**
     * Envia la orden final al controlador para impactar los cambios lógicos en la base de datos.
     */
    private void accionGuardarMazo() {
        boolean exito = controlador.guardarMazoDefinitivo();
        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Mazo guardado con éxito! Listo para las batallas.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose(); // Cerramos la pantalla al terminar
        } else {
            JOptionPane.showMessageDialog(this, "Error: Tu mazo debe contener exactamente 5 cartas para ser válido.", "Mazo Incompleto", JOptionPane.ERROR_MESSAGE);
        }
    }
}