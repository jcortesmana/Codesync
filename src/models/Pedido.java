package models;

import java.time.LocalDateTime;

public class Pedido {
    private int numeroPedido;
    private Cliente cliente;
    private Articulo articulo;
    private int cantidad;
    private LocalDateTime fechaHora;
    private boolean enviado;

    // Constructor usado en tu Main: (numero, cliente, articulo, cantidad, LocalDateTime)
    public Pedido(int numeroPedido, Cliente cliente, Articulo articulo, int cantidad, LocalDateTime fechaHora) {
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.fechaHora = fechaHora;
        this.enviado = false;
    }

    // Constructor alternativo sin numero (por si lo necesitas)
    public Pedido(Cliente cliente, Articulo articulo, int cantidad, LocalDateTime fechaHora) {
        this(0, cliente, articulo, cantidad, fechaHora);
    }

    // Getters / setters
    public int getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(int numeroPedido) { this.numeroPedido = numeroPedido; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Articulo getArticulo() { return articulo; }
    public void setArticulo(Articulo articulo) { this.articulo = articulo; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public boolean isEnviado() { return enviado; }
    public void setEnviado(boolean enviado) { this.enviado = enviado; }

    /**
     * calcularTotal: subtotal (precio * cantidad) + gastos de envío (aplica descuento del cliente si existe)
     */
    public double calcularTotal() {
        if (articulo == null) return 0.0;

        // Usa los getters que están definidos en tu modelo Articulo
        double precio = articulo.getPrecio();           // según el Articulo que tenemos en proyecto
        double gastosEnvio = articulo.getGastosEnvio();
        double subtotal = precio * cantidad;

        double descuentoCliente = 0.0;
        if (cliente != null) {
            try {
                descuentoCliente = cliente.calcularDescuentoEnvio();
            } catch (Exception e) {
                descuentoCliente = 0.0;
            }
        }

        double envio = gastosEnvio * (1 - descuentoCliente);
        return subtotal + envio;
    }

    /**
     * esCancelable: devuelve true si no está enviado y la fecha actual es anterior a
     * fechaHora + tiempoPreparacion (en minutos) del artículo.
     */
    public boolean esCancelable() {
        if (enviado) return false;
        if (fechaHora == null || articulo == null) return false;
        int minutosPrep = articulo.getTiempoPreparacion();
        return LocalDateTime.now().isBefore(fechaHora.plusMinutes(minutosPrep));
    }

    @Override
    public String toString() {
        String clienteEmail = (cliente != null) ? cliente.getEmail() : "sin_cliente";
        String codigoArt = (articulo != null) ? articulo.getCodigo() : "sin_articulo";
        return "Pedido{" +
                "numeroPedido=" + numeroPedido +
                ", cliente=" + clienteEmail +
                ", articulo=" + codigoArt +
                ", cantidad=" + cantidad +
                ", fechaHora=" + fechaHora +
                ", enviado=" + enviado +
                '}';
    }
}