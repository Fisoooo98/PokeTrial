package Model.DAO;

import Model.Entities.Carta;
import Model.Entities.Pokemon;
import Model.Entities.Rareza;
import Model.Service.PokeApiService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InventarioDAOImpl implements InventarioDAO {
    final String url = "jdbc:postgresql://localhost:5432/pkm_card_game";
    final String user = "postgres";      // o "postgres"
    final String password = "admin";
    PokeApiService api = new PokeApiService();
    @Override
    public Connection getConnection() {
        Connection conexion = null;
        try{
            conexion = DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            Logger.getLogger(JugadorDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return conexion;
    }

    @Override
    public boolean añadirCartaInventario(int idJugador, int idPokemonApi, Rareza rareza) {
        Connection conexion = getConnection();
        String sql = "INSERT INTO inventarios (id_jugador, id_pokemon_api , rareza) VALUES (?, ?, ?)";
        try{
            PreparedStatement ps  = conexion.prepareStatement(sql);
            ps.setInt(1, idJugador);
            ps.setInt(2, idPokemonApi);
            ps.setObject(3,rareza.toString(),java.sql.Types.OTHER);
            int filasAfectadas = ps.executeUpdate();
            conexion.close();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            Logger.getLogger(JugadorDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    @Override
    public List<Carta> obtenerCartasPorJugador(int idJugador) {
        Connection conexion = getConnection();
        String sql = "SELECT * FROM inventarios WHERE id_jugador = ?";
        List<Carta> cartas = new ArrayList<>();
        try{
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idJugador);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cartas.add(new Carta(rs.getInt(1),api.cargarDatosPokemon(rs.getInt(3)),Rareza.valueOf(rs.getString(4).toUpperCase())));
            }
            conexion.close();
        } catch (SQLException e) {
            Logger.getLogger(JugadorDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return cartas;
    }

    @Override
    public void guardarCartasEnInventario(int id, List<Carta> cartasObtenidas) {
        for (Carta carta : cartasObtenidas) {
            añadirCartaInventario(id, carta.getPokemon().getId(), carta.getRareza());
        }
    }

    @Override
    public void actualizarMazo(int idJugador, List<Integer> idsInventarioElegidas) {
        Connection conexion = getConnection();
        //Update inventarios set en_mazo=TRUE where id = 1 and id_jugador = 1
        String sql="Update inventarios set en_mazo=TRUE where id = ? and id_jugador = ?";
        String sqlReset = "UPDATE inventarios SET en_mazo = FALSE WHERE id_jugador = ?";
       try{
           //Reset
           PreparedStatement psReset = conexion.prepareStatement(sqlReset);
           psReset.setInt(1, idJugador);
           psReset.executeUpdate();
           psReset.close();
           //Update
           PreparedStatement ps = conexion.prepareStatement(sql);
           ps.setInt(2,idJugador);
           for (int c :  idsInventarioElegidas) {
               ps.setInt(1, c);
               ps.executeUpdate();
           }
           ps.close();
           conexion.close();
       } catch (SQLException e) {
           Logger.getLogger(JugadorDAOImpl.class.getName()).log(Level.SEVERE, null, e);
       }
    }

    @Override
    public List<Carta> obtenerMazo(int idJugador) {
        List<Carta> cartas = new ArrayList<>();
        Connection conexion = getConnection();
        String sql = "Select * from inventarios where en_mazo = TRUE and id_jugador= ?";
        try{
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idJugador);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cartas.add(new Carta(rs.getInt(1),api.cargarDatosPokemon(rs.getInt(3)),Rareza.valueOf(rs.getString(4).toUpperCase())));
            }
            ps.close();
            conexion.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cartas;
    }

}
