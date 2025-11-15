package models;

public class ClienteEstandar extends Cliente {

    private int idEstandar;  // <-- id de la tabla cliente_estandar

    public ClienteEstandar() { super(); }

    // Constructor sin id_estandar (cuando creas un cliente nuevo desde la app)
    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    // Constructor cuando recuperas desde la BD
    public ClienteEstandar(int idCliente, int idEstandar, 
                           String nombre, String domicilio, String nif, String email) {
        super(idCliente, nombre, domicilio, nif, email);
        this.idEstandar = idEstandar;
    }

    public int getIdEstandar() { return idEstandar; }
    public void setIdEstandar(int idEstandar) { this.idEstandar = idEstandar; }

    @Override
    public double calcularDescuentoEnvio() {
        return 0.0;
    }

    @Override
    public String toString() {
        return super.toString() + " [Tipo: ESTANDAR, id_estandar=" + idEstandar + "]";
    }
}
