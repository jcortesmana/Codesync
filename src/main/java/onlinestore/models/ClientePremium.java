package onlinestore.models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PREMIUM")
@Table(name = "cliente_premium")
@PrimaryKeyJoinColumn(name = "id_cliente")
public class ClientePremium extends Cliente {

    @Column(name = "cuota_anual")
    private Double cuotaAnual;

    @Column(name = "descuento")
    private Double descuento;

    // Constructor vacío obligatorio para JPA
    public ClientePremium() { 
        super(); 
    }

    // Constructor para crear un nuevo cliente premium (sin ID)
    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
        this.cuotaAnual = 30.0;
        this.descuento = 0.20;
    }

    // Constructor completo que incluye el ID (cuando lees de base de datos)
    public ClientePremium(int id, String nombre, String domicilio, String nif, String email, double cuota, double descuento) {
        super(nombre, domicilio, nif, email);
        setId(id);
        this.cuotaAnual = cuota;
        this.descuento = descuento;
    }

    // Constructor que estaba pidiendo tu DAO (el de 5 parámetros)
    public ClientePremium(int id, String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
        setId(id);
        this.cuotaAnual = 30.0;
        this.descuento = 0.20;
    }

    // GETTERS Y SETTERS
    public Double getCuotaAnual() { 
        return cuotaAnual; 
    }

    public void setCuotaAnual(Double cuotaAnual) { 
        this.cuotaAnual = cuotaAnual; 
    }

    public Double getDescuento() { 
        return descuento; 
    }

    public void setDescuento(Double descuento) { 
        this.descuento = descuento; 
    }

    @Override
    public double calcularDescuentoEnvio() {
        return descuento == null ? 0.0 : descuento;
    }

    @Override
    public String toString() {
        return super.toString() + 
               " [Tipo: PREMIUM, cuota=" + cuotaAnual + 
               ", dto=" + descuento + "]";
    }
}
