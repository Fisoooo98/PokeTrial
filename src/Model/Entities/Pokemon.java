package Model.Entities;

public class Pokemon {
    private int id;
    private String nombre;
    private int atkF;
    private int dF;
    private int atkE;
    private int dE;
    private Tipo tipo;
    private String sprite;

    public Pokemon( int id,String nombre,int atkE, int atkF, int dE, int dF,Tipo tipo,String sprite) {
        this.atkE = atkE;
        this.atkF = atkF;
        this.dE = dE;
        this.dF = dF;
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.sprite = sprite;
    }

    public Pokemon( int id,String nombre,int atkE, int atkF, int dE, int dF,Tipo tipo) {
        this.atkE = atkE;
        this.atkF = atkF;
        this.dE = dE;
        this.dF = dF;
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
    }




    //Getters and Setters
    public int getAtkE() {
        return atkE;
    }

    public void setAtkE(int atkE) {
        this.atkE = atkE;
    }

    public int getAtkF() {
        return atkF;
    }

    public void setAtkF(int atkF) {
        this.atkF = atkF;
    }

    public int getdE() {
        return dE;
    }

    public void setdE(int dE) {
        this.dE = dE;
    }

    public int getdF() {
        return dF;
    }

    public void setdF(int dF) {
        this.dF = dF;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }
//ToString


    @Override
    public String toString() {
        return "Pokemon{" +
                "atkE=" + atkE +
                ", id=" + id +
                ", nombre='" + nombre + '\'' +
                ", atkF=" + atkF +
                ", dF=" + dF +
                ", dE=" + dE +
                ", tipo=" + tipo +
                ", sprite='" + sprite + '\'' +
                '}';
    }
}
