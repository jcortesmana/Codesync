package models;

import java.time.LocalDate;

public class Pedido {
    private int numero;
    private LocalDate fecha;
    private int cantidad; // cantidad total (por simplicidad)
    private boolean enviado;
    private Cliente cliente;

    public Pedido() {}

    public Pedido(int numero, LocalDate fecha, int cantidad, boolean enviado, Cliente cliente) {
        this.numero = numero;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.enviado = enviado;
        this.cliente = cliente;
    }

    // Constructor usado en tests (sin numero auto)
    public Pedido(LocalDate fecha, int cantidad, boolean enviado, Cliente cliente) {
        this(0, fecha, cantidad, enviado, cliente);
    }

    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public boolean isEnviado() { return enviado; }
    public void setEnviado(boolean enviado) { this.enviado = enviado; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    @Override
    public String toString() {
        return "Pedido{numero=" + numero + ", fecha=" + fecha + ", cantidad=" + cantidad + ", enviado=" + enviado + ", cliente=" + (cliente!=null?cliente.getEmail():"null") + "}";
    }
}