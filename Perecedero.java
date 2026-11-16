import java.time.LocalDate;

public interface Perecedero {
    LocalDate getFechaVencimiento();
    boolean estaVencido(LocalDate fechaOperacion);
}
