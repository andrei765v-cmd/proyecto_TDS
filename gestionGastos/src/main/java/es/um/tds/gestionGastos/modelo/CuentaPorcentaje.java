package es.um.tds.gestionGastos.modelo;

import java.util.List;
import java.util.Map;

/**
 * Cuenta compartida con distribución por porcentajes personalizados.
 * Cada miembro asume un porcentaje específico del gasto.
 */
public class CuentaPorcentaje extends CuentaCompartida {
    private Map<String, Double> porcentajes; // Porcentaje de cada miembro (debe sumar 100)

    public CuentaPorcentaje(String codigo, String nombre, List<String> miembros, 
                           Map<String, Double> porcentajes) {
        super(codigo, nombre, miembros);
        
        // Validar que los porcentajes sumen 100
        double sumaPorcentajes = porcentajes.values().stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(sumaPorcentajes - 100.0) > 0.01) { // Tolerancia de 0.01 por redondeo
            throw new IllegalArgumentException("Los porcentajes deben sumar 100%. Suma actual: " + sumaPorcentajes);
        }
        
        // Validar que todos los miembros tengan porcentaje
        for (String miembro : miembros) {
            if (!porcentajes.containsKey(miembro)) {
                throw new IllegalArgumentException("Falta el porcentaje para el miembro: " + miembro);
            }
        }
        
        this.porcentajes = porcentajes;
    }

    // Getter
    public Map<String, Double> getPorcentajes() {
        return this.porcentajes;
    }

    @Override
    public void registrarGasto(String pagador, double importe) {
        // Verificar que el pagador es miembro de la cuenta
        if (!getMiembros().contains(pagador)) {
            throw new IllegalArgumentException("El pagador no es miembro de esta cuenta");
        }

        double parteDelPagador = importe * (porcentajes.get(pagador) / 100.0);

        // El pagador recibe el importe total menos su parte proporcional
        actualizarSaldo(pagador, importe - parteDelPagador);

        // Los demás miembros deben su parte proporcional
        for (String miembro : getMiembros()) {
            if (!miembro.equals(pagador)) {
                double parteMiembro = importe * (porcentajes.get(miembro) / 100.0);
                actualizarSaldo(miembro, -parteMiembro);
            }
        }
    }

    /**
     * Obtiene el porcentaje de un miembro específico
     */
    public double getPorcentajeMiembro(String miembro) {
        return porcentajes.getOrDefault(miembro, 0.0);
    }
}