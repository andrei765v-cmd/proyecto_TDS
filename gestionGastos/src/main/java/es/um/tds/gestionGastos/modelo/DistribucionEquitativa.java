package es.um.tds.gestionGastos.modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Estrategia de distribución equitativa.
 * Cada miembro paga la misma cantidad (total / número de miembros).
 */
public class DistribucionEquitativa implements EstrategiaDistribucion {

    @Override
    public Map<Usuario, Double> calcular(Usuario pagador, double importe, List<Usuario> miembros) {
        Map<Usuario, Double> distribucion = new HashMap<>();
        
        int numMiembros = miembros.size();
        double importePorPersona = importe / numMiembros;
        
        // El pagador recibe crédito por el total pagado
        distribucion.put(pagador, importe);
        
        // Todos los miembros (incluido el pagador) deben su parte
        for (Usuario miembro : miembros) {
            double saldoActual = distribucion.getOrDefault(miembro, 0.0);
            distribucion.put(miembro, saldoActual - importePorPersona);
        }
        
        return distribucion;
    }

    @Override
    public String toString() {
        return "Distribución Equitativa";
    }
}