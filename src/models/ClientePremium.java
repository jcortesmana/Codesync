package models;

public class ClientePremium extends Cliente {
    private double cuotaAnual;
    private double descuento; // porcentaje en 0..1

    public ClientePremium() { super(); }

    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
        this.cuotaAnual = 30.0;   // valores por defecto
        this.descuento = 0.20;
    }

    
   public ClientePremium(int id, String nombre, String domicilio, String nif, String email) {
    super(id, nombre, domicilio, nif, email);
    this.cuotaAnual = 0;  // o poner valores por defecto si quieres
    this.descuento = 0;
}

    public ClientePremium(int id, String nombre, String domicilio, String nif, String email,
                          double cuotaAnual, double descuento) {
        super(id, nombre, domicilio, nif, email);
        this.cuotaAnual = cuotaAnual;
        this.descuento = descuento;
    }

    public double getCuotaAnual() { return cuotaAnual; }
    public void setCuotaAnual(double cuotaAnual) { this.cuotaAnual = cuotaAnual; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    @Override
    public double calcularDescuentoEnvio() {
        return descuento;
    }

    @Override
    public String toString() {
        return super.toString() + " [Tipo: PREMIUM, cuota=" + cuotaAnual + ", dto=" + descuento + "]";
    }
}
