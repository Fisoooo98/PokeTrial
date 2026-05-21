package Model.Exceptions;

public class MonedasInsuficientesException extends RuntimeException {
    public MonedasInsuficientesException(String message) {
        super("No tienes monedas suficientes");
    }
}
