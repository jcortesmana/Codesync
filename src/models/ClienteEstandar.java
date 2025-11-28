package models;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ESTANDAR")
public class ClienteEstandar extends Cliente {

    public ClienteEstandar() { super(); }

    public ClienteEstandar(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    // constructor con id para lecturas desde BD si necesitases
    public ClienteEstandar(int id, int idEstandar, String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
        setId(id);
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
