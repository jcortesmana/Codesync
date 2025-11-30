package onlinestore.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PedidoLineaId implements Serializable {
    @Column(name = "id_pedido")
    private Integer idPedido;

    @Column(name = "codigo_articulo", length = 50)
    private String codigoArticulo;

    public PedidoLineaId() {}
    public PedidoLineaId(Integer idPedido, String codigoArticulo) {
        this.idPedido = idPedido;
        this.codigoArticulo = codigoArticulo;
    }

    public Integer getIdPedido() { return idPedido; }
    public String getCodigoArticulo() { return codigoArticulo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PedidoLineaId)) return false;
        PedidoLineaId that = (PedidoLineaId) o;
        return Objects.equals(idPedido, that.idPedido) && Objects.equals(codigoArticulo, that.codigoArticulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPedido, codigoArticulo);
    }
}