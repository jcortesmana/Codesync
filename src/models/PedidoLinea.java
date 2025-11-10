package models;

public class PedidoLinea {
    private String codigoArticulo;
    private int cantidad;

    public PedidoLinea(String codigoArticulo, int cantidad) {
        this.codigoArticulo = codigoArticulo;
        this.cantidad = cantidad;
    }

    public String getCodigoArticulo() { return codigoArticulo; }
    public int getCantidad() { return cantidad; }
}