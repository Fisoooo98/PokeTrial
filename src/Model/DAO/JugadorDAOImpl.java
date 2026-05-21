package Model.DAO;

import Model.Entities.Jugador;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JugadorDAOImpl implements JugadorDAO {
    final String url = "jdbc:postgresql://localhost:5432/pkm_card_game";
    final String user = "postgres";      // o "postgres"
    final String password = "admin";

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
    public Jugador obtenerJugadorPorId(int idJugador) {
        Jugador jugador = null;
        try{
            Connection conexion = getConnection();
            Statement sentencia = conexion.createStatement();
            ResultSet rs = sentencia.executeQuery("SELECT * FROM jugadores WHERE id = " + idJugador);
            while (rs.next()) {
                jugador = new Jugador(rs.getInt(1),rs.getString(2),rs.getInt(3));
            }
            conexion.close();
        } catch (SQLException e) {
            Logger.getLogger(JugadorDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }

        return jugador;
    }

    @Override
    public Jugador obtenerJugadorPorNombre(String nombre) {
        Jugador jugador = null;
        String sql = "SELECT * FROM jugadores WHERE username = ?";
        try (Connection conexion = getConnection();PreparedStatement ps = conexion.prepareStatement(sql)){
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            conexion.close();
            while (rs.next()) {
                jugador = new Jugador(rs.getInt(1), rs.getString(2), rs.getInt(3));
            }
        } catch (SQLException e) {
            Logger.getLogger(JugadorDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return jugador;
    }

    @Override
    public boolean crearJugador(String nombre) {
        String sql = "INSERT INTO jugadores (username, puntos_batalla) VALUES (?,500)";
        if (obtenerJugadorPorNombre(nombre) != null) {
            return false;
        }else{
            try{
                Connection conexion = getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ps.setString(1, nombre);
                ps.executeUpdate();
                conexion.close();
            }catch (SQLException e) {
                Logger.getLogger(JugadorDAOImpl.class.getName()).log(Level.SEVERE, null, e);
            }
            return true;
        }
    }

    @Override
    public void actualizarPuntosBatalla(int idJugador, int puntosBatalla) {
        String sql = "Update jugadores set puntos_batalla = ? where id= ?";
        try{
            Connection conexion = getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, puntosBatalla);
            ps.setInt(2, idJugador);
            ps.executeUpdate();
            conexion.close();
        } catch (SQLException e) {
            Logger.getLogger(JugadorDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public int obtenerPuntosBatalla(int idJugador) {
        String sql = "SELECT puntos_batalla FROM jugadores WHERE id = ?";
        int puntosBatalla = 0;
        try{
            Connection conexion = getConnection();
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idJugador);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                puntosBatalla = rs.getInt(1);
            }
            conexion.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return puntosBatalla;
    }
}
