import java.time.LocalDate;

public class Hortaliza extends Lote implements Perecedero {
    private int vidaUtilDias;

    public Hortaliza(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido, Calidad calidad, int vidaUtilDias) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad);
        this.vidaUtilDias = vidaUtilDias;
    }

    @Override
    public double calcularPago() {
        
        double precioBasePorKg = 0;
        if (getCalidad() == Calidad.A) precioBasePorKg = 4;
        else if (getCalidad() == Calidad.B) precioBasePorKg = 3;
        else if (getCalidad() == Calidad.C) precioBasePorKg = 2;

        double pesoEfectivo = getPesoRecibido() * 0.95;
        return pesoEfectivo * precioBasePorKg;
    }

    @Override
    public String getCategoria() {
        return "Hortaliza";
    }

    @Override
    public boolean requiereCamaraFria() {
        return true; 
    }

    @Override
    public LocalDate getFechaVencimiento() {
        
        return getFechaIngreso().plusDays(vidaUtilDias);
    }

    @Override
    public boolean estaVencido(LocalDate fechaOperacion) {
        
        return fechaOperacion.isAfter(getFechaVencimiento());
    }

    public int getVidaUtilDias() { return vidaUtilDias; }
}
