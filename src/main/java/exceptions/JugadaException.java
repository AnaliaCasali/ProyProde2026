package exceptions;

public class JugadaException extends RuntimeException {

    // Constructor básico con mensaje
    public JugadaException(String message) {
        super(message);
    }

    // Constructor que recibe el mensaje y la causa original (ej. una SQLException)
    public JugadaException(String message, Throwable cause) {
        super(message, cause);
    }
}