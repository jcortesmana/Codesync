package models;
public class ClientePremium extends Cliente {
    private final double cuotaAnual = 30.0;
    private final double descuentoEnvio = 0.20;

    public ClientePremium(String nombre, String domicilio, String nif, String email) {
        super(nombre, domicilio, nif, email);
    }

    @Override
    public double calcularDescuentoEnvio() {
        return descuentoEnvio;
        
    }

    public double getCuotaAnual() { return cuotaAnual; }

    @Override
    public String toString() {
        return super.toString() + " [Tipo: Premium, Cuota anual: " + cuotaAnual +
                ", Descuento envío: " + descuentoEnvio + "]";
    }
   
}