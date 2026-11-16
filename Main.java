import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
       
        SistemaAcopio sistema = new SistemaAcopio(LocalDate.of(2026, 3, 1));

        
        Bodega bodegaSeca1 = new Bodega("BOD-SECA-1", 2000.0, false);
        Bodega bodegaFria1 = new Bodega("BOD-FRIA-1", 1500.0, true);
        sistema.agregarBodega(bodegaSeca1);
        sistema.agregarBodega(bodegaFria1);

        
        try {
            
            CafeEnPergamino cafe1 = new CafeEnPergamino("CAF-01", "Don Pedro", LocalDate.of(2026, 2, 25), 500.0, Calidad.A, 13.4);
            CafeEnPergamino cafe2 = new CafeEnPergamino("CAF-02", "Doña María", LocalDate.of(2026, 2, 26), 300.0, Calidad.B, 10.0);
            bodegaSeca1.agregarLote(cafe1);
            bodegaSeca1.agregarLote(cafe2);

            
            CafeOrganico cafeOrg1 = new CafeOrganico("ORG-01", "Cooperativa Verapaz", LocalDate.of(2026, 2, 26), 400.0, Calidad.A, 11.0, "BioLatino", LocalDate.of(2026, 12, 31));
            CafeOrganico cafeOrg2 = new CafeOrganico("ORG-02", "Finca El Paraíso", LocalDate.of(2026, 2, 27), 250.0, Calidad.B, 12.5, "EcoCert", LocalDate.of(2026, 10, 15));
            bodegaSeca1.agregarLote(cafeOrg1);
            bodegaSeca1.agregarLote(cafeOrg2);

            
            Cardamomo card1 = new Cardamomo("CARD-01", "Juan Pérez", LocalDate.of(2026, 2, 20), 80.0, Calidad.B, 90.0); 
            Cardamomo card2 = new Cardamomo("CARD-02", "Ana Gómez", LocalDate.of(2026, 2, 22), 150.0, Calidad.A, 75.0);
            bodegaSeca1.agregarLote(card1);
            bodegaSeca1.agregarLote(card2);

            
            Hortaliza hort1 = new Hortaliza("HORT-01", "Hortalizas S.A.", LocalDate.of(2026, 2, 28), 200.0, Calidad.A, 7);
            Hortaliza hort2 = new Hortaliza("HORT-02", "Campesinos Unidos", LocalDate.of(2026, 3, 1), 100.0, Calidad.C, 5);
            bodegaFria1.agregarLote(hort1);
            bodegaFria1.agregarLote(hort2);

            
            QuesoFresco queso1 = new QuesoFresco("QUES-01", "Lácteos del Sur", LocalDate.of(2026, 2, 28), 40.0, Calidad.A, 25.0);
            QuesoFresco queso2 = new QuesoFresco("QUES-02", "Quesos Artesanales", LocalDate.of(2026, 3, 1), 60.0, Calidad.B, 30.0);
            bodegaFria1.agregarLote(queso1);
            bodegaFria1.agregarLote(queso2);

        } catch (Exception e) {
            System.out.println("Error al cargar datos iniciales: " + e.getMessage());
        }

        
        int opcion = 0;
        while (opcion != 6) {
            System.out.println("\n========================================");
            System.out.println("       SISTEMA DE ACOPIO - ACOPIOGT     ");
            System.out.println("========================================");
            System.out.println("Fecha actual de operación: " + sistema.getFechaOperacion());
            System.out.println("1. Registrar nuevo pedido de comprador y despachar");
            System.out.println("2. Avanzar fecha de operación");
            System.out.println("3. Consultar existencias en bodegas (Orden de salida)");
            System.out.println("4. Registrar nueva bodega");
            System.out.println("5. Ver reportes gerenciales");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                if (scanner.hasNextInt()) {
                    opcion = scanner.nextInt();
                    scanner.nextLine(); 

                    switch (opcion) {
                        case 1:
                            System.out.print("Ingrese categoría del producto (ej: Café en pergamino, Cardamomo, Hortaliza, Queso fresco, Café orgánico): ");
                            String cat = scanner.nextLine();
                            System.out.print("Ingrese los kilogramos necesarios: ");
                            double kg = scanner.nextDouble();
                            scanner.nextLine();

                            double cobro = sistema.procesarPedido(cat, kg);
                            System.out.printf("¡Pedido despachado con éxito! Total cobrado: Q%.2f\n", cobro);
                            break;

                        case 2:
                            System.out.print("Ingrese cuántos días desea avanzar la fecha: ");
                            int dias = scanner.nextInt();
                            scanner.nextLine();
                            sistema.avanzarFecha(dias);
                            System.out.println("Fecha avanzada con éxito. Los lotes vencidos han sido descartados.");
                            break;

                        case 3:
                            System.out.println("\n--- ESTADO DE LAS BODEGAS (EN ORDEN DE SALIDA) ---");
                            for (Bodega b : sistema.getBodegas()) {
                                System.out.printf("Bodega: %s | Tipo: %s | Capacidad máx: %.2f kg | Ocupado: %.2f kg\n",
                                    b.getCodigo(), (b.isEsCamaraFria() ? "Cámara Fría" : "Bodega Seca"), b.getCapacidadMaxima(), b.getPesoActual());
                                
                                for (Lote l : b) {
                                    System.out.printf("   -> [%s] %s | Prod: %s | Disp: %.2f / %.2f kg | Estado: %s | Calidad: %s\n",
                                        l.getCodigo(), l.getCategoria(), l.getProductor(), l.getPesoDisponible(), l.getPesoRecibido(), l.getEstado(), l.getCalidad());
                                }
                            }
                            break;

                        case 4:
                            System.out.print("Ingrese código de la bodega: ");
                            String codBodega = scanner.nextLine();
                            System.out.print("Ingrese capacidad máxima en kg: ");
                            double capMax = scanner.nextDouble();
                            System.out.print("¿Es cámara fría? (true/false): ");
                            boolean fria = scanner.nextBoolean();
                            scanner.nextLine();

                            Bodega nuevaBodega = new Bodega(codBodega, capMax, fria);
                            sistema.agregarBodega(nuevaBodega);
                            System.out.println("¡Bodega registrada con éxito!");
                            break;

                        case 5:
                            System.out.println("\n--- REPORTES GERENCIALES ---");
                            System.out.println("Nota: Los pagos, cobros y pérdidas se encuentran registrados en el sistema de control.");
                            
                            break;

                        case 6:
                            System.out.println("Saliendo del sistema. ¡Hasta luego!");
                            break;

                        default:
                            System.out.println("Opción inválida. Intente de nuevo.");
                    }
                } else {
                    System.out.println("Por favor ingrese un número válido.");
                    scanner.next();  
                }
            } catch (Exception e) {
                System.out.println("[ERROR RECHAZADO] Operación inválida: " + e.getMessage());
            }
        }
        scanner.close();
    }
}