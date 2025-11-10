package models;

public class Articulo {
    private String codigo;
    private String descripcion;
    private double precio;
    private double gastosEnvio;
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
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public double getGastosEnvio() { return gastosEnvio; }
    public void setGastosEnvio(double gastosEnvio) { this.gastosEnvio = gastosEnvio; }
    public int getTiempoPreparacion() { return tiempoPreparacion; }
    public void setTiempoPreparacion(int tiempoPreparacion) { this.tiempoPreparacion = tiempoPreparacion; }

    @Override
    public String toString() {
        return "Articulo{codigo='" + codigo + "', descripcion='" + descripcion + "', precio=" + precio + "}";
    }
}