import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Bodega implements Iterable<Lote> {
    private String codigo;
    private double capacidadMaxima;
    private boolean esCamaraFria;
    private List<Lote> lotes;

    public Bodega(String codigo, double capacidadMaxima, boolean esCamaraFria) {
        this.codigo = codigo;
        this.capacidadMaxima = capacidadMaxima;
        this.esCamaraFria = esCamaraFria;
        this.lotes = new ArrayList<>();
    }

    public double getPesoActual() {
        double total = 0;
        for (Lote l : lotes) {
            if (l.getEstado() == EstadoLote.DISPONIBLE) {
                total += l.getPesoDisponible();
            }
        }
        return total;
    }

    public double getCapacidadDisponible() {
        return capacidadMaxima - getPesoActual();
    }

    public boolean puedeRecibir(Lote lote) {
        
        if (lote.requiereCamaraFria() != this.esCamaraFria) {
            return false;
        }
        return getCapacidadDisponible() >= lote.getPesoDisponible();
    }

    public void agregarLote(Lote lote) {
        if (puedeRecibir(lote)) {
            lotes.add(lote);
            
            Collections.sort(lotes);
        } else {
            throw new IllegalArgumentException("La bodega no cumple con las condiciones de espacio o tipo.");
        }
    }

    @Override
    public Iterator<Lote> iterator() {
        
        Collections.sort(lotes);
        return lotes.iterator();
    }

    public String getCodigo() { return codigo; }
    public double getCapacidadMaxima() { return capacidadMaxima; }
    public boolean isEsCamaraFria() { return esCamaraFria; }
    public List<Lote> getLotes() { return lotes; }
}
