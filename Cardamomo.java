import java.time.LocalDate;

public class Cardamomo extends Lote implements Perecedero {
    private double porcentajeGranoVerde;

    public Cardamomo(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido, Calidad calidad, double porcentajeGranoVerde) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad);
        this.porcentajeGranoVerde = porcentajeGranoVerde;
    }

    @Override
    public double calcularPago() {
        
        double precioBasePorKg = 0;
        if (getCalidad() == Calidad.A) precioBasePorKg = 60;
        else if (getCalidad() == Calidad.B) precioBasePorKg = 48;
        else if (getCalidad() == Calidad.C) precioBasePorKg = 35;

        double pago = getPesoRecibido() * precioBasePorKg;

        
        if (porcentajeGranoVerde >= 85.0) {
            pago += (pago * 0.10);
        }
        return pago;
    }

    @Override
    public String getCategoria() {
        return "Cardamomo";
    }

    @Override
    public LocalDate getFechaVencimiento() {
        
        return getFechaIngreso().plusDays(180);
    }

    @Override
    public boolean estaVencido(LocalDate fechaOperacion) {
        
        return fechaOperacion.isAfter(getFechaVencimiento());
    }

    public double getPorcentajeGranoVerde() { return porcentajeGranoVerde; }
}