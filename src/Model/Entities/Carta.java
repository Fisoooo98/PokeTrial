package Model.Entities;

public class Carta {
    private int id;
    private Pokemon pokemon;
    private Rareza rareza;
    private Jugador jugador;


    public Carta(Pokemon pokemon,Rareza rareza,Jugador jugador) {
        this.pokemon = pokemon;
        this.rareza = rareza;
        this.jugador = jugador;
    }
    public Carta(Pokemon pokemon,Rareza rareza) {
        this.pokemon = pokemon;
        this.rareza = rareza;
    }

    public Carta(int id, Pokemon pokemon, Rareza rareza) {
        this.id = id;
        this.pokemon = pokemon;
        this.rareza = rareza;
    }


    //Getters and Setters

    //Pokemon
    public int getAtkE() {
        return getPokemon().getAtkE();
    }
    public int getDE() {
        return getPokemon().getdE();
    }
    public int getAtkF() {
        return getPokemon().getAtkF();
    }
    public int getDF() {
        return getPokemon().getdF();
    }

    public Tipo getTipo() {
        return getPokemon().getTipo();
    }
    public String getSprite(){
        return getPokemon().getSprite();
    }
    public String getNombre(){
        return getPokemon().getNombre();
    }

    //Carta
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public Rareza getRareza() {
        return rareza;
    }
    public String mostrarRareza() {
        String rareza = "";
        switch (this.rareza){
            case Rareza.COMUN -> rareza = "★";
            case Rareza.RARO -> rareza = "★★";
            case Rareza.EPICO -> rareza = "★★★";
            case Rareza.LEGENDARIO -> rareza = "★★★★";
        }
        return rareza;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public void setRareza(Rareza rareza) {
        this.rareza = rareza;
    }

    @Override
    public String toString() {
        return "Carta{" +
                "id=" + id +
                ", pokemon=" + pokemon +
                ", rareza=" + rareza +
                '}';
    }
}
