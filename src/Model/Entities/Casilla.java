package Model.Entities;

public class Casilla {
    private Carta carta;
    private Jugador propietario;

    public Casilla() {

    }
    public Casilla(Carta carta, Jugador propietario) {
        this.carta = carta;
        this.propietario = propietario;
    }

    //Metodos
    public boolean isVacia(){
        return this.carta == null;
    }

    //Getters and Setters
    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }

    public Jugador getPropietario() {
        return propietario;
    }

    public void setPropietario(Jugador propietario) {
        this.propietario = propietario;
    }
}
