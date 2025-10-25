package exceptions;
public class PedidoNoCancelableException extends Exception {
    public PedidoNoCancelableException(String mensaje) {
        super(mensaje);
    }
}