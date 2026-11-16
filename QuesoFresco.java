import java.time.LocalDate;

public class QuesoFresco extends Lote implements Perecedero {
    private double porcentajeGrasa;

    public QuesoFresco(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido, Calidad calidad, double porcentajeGrasa) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad);
        this.porcentajeGrasa = porcentajeGrasa;
    }

    @Override
    public double calcularPago() {
       
        double precioBasePorKg = 0;
        if (getCalidad() == Calidad.A) precioBasePorKg = 38;
        else if (getCalidad() == Calidad.B) precioBasePorKg = 30;
        
        double pagoTotal = getPesoRecibido() * (precioBasePorKg - 1.50);
        return pagoTotal;
    }

    @Override
    public String getCategoria() {
        return "Queso fresco";
    }

    @Override
    public boolean requiereCamaraFria() {
        return true; 
    }

    @Override
    public LocalDate getFechaVencimiento() {
        
        return getFechaIngreso().plusDays(21);
    }

    @Override
    public boolean estaVencido(LocalDate fechaOperacion) {
        
        return fechaOperacion.isAfter(getFechaVencimiento());
    }

    public double getPorcentajeGrasa() { return porcentajeGrasa; }
}