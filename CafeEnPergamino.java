import java.time.LocalDate;

public class CafeEnPergamino extends Lote {
    private double humedad;

    public CafeEnPergamino(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido, Calidad calidad, double humedad) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad);
        this.humedad = humedad;
    }

    @Override
    public double calcularPago() {
        
        double precioBasePorKg = 0;
        if (getCalidad() == Calidad.A) precioBasePorKg = 12;
        else if (getCalidad() == Calidad.B) precioBasePorKg = 9;
        else if (getCalidad() == Calidad.C) precioBasePorKg = 6;

        double pagoTotalBase = getPesoRecibido() * precioBasePorKg;

        
        if (humedad > 12.0) {
            double exceso = humedad - 12.0;
            
            int puntosExceso = (int) Math.ceil(exceso); 
            double descuentoPorcentaje = puntosExceso * 0.03; 
            pagoTotalBase -= (pagoTotalBase * descuentoPorcentaje);
        }

        return pagoTotalBase;
    }

    @Override
    public String getCategoria() {
        return "Café en pergamino";
    }

    public double getHumedad() {
        return humedad;
    }
}
