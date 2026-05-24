package View.Combate;

import Model.Entities.Dificultad;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SelectorDificultadPanel extends JPanel {

    private JComboBox<Dificultad> comboDificultad;
    private JButton btnAceptar;
    private JButton btnCancelar;

    public SelectorDificultadPanel() {
        // Estilo del contenedor principal (Fondo oscuro uniforme)
        this.setBackground(new Color(17, 24, 39)); // Gris muy oscuro/Azulado nocturno
        this.setLayout(new BorderLayout(15, 15));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- ENCABEZADO ---
        JLabel lblTitulo = new JLabel("PREPARARTE PARA LA BATALLA", JLabel.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(251, 191, 36)); // Dorado épico
        this.add(lblTitulo, BorderLayout.NORTH);

        // --- CUERPO CENTRAL ---
        JPanel panelCentral = new JPanel(new GridLayout(2, 1, 10, 10));
        panelCentral.setBackground(new Color(17, 24, 39));

        JLabel lblSubtitulo = new JLabel("Selecciona la dificultad de tus rivales:", JLabel.CENTER);
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblSubtitulo.setForeground(Color.WHITE);
        panelCentral.add(lblSubtitulo);

        // ComboBox Estilizado
        comboDificultad = new JComboBox<>(Dificultad.values());
        comboDificultad.setFont(new Font("SansSerif", Font.BOLD, 14));
        comboDificultad.setBackground(new Color(31, 41, 55)); // Gris intermedio
        comboDificultad.setForeground(Color.WHITE);
        comboDificultad.setSelectedItem(Dificultad.NORMAL); // Por defecto
        comboDificultad.setFocusable(false);

        // Centrar el texto del combobox
        ((JLabel)comboDificultad.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        panelCentral.add(comboDificultad);

        this.add(panelCentral, BorderLayout.CENTER);

        // --- BOTONERA INFERIOR ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(new Color(17, 24, 39));

        btnAceptar = crearBotonEstilizado("ACEPTAR", new Color(16, 185, 129)); // Verde éxito
        btnCancelar = crearBotonEstilizado("CANCELAR", new Color(239, 68, 68)); // Rojo peligro

        panelBotones.add(btnCancelar);
        panelBotones.add(btnAceptar);

        this.add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * 🛠️ Generador de botones estéticos para no repetir código visual
     */
    private JButton crearBotonEstilizado(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.BOLD, 12));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    // --- GETTERS PARA CAPTURAR LOS EVENTOS EN EL MENU ---
    public JComboBox<Dificultad> getComboDificultad() { return comboDificultad; }
    public JButton getBtnAceptar() { return btnAceptar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}
