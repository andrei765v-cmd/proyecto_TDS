package es.um.tds.gestionGastos.modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Estrategia de distribución por porcentajes.
 * Cada miembro paga un porcentaje específico del total.
 */
public class DistribucionPorcentaje implements EstrategiaDistribucion {
    private Map<Usuario, Double> porcentajes; // Porcentaje que debe pagar cada miembro (0.0 a 1.0)

    /**
     * Constructor que valida que los porcentajes sumen 100%
     */
    public DistribucionPorcentaje(Map<Usuario, Double> porcentajes) {
        // Validar que los porcentajes sumen aproximadamente 100%
        double sumaPorcentajes = porcentajes.values().stream()
            .mapToDouble(Double::doubleValue)
            .sum();
        
        if (Math.abs(sumaPorcentajes - 1.0) > 0.001) {
            throw new IllegalArgumentException(
                "Los porcentajes deben sumar 100% (1.0). Suma actual: " + sumaPorcentajes);
        }
        
        this.porcentajes = new HashMap<>(porcentajes);
    }

    @Override
    public Map<Usuario, Double> calcular(Usuario pagador, double importe, List<Usuario> miembros) {
        Map<Usuario, Double> distribucion = new HashMap<>();
        
        // El pagador recibe crédito por el total pagado
        distribucion.put(pagador, importe);
        
        // Cada miembro debe pagar su porcentaje
        for (Usuario miembro : miembros) {
            double porcentajeMiembro = porcentajes.getOrDefault(miembro, 0.0);
            double importeMiembro = importe * porcentajeMiembro;
            
            double saldoActual = distribucion.getOrDefault(miembro, 0.0);
            distribucion.put(miembro, saldoActual - importeMiembro);
        }
        
        return distribucion;
    }

    public Map<Usuario, Double> getPorcentajes() {
        return new HashMap<>(porcentajes);
    }

    public void setPorcentaje(Usuario miembro, double porcentaje) {
        if (porcentaje < 0.0 || porcentaje > 1.0) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 1");
        }
        porcentajes.put(miembro, porcentaje);
    }

    @Override
    public String toString() {
        return "Distribución por Porcentaje";
    }
}