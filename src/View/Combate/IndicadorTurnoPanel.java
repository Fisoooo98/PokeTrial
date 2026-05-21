package View.Combate;

import javax.swing.*;
import java.awt.*;

public class IndicadorTurnoPanel extends JPanel {
    private boolean activo = false;

    public IndicadorTurnoPanel() {
        setOpaque(false);
        setPreferredSize(new Dimension(20, 20));
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        repaint(); // Forzamos a redibujar el triángulo
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (activo) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(239, 68, 68)); // Rojo vibrante

            // Dibujamos un triángulo apuntando a la derecha
            int[] xPoints = {4, 4, 16};
            int[] yPoints = {3, 17, 10};
            g2d.fillPolygon(xPoints, yPoints, 3);
        }
    }
}