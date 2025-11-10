package models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Pedido {
    private int numero;
    private LocalDate fecha;             // usado en algunos contextos
    private LocalDateTime fechaHora;     // usado por DAOs y Controlador
    private int cantidad;
    private boolean enviado;
    private Cliente cliente;
    private Articulo articulo;           // nuevo campo

    public Pedido() {}

    // Constructor original (mantiene compatibilidad)
    public Pedido(int numero, LocalDate fecha, int cantidad, boolean enviado, Cliente cliente) {
        this.numero = numero;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.enviado = enviado;
        this.cliente = cliente;
    }

    // Constructor usado en tests (sin numero)
    public Pedido(LocalDate fecha, int cantidad, boolean enviado, Cliente cliente) {
        this(0, fecha, cantidad, enviado, cliente);
    }

    // Nuevo constructor completo (usado por Controlador y DAO)
    public Pedido(int numero, Cliente cliente, Articulo articulo, int cantidad, LocalDateTime fechaHora) {
        this.numero = numero;
        this.cliente = cliente;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.fechaHora = fechaHora;
        this.enviado = false;
        this.fecha = fechaHora.toLocalDate(); // conversión para compatibilidad
    }

    // Getters y setters
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public boolean isEnviado() { return enviado; }
    public void setEnviado(boolean enviado) { this.enviado = enviado; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Articulo getArticulo() { return articulo; }
    public void setArticulo(Articulo articulo) { this.articulo = articulo; }

    @Override
    public String toString() {
        return "Pedido{" +
                "numero=" + numero +
                ", fecha=" + fecha +
                ", cantidad=" + cantidad +
                ", enviado=" + enviado +
                ", cliente=" + (cliente != null ? cliente.getEmail() : "null") +
                ", articulo=" + (articulo != null ? articulo.getCodigo() : "null") +
                '}';
    }
}
