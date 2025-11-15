package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private int numeroPedido;
    private Cliente cliente;
    private LocalDateTime fechaHora;
    private boolean enviado;

    private List<PedidoLinea> lineas = new ArrayList<>();

    // ==========================================
    // Constructor usado en Controladores:
    // (numero, cliente, articulo, cantidad, fecha)
    // ==========================================
    public Pedido(int numeroPedido, Cliente cliente, Articulo articulo, int cantidad, LocalDateTime fechaHora) {
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.enviado = false;

        // Convertimos articulo+cantidad en una línea real
        PedidoLinea linea = new PedidoLinea(articulo.getCodigo(), cantidad);
        this.lineas.add(linea);
    }

    // ==========================================
    // Constructor alternativo (sin número)
    // ==========================================
    public Pedido(Cliente cliente, Articulo articulo, int cantidad, LocalDateTime fechaHora) {
        this(0, cliente, articulo, cantidad, fechaHora);
    }

    // ==========================================
    // GETTERS / SETTERS
    // ==========================================

    public int getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(int numeroPedido) { this.numeroPedido = numeroPedido; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public boolean isEnviado() { return enviado; }
    public void setEnviado(boolean enviado) { this.enviado = enviado; }

    public List<PedidoLinea> getLineas() {
        return lineas;
    }

    // ==========================================
    // MULTILINEA
    // ==========================================
    public void addLinea(Articulo articulo, int cantidad) {
        this.lineas.add(new PedidoLinea(articulo.getCodigo(), cantidad));
    }

    // ==========================================
    // LÓGICA: Calcular total
    // ==========================================
    public double calcularTotal() {
        double total = 0;

        for (PedidoLinea linea : lineas) {
            Articulo art = cliente.getTienda().buscarArticulo(linea.getCodigoArticulo());
            total += art.getPrecio() * linea.getCantidad();
            total += art.getGastosEnvio();
        }

        return total;
    }

    // ==========================================
    // Cancelación
    // ==========================================
    public boolean esCancelable() {
        if (enviado) return false;

        Articulo articulo = cliente.getTienda().buscarArticulo(lineas.get(0).getCodigoArticulo());

        if (articulo == null) return false;

        return LocalDateTime.now().isBefore(
                fechaHora.plusMinutes(articulo.getTiempoPreparacion())
        );
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "numeroPedido=" + numeroPedido +
                ", cliente=" + cliente.getEmail() +
                ", lineas=" + lineas.size() +
                ", fechaHora=" + fechaHora +
                ", enviado=" + enviado +
                '}';
    }
}
