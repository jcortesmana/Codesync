package onlinestore.models;

import jakarta.persistence.*;

@Entity
@Table(name = "articulo")
public class Articulo {
    @Id
    @Column(name = "codigo", length = 50)
    private String codigo;

    private String descripcion;

    @Column(name = "precio")
    private double precio;

    @Column(name = "gastos_envio")
    private double gastosEnvio;

    @Column(name = "tiempo_preparacion")
    private int tiempoPreparacion;

    public Articulo() {}

    public Articulo(String codigo, String descripcion, double precio, double gastosEnvio, int tiempoPreparacion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.gastosEnvio = gastosEnvio;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public double getGastosEnvio() { return gastosEnvio; }
    public int getTiempoPreparacion() { return tiempoPreparacion; }

    @Override
    public String toString() {
        return "Articulo{codigo='" + codigo + "', descripcion='" + descripcion + "', precio=" + precio + "}";
    }
}
