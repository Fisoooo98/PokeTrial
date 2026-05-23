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
        aplicarMejoraPorRareza(this.pokemon, this.rareza);
    }
    public Carta(Pokemon pokemon,Rareza rareza) {
        this.pokemon = pokemon;
        this.rareza = rareza;
        aplicarMejoraPorRareza(this.pokemon, this.rareza);
    }

    public Carta(int id, Pokemon pokemon, Rareza rareza) {
        this.id = id;
        this.pokemon = pokemon;
        this.rareza = rareza;
        aplicarMejoraPorRareza(this.pokemon, this.rareza);
    }

    //Metodo
    private void aplicarMejoraPorRareza(Pokemon pokemon, Rareza rareza) {
        double multiplicador = 1.0;

        switch (rareza) {
            case COMUN -> multiplicador = 1.0;
            case RARO -> multiplicador = 1.50;      // +50%
            case EPICO -> multiplicador = 1.70;     // +70%
            case LEGENDARIO -> multiplicador = 2.50; // +150%
        }

        if (multiplicador > 1.0 && pokemon != null) {
            // Mapeamos y multiplicamos cada estadística de forma matemática exacta
            pokemon.setAtkF((int) (pokemon.getAtkF() * multiplicador));
            pokemon.setdE((int) (pokemon.getdE() * multiplicador));   // Defensa Especial
            pokemon.setAtkE((int) (pokemon.getAtkE() * multiplicador)); // Ataque Especial
            pokemon.setdF((int) (pokemon.getdF() * multiplicador));   // Defensa Física
        }
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
