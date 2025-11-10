package models;

public abstract class Cliente {
    private int id;
    private String nombre;
    private String domicilio;
    private String nif;
    private String email;

    public Cliente() {}

    public Cliente(int id, String nombre, String domicilio, String nif, String email) {
        this.id = id;
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;
    }

    public Cliente(String nombre, String domicilio, String nif, String email) {
        this(0, nombre, domicilio, nif, email);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }
    public String getNif() { return nif; }
    public void setNif(String nif) { this.nif = nif; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public abstract double calcularDescuentoEnvio();

    @Override
    public String toString() {
        return "Cliente{id=" + id + ", nombre='" + nombre + "', email='" + email + "', nif='" + nif + "'}";
    }
}