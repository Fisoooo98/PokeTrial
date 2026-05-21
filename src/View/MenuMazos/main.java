package View.MenuMazos;

import Controller.MazoController;
import Model.Entities.Jugador;
import Model.Service.PartidaService;

public class main {
    static void main() {
        PartidaService partidaService = new PartidaService();
        // Código para meter adentro del botón "Gestionar Mazo" de tu menú principal:
        Jugador jugador = new Jugador(1,"Fisooo");
        partidaService.actualizarPuntosBatalla(1,100000);

        // Código para el evento del botón "Ir a la Tienda de Sobres" en tu Menú Principal:
        Controller.SobreController sobreCtrl = new Controller.SobreController(jugador);
        View.MenuSobres.VentanaTiendaSobres tiendaSobres = new View.MenuSobres.VentanaTiendaSobres(sobreCtrl);
        tiendaSobres.setVisible(true);
    }
}
