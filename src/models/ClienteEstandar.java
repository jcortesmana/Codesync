package models;

public class ClienteEstandar extends Cliente {
    public ClienteEstandar() { super(); }
    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }
    public ClienteEstandar(int id, String nombre, String domicilio, String nif, String email) {
        super(id, nombre, domicilio, nif, email);
    }

    @Override
    public double calcularDescuentoEnvio() {
        return 0.0;
    }

    @Override
    public String toString() {
        return super.toString() + " [Tipo: ESTANDAR]";
    }
}