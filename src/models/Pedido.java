package models;
import java.time.LocalDateTime;
public class Pedido {
    private int numeroPedido;
    private Cliente cliente;
    private Articulo articulo;
    private int cantidad;
    private LocalDateTime fechaHora;
    private boolean enviado;

    public Pedido(int numeroPedido, Cliente cliente, Articulo articulo, int cantidad, LocalDateTime fechaHora) {
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.fechaHora = fechaHora;
        this.enviado = false;
    }

    public int getNumeroPedido() { return numeroPedido; }
    public Cliente getCliente() { return cliente; }
    public Articulo getArticulo() { return articulo; }
    public int getCantidad() { return cantidad; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public boolean isEnviado() { return enviado; }
    public void setEnviado(boolean enviado) { this.enviado = enviado; }

    public double calcularTotal() {
        double subtotal = articulo.getPrecioVenta() * cantidad;
        double envio = articulo.getGastosEnvio() * (1 - cliente.calcularDescuentoEnvio());
        return subtotal + envio;
    }

    public boolean esCancelable() {
        LocalDateTime limite = fechaHora.plusMinutes(articulo.getTiempoPreparacion());
        return !enviado && LocalDateTime.now().isBefore(limite);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "numeroPedido=" + numeroPedido +
                ", cliente=" + (cliente != null ? cliente.getEmail() : "null") +
                ", articulo=" + (articulo != null ? articulo.getCodigo() : "null") +
                ", cantidad=" + cantidad +
                ", fechaHora=" + fechaHora +
                ", enviado=" + enviado +
                '}';
    }
}