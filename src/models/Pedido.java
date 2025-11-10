package models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Pedido {

    private int numero;         // equivale a numero_pedido en DB
    private LocalDate fecha;    // fecha del pedido
    private int cantidad;
    private boolean enviado;
    private Cliente cliente;

    // Constructor base
    public Pedido(LocalDate fecha, int cantidad, boolean enviado, Cliente cliente) {
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.enviado = enviado;
        this.cliente = cliente;
    }

    // Constructor completo (para lecturas desde DB)
    public Pedido(int numero, LocalDate fecha, int cantidad, boolean enviado, Cliente cliente) {
        this.numero = numero;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.enviado = enviado;
        this.cliente = cliente;
    }

    // ======================
    // GETTERS y SETTERS
    // ======================
    public int getNumero() {
        return numero;
    }

    // Método adicional por compatibilidad con el código antiguo
    public int getNumeroPedido() {
        return numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    // ======================
    // LÓGICA DE NEGOCIO
    // ======================

    /**
     * Calcula si el pedido todavía puede cancelarse.
     * Un pedido puede cancelarse si:
     *  - NO ha sido enviado
     *  - y han pasado menos de 2 días desde su fecha
     */
    public boolean esCancelable() {
        LocalDate hoy = LocalDate.now();
        long dias = ChronoUnit.DAYS.between(fecha, hoy);
        return !enviado && dias < 2;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "numero=" + numero +
                ", fecha=" + fecha +
                ", cantidad=" + cantidad +
                ", enviado=" + enviado +
                ", cliente=" + (cliente != null ? cliente.getEmail() : "null") +
                '}';
    }
}