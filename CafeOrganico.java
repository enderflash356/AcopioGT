import java.time.LocalDate;

public class CafeOrganico extends CafeEnPergamino {
    private String entidadCertificadora;
    private LocalDate vigenciaSello;

    public CafeOrganico(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido, Calidad calidad, double humedad, String entidadCertificadora, LocalDate vigenciaSello) {
        super(codigo, productor, fechaIngreso, pesoRecibido, calidad, humedad);
        this.entidadCertificadora = entidadCertificadora;
        this.vigenciaSello = vigenciaSello;
    }

    @Override
    public double calcularPago() {
        
        double pagoBase = super.calcularPago();
        double bonificacion = getPesoRecibido() * 0.75;
        return pagoBase + bonificacion;
    }

    @Override
    public String getCategoria() {
        return "Café orgánico";
    }

    public String getEntidadCertificadora() { return entidadCertificadora; }
    public LocalDate getVigenciaSello() { return vigenciaSello; }
}