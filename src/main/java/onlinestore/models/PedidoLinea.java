package onlinestore.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pedido_articulo")
public class PedidoLinea {

    @EmbeddedId
    private PedidoLineaId id = new PedidoLineaId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idPedido")
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codigoArticulo")
    @JoinColumn(
            name = "codigo_articulo",
            referencedColumnName = "codigo",
            insertable = false,
            updatable = false
    )
    private Articulo articulo;

    @Column(name = "cantidad")
    private int cantidad;

    public PedidoLinea() {}

    public PedidoLinea(String codigoArticulo, int cantidad) {
        this.id = new PedidoLineaId(null, codigoArticulo);
        this.cantidad = cantidad;
    }

    public PedidoLineaId getId() { return id; }
    public int getCantidad() { return cantidad; }
    public String getCodigoArticulo() { return id.getCodigoArticulo(); }
    public Articulo getArticulo() { return articulo; }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
        if (pedido != null) {
            this.id = new PedidoLineaId(pedido.getNumero(), this.id.getCodigoArticulo());
        }
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
        if (articulo != null) {
            this.id = new PedidoLineaId(this.id.getIdPedido(), articulo.getCodigo());
        }
    }
}
