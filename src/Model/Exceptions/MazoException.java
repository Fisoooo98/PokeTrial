package Model.Exceptions;

public class MazoException extends RuntimeException {
    public MazoException() {
        super("El mazo debe tener 5 cartas");
    }
}
