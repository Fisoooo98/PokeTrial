package View.MenuPrincipal;

import Controller.MenuController;
import javax.swing.*;
import java.awt.*;

public class VentanaMenuPrincipal extends JFrame {
    private final MenuController controlador;
    private JLabel lblDinero;

    public VentanaMenuPrincipal(MenuController controlador) {
        this.controlador = controlador;

        setTitle("Pokémon Triple Triad - Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(17, 24, 39));
        setLayout(new BorderLayout());

        // Header: Nombre y Dinero
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel lblUser = new JLabel("Bienvenido, " + controlador.getJugador().getNombre());
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblUser.setForeground(Color.WHITE);

        lblDinero = new JLabel(controlador.getJugador().getPuntosBatalla() + " PB");
        lblDinero.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblDinero.setForeground(new Color(16, 185, 129)); // Verde Esmeralda

        panelHeader.add(lblUser, BorderLayout.WEST);
        panelHeader.add(lblDinero, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);

        // Centro: Los 3 Botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 20, 0));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

        panelBotones.add(crearBotonMenu("COMBATE", new Color(239, 68, 68), e -> controlador.abrirCombate(this)));
        panelBotones.add(crearBotonMenu("MAZOS", new Color(59, 130, 246), e -> controlador.abrirGestionMazos()));
        panelBotones.add(crearBotonMenu("TIENDA", new Color(245, 158, 11), e -> controlador.abrirTienda(this)));

        add(panelBotones, BorderLayout.CENTER);
    }

    private JButton crearBotonMenu(String texto, Color color, java.awt.event.ActionListener accion) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setBackground(new Color(31, 41, 55));
        btn.setForeground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(color, 2));
        btn.addActionListener(accion);
        return btn;
    }

    public void actualizarDatos(String nombre, int puntos) {
        lblDinero.setText(puntos + " PB");
        repaint();
    }
}