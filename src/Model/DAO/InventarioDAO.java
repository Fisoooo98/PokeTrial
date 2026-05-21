package Model.DAO;

import Model.Entities.Carta;
import Model.Entities.Rareza;

import java.sql.Connection;
import java.util.List;

public interface InventarioDAO {
    Connection getConnection();
    boolean añadirCartaInventario(int idJugador, int idPokemonApi, Rareza rareza);
    List<Carta> obtenerCartasPorJugador(int idJugador);
    void guardarCartasEnInventario(int id, List<Carta> cartasObtenidas);
    void actualizarMazo(int idJugador,List<Integer> idsInventarioElegidas);
    List<Carta> obtenerMazo(int idJugador);
}
