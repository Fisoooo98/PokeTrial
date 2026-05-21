package View.Login;

import Controller.LoginController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaLogin extends JFrame {

    private final LoginController controlador;
    private JTextField txtUsername;
    private JButton btnIngresar;

    public VentanaLogin(LoginController controlador) {
        this.controlador = controlador;

        setTitle("Pokémon Triple Triad - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Configuración estética del panel principal (Fondo Slate 900)
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setBackground(new Color(17, 24, 39));
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        add(panelPrincipal);

        // --- TITULO DE LA APP ---
        JLabel lblTitulo = new JLabel("TRIPLE TRIAD ARENA");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(245, 158, 11)); // Color Dorado
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("Inicia sesión o registra tu Entrenador");
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSubtitulo.setForeground(Color.LIGHT_GRAY);
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- CAMPO DE TEXTO ---
        JLabel lblUserTag = new JLabel("Nombre de Usuario:");
        lblUserTag.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblUserTag.setForeground(Color.WHITE);
        lblUserTag.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(300, 35));
        txtUsername.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtUsername.setBackground(new Color(31, 41, 55)); // Slate 800
        txtUsername.setForeground(Color.WHITE);
        txtUsername.setCaretColor(Color.WHITE);
        txtUsername.setBorder(BorderFactory.createLineBorder(new Color(75, 85, 99), 1));
        txtUsername.setHorizontalAlignment(JTextField.CENTER);

        // Permitir loguearse directamente al pulsar "Enter" en el teclado
        txtUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    controlador.procesarLogin(VentanaLogin.this, txtUsername.getText());
                }
            }
        });

        // --- BOTÓN ACCIÓN ---
        btnIngresar = new JButton("INGRESAR AL ESTADIO");
        btnIngresar.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnIngresar.setBackground(new Color(31, 41, 55));
        btnIngresar.setForeground(new Color(245, 158, 11)); // Letras doradas
        btnIngresar.setBorder(BorderFactory.createLineBorder(new Color(245, 158, 11), 2));
        btnIngresar.setMaximumSize(new Dimension(300, 40));
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnIngresar.addActionListener(e -> controlador.procesarLogin(this, txtUsername.getText()));

        // --- ENSAMBLADO CON ESPACIADORES ---
        panelPrincipal.add(Box.createVerticalStrut(10));
        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(lblSubtitulo);
        panelPrincipal.add(Box.createVerticalStrut(35));
        panelPrincipal.add(lblUserTag);
        panelPrincipal.add(Box.createVerticalStrut(10));
        panelPrincipal.add(txtUsername);
        panelPrincipal.add(Box.createVerticalStrut(25));
        panelPrincipal.add(btnIngresar);
        panelPrincipal.add(Box.createVerticalStrut(10));
    }
}