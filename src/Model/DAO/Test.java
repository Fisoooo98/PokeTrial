package Model.DAO;

import java.sql.*;

public class Test {
    public static void main(String[] args) {
        JugadorDAOImpl jugadorDAO = new JugadorDAOImpl();
        InventarioDAOImpl inventarioDAO = new InventarioDAOImpl();
        System.out.println(inventarioDAO.obtenerMazo(1));
    }
}