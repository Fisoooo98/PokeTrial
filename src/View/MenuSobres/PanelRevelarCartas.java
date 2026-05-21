package View.MenuSobres;

import Model.Entities.Carta;
import View.Combate.VistaCarta; // Reutilizamos tu componente visual de cartas
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelRevelarCartas extends JDialog {

    public PanelRevelarCartas(Frame padre, List<Carta> cartasGanadas) {
        super(padre, "¡Sobres Abiertos!", true); // True para hacerlo modal
        setSize(900, 420);
        setLocationRelativeTo(padre);
        getContentPane().setBackground(new Color(11, 15, 26)); // Un tono aún más oscuro para resaltar el brillo (Slate 950)
        setLayout(new BorderLayout(20, 20));

        // Mensaje superior animado por texto
        JLabel lblFelicitaciones = new JLabel("¡SENSACIONAL! HAS DESBLOQUEADO LAS SIGUIENTES CARTAS:", SwingConstants.CENTER);
        lblFelicitaciones.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblFelicitaciones.setForeground(new Color(16, 185, 129)); // Verde Esmeralda de recompensa
        lblFelicitaciones.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(lblFelicitaciones, BorderLayout.NORTH);

        // Panel horizontal donde se colocarán las cartas descubiertas lado a lado
        JPanel panelCartasReveladas = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelCartasReveladas.setOpaque(false);

        // Poblamos el panel con tus componentes visuales exactos
        for (Carta carta : cartasGanadas) {
            VistaCarta vistaCard = new VistaCarta(
                    carta.getNombre(), carta.getTipo().toString(), carta.mostrarRareza(),
                    carta.getAtkF(), carta.getDE(), carta.getAtkE(), carta.getDF(),
                    "TIENDA", carta.getSprite()
            );

            // Le aplicamos un borde de brillo especial según su rareza para que se note el premio
            vistaCard.setBorder(BorderFactory.createLineBorder(new Color(245, 158, 11), 2, true));
            panelCartasReveladas.add(vistaCard);
        }

        add(panelCartasReveladas, BorderLayout.CENTER);

        // Botón inferior para aceptar y cerrar la ventana de premios
        JButton btnAceptar = new JButton("RECLAMAR Y GUARDAR");
        btnAceptar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAceptar.setBackground(new Color(59, 130, 246)); // Azul
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFocusPainted(false);
        btnAceptar.setPreferredSize(new Dimension(200, 40));
        btnAceptar.addActionListener(e -> this.dispose());

        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        panelBoton.setOpaque(false);
        panelBoton.add(btnAceptar);
        add(panelBoton, BorderLayout.SOUTH);
    }
}