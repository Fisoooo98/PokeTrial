package Model.Entities;

public class Tablero {

    public static final int FILAS = 3;
    public static final int COLUMNAS = 3;

    private Casilla[][] casillas;

    public Tablero() {
        this.casillas = new Casilla[FILAS][COLUMNAS];
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                this.casillas[f][c] = new Casilla();
            }
        }
    }

    //Metodos
    public boolean esLleno() {
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                if (this.casillas[f][c] == null || this.casillas[f][c].isVacia()) {
                    return false;
                }
            }
        }
        return true;
    }

    public int contarCasillasPorJugador(Jugador jugador) {
        int contador = 0;
        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                Casilla casilla = this.casillas[f][c];
                if (!casilla.isVacia() && casilla.getPropietario().equals(jugador)) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public Casilla getCasilla(int fila, int columna) {
        if (fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS) {
            return casillas[fila][columna];
        }
        return null; // O lanzar una excepción si se sale del tablero
    }
    public Tablero clonar() {
        Tablero copiaTablero = new Tablero();

        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                Casilla casillaOriginal = this.casillas[f][c];

                if (casillaOriginal != null) {
                    // Usamos el método clonar de Casilla para obtener una réplica exacta
                    copiaTablero.setCasilla(casillaOriginal.clonar(), f, c);
                }
            }
        }

        return copiaTablero;
    }

    public Casilla[][] getCasillas() {
        return casillas;
    }

    public void setCasilla(Casilla casillas,int fila,int columna) {
        this.casillas[fila][columna] = casillas;
    }
}
