package Model.Entities;

import Model.DAO.InventarioDAOImpl;
import Model.DAO.JugadorDAOImpl;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    private int id;
    private String nombre;
    private int puntosBatalla;
    private List<Carta> coleccion;
    private List<Carta> mazo;
    JugadorDAOImpl jugadorDAO = new JugadorDAOImpl();

    public Jugador() {
        this.mazo = new ArrayList<>();
        this.coleccion = new ArrayList<>();
    }

    public Jugador(int id, String nombre,int  puntosBatalla) {
        this.id = id;
        this.nombre = nombre;
        this.puntosBatalla = 500;
        this.mazo = new ArrayList<>();
        this.coleccion = new ArrayList<>();
    }

    public Jugador(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.puntosBatalla = jugadorDAO.obtenerPuntosBatalla(id);
        this.mazo = new ArrayList<>();
        this.coleccion = new ArrayList<>();
    }
    //Metodos
    public void añadirPuntos(int puntos){
        this.puntosBatalla += puntos;
    }

    public void restarPuntos(int puntos){
        this.puntosBatalla -= puntos;
    }
    //Getters and Setters
    public List<Carta> getColeccion() {
        return coleccion;
    }

    public void setColeccion(List<Carta> coleccion) {
        this.coleccion = coleccion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Carta> getMazo() {
        return mazo;
    }

    public void setMazo(List<Carta> mazo) {
        this.mazo = mazo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntosBatalla() {
        return puntosBatalla;
    }

    public void setPuntosBatalla(int puntosBatalla) {
        this.puntosBatalla = puntosBatalla;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "coleccion=" + coleccion +
                ", id=" + id +
                ", nombre='" + nombre + '\'' +
                ", puntosBatalla=" + puntosBatalla +
                ", mazo=" + mazo +
                '}';
    }
}
