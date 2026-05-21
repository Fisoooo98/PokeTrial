package Test;

import Model.DAO.InventarioDAO;
import Model.DAO.InventarioDAOImpl;
import Model.DAO.JugadorDAOImpl;
import Model.Entities.Rareza;
import Model.Entities.Tipo;
import Model.Service.PokeApiService;

import java.util.List;

public class DAOTEST {
    static void main() {
        InventarioDAOImpl inventarioDAO = new InventarioDAOImpl();
        JugadorDAOImpl jugadorDAO = new JugadorDAOImpl();
        inventarioDAO.añadirCartaInventario(1,3, Rareza.COMUN);
        System.out.println(jugadorDAO.obtenerJugadorPorId(1));

    }
}
