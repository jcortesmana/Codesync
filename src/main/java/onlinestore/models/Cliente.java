package onlinestore.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_cliente")
public abstract class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer id;

    private String nombre;
    private String domicilio;
    private String nif;
    @Column(unique = true)
    private String email;

    public Cliente() {}

    public Cliente(String nombre, String domicilio, String nif, String email) {
        this.nombre = nombre;
        this.domicilio = domicilio;
        this.nif = nif;
        this.email = email;
    }

    public Integer getId() { return id == null ? 0 : id; } // compatibilidad con código previo
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
        return "Cliente{id=" + id + ", nombre='" + nombre + "', email='" + email + "'}";
    }
}