package Main;

import Controller.LoginController;
import View.Login.VentanaLogin;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // Ejecutamos la interfaz gráfica dentro del hilo de eventos de Swing (AWT-EventQueue)
        // Esto evita errores de sincronización visual y cuelgues raros.
        SwingUtilities.invokeLater(() -> {
            try {
                // 1. Inicializamos el controlador de inicio de sesión (Login)
                LoginController loginController = new LoginController();

                // 2. Creamos la ventana de Login inyectándole su controlador dedicado
                // Esta ventana se encargará de pedir el username y conectar con tu DAO
                VentanaLogin ventanaLogin = new VentanaLogin(loginController);

                // 3. Hacemos visible la pantalla de acceso
                ventanaLogin.setVisible(true);

                System.out.println("🚀 ¡Pantalla de Login de Pokémon Triple Triad iniciada con éxito!");

            } catch (Exception e) {
                System.err.println("❌ Error crítico al iniciar la aplicación: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}