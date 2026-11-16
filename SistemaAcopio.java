import java.time.LocalDate;
import java.util.*;

public class SistemaAcopio {
    private LocalDate fechaOperacion;
    private List<Bodega> bodegas;
    private List<Lote> todosLosLotes;
    private Map<String, Double> pagosPorCategoria;
    private Map<String, Double> cobrosPorCategoria;
    private Map<String, Double> perdidasPorCategoria;

    public SistemaAcopio(LocalDate fechaInicial) {
        this.fechaOperacion = fechaInicial;
        this.bodegas = new ArrayList<>();
        this.todosLosLotes = new ArrayList<>();
        this.pagosPorCategoria = new HashMap<>();
        this.cobrosPorCategoria = new HashMap<>();
        this.perdidasPorCategoria = new HashMap<>();
    }

    public void agregarBodega(Bodega bodega) {
        bodegas.add(bodega);
    }

    public String preLiquidar(Lote lote) {
        double pago = lote.calcularPago();
        String vencimientoStr = (lote instanceof Perecedero) ? 
            ((Perecedero) lote).getFechaVencimiento().toString() : "No perecedero";
        
        return String.format("Categoría: %s | Pago estimado: Q%.2f | Vencimiento: %s", 
            lote.getCategoria(), pago, vencimientoStr);
    }

    public void recibirLote(Lote lote, String codigoBodega) {
        
        if (lote.getPesoRecibido() <= 0) {
            throw new IllegalArgumentException("El peso recibido debe ser positivo.");
        }
        if (lote.getFechaIngreso().isAfter(fechaOperacion)) {
            throw new IllegalArgumentException("La fecha de ingreso no puede ser posterior a la fecha de operación.");
        }
        if (lote instanceof QuesoFresco && lote.getCalidad() == Calidad.C) {
            throw new IllegalArgumentException("No se acepta queso fresco de calidad C.");
        }

        Bodega bodegaDestino = null;
        for (Bodega b : bodegas) {
            if (b.getCodigo().equals(codigoBodega)) {
                bodegaDestino = b;
                break;
            }
        }

        if (bodegaDestino == null) {
            throw new IllegalArgumentException("La bodega especificada no existe.");
        }

        if (!bodegaDestino.puedeRecibir(lote)) {
            throw new IllegalArgumentException("La bodega no tiene espacio suficiente o no es compatible (cámara fría).");
        }

        bodegaDestino.agregarLote(lote);
        todosLosLotes.add(lote);

     
        double pago = lote.calcularPago();
        pagosPorCategoria.put(lote.getCategoria(), 
            pagosPorCategoria.getOrDefault(lote.getCategoria(), 0.0) + pago);
    }

    public void avanzarFecha(int dias) {
        if (dias <= 0) throw new IllegalArgumentException("Los días a avanzar deben ser positivos.");
        fechaOperacion = fechaOperacion.plusDays(dias);

       
        for (Bodega b : bodegas) {
            Iterator<Lote> it = b.iterator();
            while (it.hasNext()) {
                Lote l = it.next();
                if (l.getEstado() == EstadoLote.DISPONIBLE && l instanceof Perecedero) {
                    if (((Perecedero) l).estaVencido(fechaOperacion)) {
                        double perdida = l.descartar();
                        perdidasPorCategoria.put(l.getCategoria(), 
                            perdidasPorCategoria.getOrDefault(l.getCategoria(), 0.0) + perdida);
                    }
                }
            }
        }
    }

    public double procesarPedido(String categoria, double kgRequeridos) {
        double kgPendientes = kgRequeridos;
        Map<Lote, Double> kgTomadosPorLote = new HashMap<>();

      
        List<Lote> todosDisponibles = new ArrayList<>();
        for (Bodega b : bodegas) {
            for (Lote l : b) {
                if (l.getCategoria().equalsIgnoreCase(categoria) && l.getEstado() == EstadoLote.DISPONIBLE) {
                    todosDisponibles.add(l);
                }
            }
        }

        
        Collections.sort(todosDisponibles);

        for (Lote l : todosDisponibles) {
            if (kgPendientes <= 0) break;
            double disponibleEnLote = l.getPesoDisponible();
            double aTomar = Math.min(disponibleEnLote, kgPendientes);
            
            kgTomadosPorLote.put(l, aTomar);
            kgPendientes -= aTomar;
        }

        
        if (kgPendientes > 0) {
            throw new IllegalArgumentException("No hay suficiente inventario para cubrir el pedido completo.");
        }

        
        double totalCobradoPedido = 0;
        for (Map.Entry<Lote, Double> entry : kgTomadosPorLote.entrySet()) {
            Lote l = entry.getKey();
            double kg = entry.getValue();
            double costoUnitario = l.calcularCostoPorKg();
            double cobroLote = kg * costoUnitario * 1.35;

            totalCobradoPedido += cobroLote;
            l.despachar(kg);
        }

        cobrosPorCategoria.put(categoria, 
            cobrosPorCategoria.getOrDefault(categoria, 0.0) + totalCobradoPedido);

        return totalCobradoPedido;
    }

    public LocalDate getFechaOperacion() { return fechaOperacion; }
    public List<Bodega> getBodegas() { return bodegas; }
}