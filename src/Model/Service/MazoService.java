package Model.Service;

import Model.DAO.InventarioDAOImpl;
import Model.Entities.Carta;
import Model.Entities.Jugador;
import Model.Exceptions.MazoException;

import java.util.List;

public class MazoService {
    InventarioDAOImpl inventarioDAO = new InventarioDAOImpl();

    public List<Carta> obtenerInventario(int id) {
        return inventarioDAO.obtenerCartasPorJugador(id);
    }


    public List<Carta> cargarMazoJugador(int idJugador) throws MazoException {
        List<Carta> mazo = inventarioDAO.obtenerMazo(idJugador);
        if (mazo.size() == 5) {
            return mazo;
        }else{
            throw new MazoException();
        }
    }
    public boolean modificarMazo(int idJugador, List<Integer> idsInventarioElegidas){
        if (idsInventarioElegidas == null || idsInventarioElegidas.size() != 5) {
            return false;
        }
        inventarioDAO.actualizarMazo(idJugador, idsInventarioElegidas);
        return true;
    }
}
