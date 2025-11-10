package es.um.tds.gestionGastos.modelo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase abstracta que representa una cuenta de gasto compartida entre varias personas.
 * Implementa el patrón Strategy para diferentes tipos de distribución (equitativa, porcentaje).
 */
public abstract class CuentaCompartida {
    private String codigo;
    private String nombre;
    private List<String> miembros; // Lista de nombres de las personas
    private Map<String, Double> saldos; // Saldo de cada miembro (+ debe cobrar, - debe pagar)

    // Constructor
    public CuentaCompartida(String codigo, String nombre, List<String> miembros) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.miembros = miembros;
        this.saldos = new HashMap<>();
        
        // Inicializar todos los saldos a 0
        for (String miembro : miembros) {
            saldos.put(miembro, 0.0);
        }
    }

    // Getters
    public String getCodigo() {
        return this.codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public List<String> getMiembros() {
        return this.miembros;
    }

    public Map<String, Double> getSaldos() {
        return this.saldos;
    }

    public double getSaldoMiembro(String miembro) {
        return saldos.getOrDefault(miembro, 0.0);
    }

    // Setter
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método abstracto que define cómo se distribuye un gasto entre los miembros.
     * Cada subclase implementará su propia estrategia (equitativa, por porcentaje).
     * 
     * @param pagador Nombre de la persona que pagó el gasto
     * @param importe Cantidad pagada
     */
    public abstract void registrarGasto(String pagador, double importe);

    /**
     * Actualiza el saldo de un miembro
     */
    protected void actualizarSaldo(String miembro, double cantidad) {
        double saldoActual = saldos.get(miembro);
        saldos.put(miembro, saldoActual + cantidad);
    }

    /**
     * Obtiene un resumen de los saldos de la cuenta
     */
    public String obtenerResumenSaldos() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cuenta: ").append(nombre).append("\n");
        sb.append("Saldos:\n");
        
        for (Map.Entry<String, Double> entry : saldos.entrySet()) {
            String miembro = entry.getKey();
            double saldo = entry.getValue();
            
            if (saldo > 0) {
                sb.append("  ").append(miembro).append(": +").append(String.format("%.2f", saldo))
                  .append("€ (le deben)\n");
            } else if (saldo < 0) {
                sb.append("  ").append(miembro).append(": ").append(String.format("%.2f", saldo))
                  .append("€ (debe)\n");
            } else {
                sb.append("  ").append(miembro).append(": 0.00€ (equilibrado)\n");
            }
        }
        
        return sb.toString();
    }
}