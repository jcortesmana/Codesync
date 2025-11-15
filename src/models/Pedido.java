package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private String numeroPedido;  
    private Cliente cliente;
    private LocalDateTime fechaHora;
    private boolean enviado;

    private Tienda tienda;

    private List<PedidoLinea> lineas = new ArrayList<>();

    // ==========================================
    // Constructor correcto para crear pedidos
    // ==========================================
    public Pedido(String numeroPedido, Cliente cliente,
                  LocalDateTime fechaHora, Tienda tienda) {

        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.fechaHora = fechaHora;
        this.enviado = false;
        this.tienda = tienda;
    }

    // ==========================================
    // Constructor desde BD
    // ==========================================
    public Pedido(String numeroPedido, Cliente cliente,
                  LocalDateTime fechaHora) {

        this(numeroPedido, cliente, fechaHora, null);
    }

    public Pedido(String numeroPedido, Cliente cliente,
              Articulo articulo, int cantidad,
              LocalDateTime fechaHora, Tienda tienda) {

    this.numeroPedido = numeroPedido;
    this.cliente = cliente;
    this.fechaHora = fechaHora;
    this.enviado = false;
    this.tienda = tienda;

    if (articulo != null) {
        this.lineas.add(new PedidoLinea(articulo.getCodigo(), cantidad));
    }
}


    // GETTERS
    public String getNumeroPedido() { return numeroPedido; }
    public Cliente getCliente() { return cliente; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public boolean isEnviado() { return enviado; }
    public List<PedidoLinea> getLineas() { return lineas; }
    public Tienda getTienda() { return tienda; }

    public void setEnviado(boolean enviado) { this.enviado = enviado; }
    public void setTienda(Tienda tienda) { this.tienda = tienda; }

    // Añadir líneas
    public void addLinea(Articulo articulo, int cantidad) {
        this.lineas.add(new PedidoLinea(articulo.getCodigo(), cantidad));
    }

    // Calcular total
    public double calcularTotal() {
        double total = 0;

        for (PedidoLinea linea : lineas) {
            Articulo art = tienda.buscarArticulo(linea.getCodigoArticulo());
            total += art.getPrecio() * linea.getCantidad();
            total += art.getGastosEnvio();
        }

        return total;
    }

    // Cancelación
    public boolean esCancelable() {
        if (enviado || lineas.isEmpty()) return false;

        Articulo articulo = tienda.buscarArticulo(lineas.get(0).getCodigoArticulo());

        return LocalDateTime.now().isBefore(
                fechaHora.plusMinutes(articulo.getTiempoPreparacion())
        );
    }

    @Override
public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("\n=== PEDIDO ").append(numeroPedido).append(" ===\n");
    sb.append("Cliente: ").append(cliente.getNombre())
      .append(" (").append(cliente.getEmail()).append(")\n");
    sb.append("Fecha: ").append(fechaHora).append("\n");
    sb.append("Estado: ").append(enviado ? "Enviado" : "Pendiente").append("\n");
    sb.append("Artículos:\n");

    for (PedidoLinea linea : lineas) {

        Articulo art = tienda != null
                ? tienda.buscarArticulo(linea.getCodigoArticulo())
                : null;

        if (art != null) {
            sb.append(" - ").append(art.getDescripcion())
              .append(" | Cantidad: ").append(linea.getCantidad())
              .append(" | Precio: ").append(art.getPrecio()).append("€\n");
        } else {
            sb.append(" - Código: ").append(linea.getCodigoArticulo())
              .append(" | Cantidad: ").append(linea.getCantidad()).append("\n");
        }
    }

    sb.append("TOTAL: ").append(calcularTotal()).append(" €\n");

    return sb.toString();
}

}
