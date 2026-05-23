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
    public Casilla clonar() {
        Casilla copiaCasilla = new Casilla();

        // Si la casilla no está vacía, copiamos la carta y el propietario
        if (!this.isVacia()) {
            // Clonamos la carta para que sus estadísticas alteradas en la simulación no afecten a la real
            copiaCasilla.setCarta(this.carta.clonar());
            copiaCasilla.setPropietario(this.propietario); // El jugador puede ser la misma referencia
        }

        return copiaCasilla;
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
