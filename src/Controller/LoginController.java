package Controller;

import Model.DAO.JugadorDAOImpl;
import Model.Entities.Jugador;
import View.Login.VentanaLogin;
import View.MenuPrincipal.VentanaMenuPrincipal;
import javax.swing.*;

public class LoginController {

    private final JugadorDAOImpl jugadorDAO;

    public LoginController() {
        this.jugadorDAO = new JugadorDAOImpl();
    }

    /**
     * Procesa el intento de entrada al juego.
     * @param vista Referencia de la ventana de login para interactuar o cerrarla.
     * @param username El nombre ingresado en el campo de texto.
     */
    public void procesarLogin(VentanaLogin vista, String username) {
        if (username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, introduce un nombre de usuario.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 🔍 Buscamos si ya existe el jugador en PostgreSQL
        Jugador jugador = jugadorDAO.obtenerJugadorPorNombre(username.trim());

        if (jugador != null) {
            // 🎉 CASO A: Existe -> Iniciamos sesión directamente
            abrirDashboardPrincipal(vista, jugador);
        } else {
            // 🆕 CASO B: No existe -> Preguntamos si quiere registrarse
            int opcion = JOptionPane.showConfirmDialog(
                    vista,
                    "El usuario '" + username + "' no existe.\n¿Deseas registrar un nuevo entrenador con 500 PB iniciales?",
                    "Registrar Entrenador",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (opcion == JOptionPane.YES_OPTION) {
                boolean creado = jugadorDAO.crearJugador(username.trim());
                if (creado) {
                    // Volvemos a buscarlo para recuperar el ID autogenerado por la BD
                    Jugador nuevoJugador = jugadorDAO.obtenerJugadorPorNombre(username.trim());
                    JOptionPane.showMessageDialog(vista, "¡Entrenador registrado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    abrirDashboardPrincipal(vista, nuevoJugador);
                } else {
                    JOptionPane.showMessageDialog(vista, "Error al crear el jugador. Inténtalo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    /**
     * Salta del Login hacia el Menú Principal inyectando los datos correctos de la BD.
     */
    private void abrirDashboardPrincipal(VentanaLogin loginVista, Jugador jugador) {
        // Inicializamos el MenuController pasándole el ID real del jugador
        MenuController menuCtrl = new MenuController(jugador.getId());

        // Abrimos el dashboard principal
        VentanaMenuPrincipal dashboard = new VentanaMenuPrincipal(menuCtrl);
        dashboard.setVisible(true);

        // Destruimos la ventana de login para que no quede flotando en memoria
        loginVista.dispose();
    }
}