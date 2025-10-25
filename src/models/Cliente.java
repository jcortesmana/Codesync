package models;
public abstract class Cliente {
    private String nombre;
    private String domicilio;
    private String nif;
    private String email; // Identificador único

    public Cliente(String nombre, String domicilio, String nif, String email) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }

    public String getEmail() { return email; }

    public abstract double calcularDescuentoEnvio();

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", domicilio='" + domicilio + '\'' +
                ", nif='" + nif + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    // Implementamos equals y hashCode basados en email (identidad única del cliente)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        return email != null && email.equalsIgnoreCase(cliente.email);
    }

    @Override
    public int hashCode() {
        return email == null ? 0 : email.toLowerCase().hashCode();
    }
}