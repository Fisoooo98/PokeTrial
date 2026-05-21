package View.Combate;

import Controller.PartidaController;
import View.Combate.VentanaJuego;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CasillaPanel extends JPanel {
    private int fila, columna;
    private VentanaJuego ventana;

    public CasillaPanel(VentanaJuego ventana, int fila, int columna) {
        this.ventana = ventana;
        this.fila = fila;
        this.columna = columna;

        setBackground(new Color(27, 28, 42));
        setBorder(new LineBorder(new Color(60, 64, 91), 2, true));
        setLayout(new BorderLayout());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Le pedimos el controlador a la ventana y ejecutamos pasándole esta casilla
                if (ventana.getControlador() != null) {
                    ventana.getControlador().EjecutarMoviento(fila, columna, ventana, CasillaPanel.this);
                }
            }
        });
    }
}