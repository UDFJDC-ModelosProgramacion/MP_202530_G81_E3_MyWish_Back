package co.edu.udistrital.mdp.back.exceptions;


/**
 * Excepción personalizada para errores al enviar correos electrónicos.
 */
public class CorreoNoEnviadoException extends RuntimeException {

    /**
     * Constructor con mensaje.
     * @param message Mensaje descriptivo del error.
     */
    public CorreoNoEnviadoException(String message) {
        super(message);
    }

    /**
     * Constructor con mensaje y causa original.
     * @param message Mensaje descriptivo del error.
     * @param cause Causa original del error.
     */
    public CorreoNoEnviadoException(String message, Throwable cause) {
        super(message, cause);
    }
}
