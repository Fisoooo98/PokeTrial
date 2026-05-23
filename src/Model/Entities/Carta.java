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
        aplicarMejoraPorRareza(this.pokemon, this.rareza);
    }

    public Carta(Pokemon pokemon, Rareza rareza) {
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

    /**
     * Constructor INTERNO solo para clonar: recibe un Pokemon ya con las stats
     * definitivas aplicadas, por lo que NO llama a aplicarMejoraPorRareza().
     */
    private Carta(int id, Pokemon pokemon, Rareza rareza, Jugador jugador, boolean yaAplicado) {
        this.id = id;
        this.pokemon = pokemon;
        this.rareza = rareza;
        this.jugador = jugador;
        // yaAplicado = true → no multiplicamos stats de nuevo
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
            pokemon.setdE((int)  (pokemon.getdE()  * multiplicador));
            pokemon.setAtkE((int)(pokemon.getAtkE() * multiplicador));
            pokemon.setdF((int)  (pokemon.getdF()  * multiplicador));
        }
    }

    /**
     * 🔑 FIX: Clona la carta copiando primero el Pokemon (campos primitivos),
     * de forma que el constructor privado NO vuelva a multiplicar las stats.
     * Antes se pasaba this.pokemon directamente → aplicarMejoraPorRareza()
     * multiplicaba las stats del Pokemon ORIGINAL acumulativamente en cada
     * simulación de la IA, corrompiendo todos los combates futuros.
     */
    public Carta clonar() {
        // Copiamos el Pokemon con las stats ya definitivas (rareza ya aplicada)
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

        // Usamos el constructor privado que NO llama a aplicarMejoraPorRareza
        return new Carta(this.id, pokemonCopiado, this.rareza, this.jugador, true);
    }

    // ── Getters delegados al Pokemon ────────────────────────────────────────

    public int getAtkE()   { return getPokemon().getAtkE(); }
    public int getDE()     { return getPokemon().getdE(); }
    public int getAtkF()   { return getPokemon().getAtkF(); }
    public int getDF()     { return getPokemon().getdF(); }
    public Tipo getTipo()  { return getPokemon().getTipo(); }
    public String getSprite() { return getPokemon().getSprite(); }
    public String getNombre() { return getPokemon().getNombre(); }

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