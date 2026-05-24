package Model.Entities;

public class Carta {
    private int id;
    private Pokemon pokemon;
    private Rareza rareza;
    private Jugador jugador;


    public Carta(Pokemon pokemon, Rareza rareza, Jugador jugador) {
        this.pokemon = pokemon;
        this.rareza = rareza;
        this.jugador = jugador;
        if (pokemon != null) aplicarMejoraPorRareza(this.pokemon, this.rareza);
    }

    public Carta(Pokemon pokemon, Rareza rareza) {
        this.pokemon = pokemon;
        this.rareza = rareza;
        if (pokemon != null) aplicarMejoraPorRareza(this.pokemon, this.rareza);
    }

    public Carta(int id, Pokemon pokemon, Rareza rareza) {
        this.id = id;
        this.pokemon = pokemon;
        this.rareza = rareza;
        if (pokemon != null) aplicarMejoraPorRareza(this.pokemon, this.rareza);
    }

    private Carta(int id, Pokemon pokemon, Rareza rareza, Jugador jugador, boolean yaAplicado) {
        this.id = id;
        this.pokemon = pokemon;
        this.rareza = rareza;
        this.jugador = jugador;
    }

    private void aplicarMejoraPorRareza(Pokemon pokemon, Rareza rareza) {
        double multiplicador = 1.0;
        switch (rareza) {
            case COMUN      -> multiplicador = 1.0;
            case RARO       -> multiplicador = 1.50;
            case EPICO      -> multiplicador = 1.70;
            case LEGENDARIO -> multiplicador = 2.50;
        }
        if (multiplicador > 1.0 && pokemon != null) {
            pokemon.setAtkF((int) (pokemon.getAtkF() * multiplicador));
            pokemon.setdE((int)   (pokemon.getdE()   * multiplicador));
            pokemon.setAtkE((int) (pokemon.getAtkE() * multiplicador));
            pokemon.setdF((int)   (pokemon.getdF()   * multiplicador));
        }
    }

    public Carta clonar() {
        if (this.pokemon == null) return null; // Carta corrupta, no clonar
        Pokemon pokemonCopiado = new Pokemon(
                this.pokemon.getId(),
                this.pokemon.getNombre(),
                this.pokemon.getAtkE(),
                this.pokemon.getAtkF(),
                this.pokemon.getdE(),
                this.pokemon.getdF(),
                this.pokemon.getTipo(),
                this.pokemon.getSprite()
        );
        return new Carta(this.id, pokemonCopiado, this.rareza, this.jugador, true);
    }

    // ── Getters delegados al Pokemon (con guardia null) ──────────────────────

    public boolean isValida() { return this.pokemon != null; }

    public int getAtkE()      { return pokemon != null ? pokemon.getAtkE() : 0; }
    public int getDE()        { return pokemon != null ? pokemon.getdE()   : 0; }
    public int getAtkF()      { return pokemon != null ? pokemon.getAtkF() : 0; }
    public int getDF()        { return pokemon != null ? pokemon.getdF()   : 0; }
    public Tipo getTipo()     { return pokemon != null ? pokemon.getTipo() : null; }
    public String getSprite() { return pokemon != null ? pokemon.getSprite() : ""; }
    public String getNombre() { return pokemon != null ? pokemon.getNombre() : "???"; }

    // ── Getters / Setters de Carta ───────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Pokemon getPokemon() { return pokemon; }
    public void setPokemon(Pokemon pokemon) { this.pokemon = pokemon; }

    public Rareza getRareza() { return rareza; }
    public void setRareza(Rareza rareza) { this.rareza = rareza; }

    public String mostrarRareza() {
        return switch (this.rareza) {
            case COMUN      -> "★";
            case RARO       -> "★★";
            case EPICO      -> "★★★";
            case LEGENDARIO -> "★★★★";
        };
    }

    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }

    @Override
    public String toString() {
        return "Carta{id=" + id + ", pokemon=" + pokemon + ", rareza=" + rareza + '}';
    }
}