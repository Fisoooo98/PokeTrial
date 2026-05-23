package View.Combate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class VistaCarta extends JPanel {
    private String nombre, tipo, rareza;
    private int up, down, left, right;
    private String jugador;
    private Image spriteImagen = null;
    private boolean esSeleccionada = false;

    // Mapa de colores vibrantes según el tipo de Pokémon
    private static final Map<String, Color> COLORES_TIPO = new HashMap<>();
    static {
        // 🔥 Tus tipos originales (mantenidos y ajustados)
        COLORES_TIPO.put("FUEGO", new Color(185, 28, 28));     // Rojo vibrante (Red 700)
        COLORES_TIPO.put("AGUA", new Color(29, 78, 216));      // Azul océano (Blue 700)
        COLORES_TIPO.put("PLANTA", new Color(21, 128, 61));     // Verde hoja (Green 700)
        COLORES_TIPO.put("ELECTRICO", new Color(202, 138, 4));  // Amarillo/Ámbar oscuro (Yellow 600)
        COLORES_TIPO.put("DRAGON", new Color(109, 40, 217));    // Violeta místico (Violet 700)
        COLORES_TIPO.put("FANTASMA", new Color(76, 29, 149));   // Morado espectral (Purple 800)
        COLORES_TIPO.put("PSIQUICO", new Color(219, 39, 119));  // Fuchsia/Rosa psíquico (Pink 600)
        COLORES_TIPO.put("LUCHA", new Color(146, 64, 14));      // Marrón terracota (Amber 800)

        // 🌟 NUEVOS TIPOS AÑADIDOS
        COLORES_TIPO.put("NORMAL", new Color(100, 116, 139));   // Gris acero/plata (Slate 500)
        COLORES_TIPO.put("HIELO", new Color(6, 182, 212));      // Turquesa/Cian helado (Cyan 500)
        COLORES_TIPO.put("VENENO", new Color(134, 25, 143));    // Púrpura tóxico (Fuchsia 800)
        COLORES_TIPO.put("TIERRA", new Color(120, 53, 4));      // Marrón oscuro tierra (Yellow 900)
        COLORES_TIPO.put("VOLADOR", new Color(96, 165, 250));   // Azul cielo (Blue 400)
        COLORES_TIPO.put("BICHO", new Color(101, 163, 13));     // Verde bicho/oliva (Lime 600)
        COLORES_TIPO.put("ROCA", new Color(120, 113, 108));     // Gris piedra (Stone 500)
        COLORES_TIPO.put("ACERO", new Color(71, 85, 105));      // Gris metalizado (Slate 600)
        COLORES_TIPO.put("HADA", new Color(244, 114, 182));     // Rosa pastel brillante (Pink 400)
        COLORES_TIPO.put("SINIESTRO", new Color(30, 41, 59));   // Negro/Azul noche (Slate 800)
    }

    public VistaCarta(String nombre, String tipo, String rareza, int up, int down, int left, int right, String jugador, String urlSprite) {
        this.nombre = nombre.toUpperCase();
        this.tipo = tipo.toUpperCase();
        this.rareza = rareza;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.jugador = jugador;

        setPreferredSize(new Dimension(190, 135));
        setSize(getPreferredSize());
        setOpaque(false);

        // Hilo secundario para descargar el sprite asíncronamente
        new Thread(() -> {
            try {
                if (urlSprite != null && !urlSprite.isEmpty()) {
                    BufferedImage img = ImageIO.read(new URL(urlSprite));
                    this.spriteImagen = img.getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    repaint();
                }
            } catch (Exception e) {
                System.err.println("No se pudo cargar el sprite de " + nombre);
            }
        }).start();

        // Evento para capturar la selección e iniciar el movimiento
        // Adentro del constructor de VistaCarta o donde manejes su clic nativo:
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {

                // 1. Buscamos de qué ventana es hija esta carta en este momento
                Window ventanaAncestro = SwingUtilities.getWindowAncestor(VistaCarta.this);

                // 2. ⚔️ CASO A: Si está en la pantalla de combate, ejecuta la lógica de selección antigua
                if (ventanaAncestro instanceof View.Combate.VentanaJuego) {
                    View.Combate.VentanaJuego ventanaCombate = (View.Combate.VentanaJuego) ventanaAncestro;

                    // Poné acá la lógica exacta que tenías en tu línea 64, por ejemplo:
                    if (ventanaCombate.getControlador().getPartidaService().getJugadorActual().getId() == 1 && jugador.equals("J1") ||
                            ventanaCombate.getControlador().getPartidaService().getJugadorActual().getId() == 2 && jugador.equals("J2")) {

                        // Deseleccionamos la anterior
                        if (ventanaCombate.getCartaSeleccionada() != null) {
                            ventanaCombate.getCartaSeleccionada().marcarSeleccionada(false);
                        }
                        // Seleccionamos esta
                        marcarSeleccionada(true);
                        ventanaCombate.setCartaSeleccionada(VistaCarta.this);
                    }

                }
                // 🎴 CASO B: Si está en la ventana de gestión de mazo, ¡NO HACEMOS NADA ACÁ!
                else if (ventanaAncestro instanceof View.MenuMazos.VentanaGestionMazo) {
                    // Dejamos que los MouseListeners que le pusimos en 'VentanaGestionMazo'
                    // (los que llaman al controlador para agregar/remover) manejen el flujo.
                    System.out.println("Clic en carta dentro del Gestor de Mazos. Lógica delegada a la ventana.");
                }
            }
        });
    }

    public void marcarSeleccionada(boolean sel) {
        this.esSeleccionada = sel;
        repaint();
    }
    public void cambiarPropietario(String nuevoJugador) {
        this.jugador = nuevoJugador; // Actualizamos el String interno ("J1" o "J2")

        // 🎨 ACTUALIZA EL BORDE O FONDO VISUAL SEGÚN TU DISEÑO
        if (nuevoJugador.equals("J1")) {
            // Por ejemplo, si usabas bordes azules para J1:
            setBorder(BorderFactory.createLineBorder(new Color(59, 130, 246), 3, true));
        } else {
            // Bordes rojos para J2:
            setBorder(BorderFactory.createLineBorder(new Color(239, 68, 68), 3, true));
        }

        // Le decimos a Swing que redibuje esta carta en particular
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Fondo base según tipo elemental
        Color colorFondo = COLORES_TIPO.getOrDefault(tipo, new Color(40, 40, 40));
        g2d.setColor(colorFondo);
        g2d.fillRoundRect(0, 0, w, h, 12, 12);

        // Borde indicador de selección o jugador
        if (esSeleccionada) {
            g2d.setColor(Color.YELLOW);
            g2d.setStroke(new BasicStroke(3));
        } else {
            g2d.setColor(jugador.equals("J1") ? new Color(59, 130, 246) : new Color(239, 68, 68));
            g2d.setStroke(new BasicStroke(2));
        }
        g2d.drawRoundRect(1, 1, w - 2, h - 2, 12, 12);

        // --- HEADER ---
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 11));
        g2d.drawString(jugador, 10, 18);

        // Píldora de Tipo
        g2d.setColor(new Color(255, 255, 255, 60));
        g2d.fillRoundRect(w - 75, 6, 68, 16, 6, 6);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 9));
        FontMetrics fmTipo = g2d.getFontMetrics();
        g2d.drawString(tipo, w - 41 - (fmTipo.stringWidth(tipo) / 2), 18);

        // --- CENTRO: Sprite ---
        if (spriteImagen != null) {
            g2d.drawImage(spriteImagen, 15, 30, null);
        } else {
            g2d.setColor(new Color(255, 255, 255, 40));
            g2d.fillOval(20, 30, 55, 55);
        }

        // --- LADO DERECHO: Estadísticas en Cruz ---
        int centroX = w - 50;
        int centroY = h / 2 + 5;
        g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
        g2d.setColor(Color.WHITE);

        pintarNumero(g2d, String.valueOf(up), centroX, centroY - 24);
        pintarNumero(g2d, String.valueOf(down), centroX, centroY + 24);
        pintarNumero(g2d, String.valueOf(left), centroX - 24, centroY);
        pintarNumero(g2d, String.valueOf(right), centroX + 24, centroY);

        // --- FOOTER: Nombre y Rareza ---
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, h - 28, w, 28);
        g2d.setColor(Color.WHITE);

        g2d.setFont(new Font("SansSerif", Font.BOLD, 11));
        g2d.drawString(nombre, 10, h - 10);

        g2d.setColor(new Color(245, 158, 11));
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));
        FontMetrics fmStars = g2d.getFontMetrics();
        g2d.drawString(rareza, w - 10 - fmStars.stringWidth(rareza), h - 9);
    }

    private void pintarNumero(Graphics2D g2d, String txt, int x, int y) {
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(txt, x - fm.stringWidth(txt) / 2, y + fm.getAscent() / 2 - 2);
    }
    public String getJugador() {
        return jugador;
    }
}