package Model.DAO;

import Model.Entities.Jugador;

import java.sql.Connection;

public interface JugadorDAO {
    Connection getConnection();
    Jugador obtenerJugadorPorId(int idJugador);
    Jugador obtenerJugadorPorNombre(String nombre);
    boolean crearJugador(String nombre);
    void actualizarPuntosBatalla(int idJugador,int puntosBatalla);
    int obtenerPuntosBatalla(int idJugador);
}
