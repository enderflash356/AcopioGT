import java.time.LocalDate;

public abstract class Lote implements Comparable<Lote> {
    private String codigo;
    private String productor;
    private LocalDate fechaIngreso;
    private double pesoRecibido;
    private double pesoDisponible;
    private Calidad calidad;
    private EstadoLote estado;

    
    public Lote(String codigo, String productor, LocalDate fechaIngreso, double pesoRecibido, Calidad calidad) {
        this.codigo = codigo;
        this.productor = productor;
        this.fechaIngreso = fechaIngreso;
        this.pesoRecibido = pesoRecibido;
        this.pesoDisponible = pesoRecibido; 
        this.calidad = calidad;
        this.estado = EstadoLote.DISPONIBLE;
    }

    
    public abstract double calcularPago();
    public abstract String getCategoria();

    
    public double calcularCostoPorKg() {
        if (pesoRecibido == 0) return 0;
        return calcularPago() / pesoRecibido;
    }

    public boolean requiereCamaraFria() {
        return false; 
    }

    public void despachar(double kg) {
        if (kg <= pesoDisponible) {
            pesoDisponible -= kg;
            if (pesoDisponible == 0) {
                estado = EstadoLote.AGOTADO;
            }
        }
    }

    public double descartar() {
        double perdida = pesoDisponible * calcularCostoPorKg();
        pesoDisponible = 0;
        estado = EstadoLote.DESCARTADO;
        return perdida;
    }

    
    public String getCodigo() { return codigo; }
    public String getProductor() { return productor; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public double getPesoRecibido() { return pesoRecibido; }
    public double getPesoDisponible() { return pesoDisponible; }
    public Calidad getCalidad() { return calidad; }
    public EstadoLote getEstado() { return estado; }

    @Override
    public int compareTo(Lote otro) {
        boolean esPerecedero1 = this instanceof Perecedero;
        boolean esPerecedero2 = otro instanceof Perecedero;

        
        if (esPerecedero1 && !esPerecedero2) return -1;
        if (!esPerecedero1 && esPerecedero2) return 1;

        
        if (esPerecedero1 && esPerecedero2) {
            LocalDate v1 = ((Perecedero) this).getFechaVencimiento();
            LocalDate v2 = ((Perecedero) otro).getFechaVencimiento();
            int cmpVencimiento = v1.compareTo(v2);
            if (cmpVencimiento != 0) return cmpVencimiento;
        } else {
           
            int cmpIngreso = this.fechaIngreso.compareTo(otro.fechaIngreso);
            if (cmpIngreso != 0) return cmpIngreso;
        }

        
        int cmpCalidad = this.calidad.compareTo(otro.calidad); 
        if (cmpCalidad != 0) return cmpCalidad;

        
        return this.codigo.compareTo(otro.codigo);
    }
}
