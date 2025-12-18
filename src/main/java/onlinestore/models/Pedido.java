package onlinestore.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "numero")
    private Integer numero;

    @Column(name = "fecha")
    private LocalDate fecha;

    @SuppressWarnings("unused")
    private int cantidad;
    private boolean enviado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoLinea> lineas = new ArrayList<>();

    @Transient
    private Tienda tienda; // para compatibilidad con tu lógica que necesita tienda para calcular totales

    public Pedido() {}

    // constructor usado al crear pedidos (compatibilidad)
    public Pedido(String numeroStr, Cliente cliente, java.time.LocalDateTime fechaHora) {
        this.numero = (numeroStr == null || numeroStr.isBlank()) ? null : tryParseInt(numeroStr);
        this.cliente = cliente;
        this.fecha = fechaHora.toLocalDate();
        this.enviado = false;
    }

    private Integer tryParseInt(String s) {
        try { return Integer.valueOf(s); } catch (Exception e) { return null; }
    }

    // usado por tests y demás: crear con fecha y cliente
    public Pedido(LocalDate fecha, Cliente cliente) {
        this.fecha = fecha;
        this.cliente = cliente;
    }

    public Integer getNumero() { return numero; }
    public String getNumeroPedido() { return numero == null ? null : String.valueOf(numero); } // compatibilidad
    public java.time.LocalDate getFecha() { return fecha; }
    public java.time.LocalDateTime getFechaHora() { return fecha.atStartOfDay(); } // compat con prev. código
    public Cliente getCliente() { return cliente; }
    public void setEnviado(boolean enviado) { this.enviado = enviado; }
    public boolean isEnviado() { return enviado; }
    public List<PedidoLinea> getLineas() { return lineas; }
    public int getCantidad() { return lineas.stream().mapToInt(PedidoLinea::getCantidad).sum(); }

    public void setTienda(Tienda tienda) { this.tienda = tienda; }
    public Tienda getTienda() { return tienda; }

    public void addLinea(Articulo articulo, int cantidad) {
        PedidoLinea pl = new PedidoLinea(articulo.getCodigo(), cantidad);
        pl.setArticulo(articulo);
        pl.setPedido(this);
        lineas.add(pl);
    }

    public double calcularTotal() {
        double total = 0;
        if (tienda == null) return 0;
        for (PedidoLinea pl : lineas) {
            Articulo art = tienda.buscarArticulo(pl.getCodigoArticulo());
            if (art != null) {
                total += art.getPrecio() * pl.getCantidad();
                total += art.getGastosEnvio();
            }
        }
        return total;
    }

    public boolean esCancelable() {
        if (enviado || lineas.isEmpty()) return false;
        Articulo articulo = tienda != null ? tienda.buscarArticulo(lineas.get(0).getCodigoArticulo()) : null;
        if (articulo == null) return false;
        return java.time.LocalDateTime.now().isBefore(getFechaHora().plusMinutes(articulo.getTiempoPreparacion()));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== PEDIDO ").append(getNumeroPedido()).append(" ===\n");
        sb.append("Cliente: ").append(cliente != null ? cliente.getNombre() : "N/A")
                .append(" (").append(cliente != null ? cliente.getEmail() : "N/A").append(")\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Estado: ").append(enviado ? "Enviado" : "Pendiente").append("\n");
        sb.append("Artículos:\n");
        for (PedidoLinea linea : lineas) {
            Articulo art = tienda != null ? tienda.buscarArticulo(linea.getCodigoArticulo()) : null;
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