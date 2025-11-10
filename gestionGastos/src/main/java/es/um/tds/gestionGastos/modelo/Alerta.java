package es.um.tds.gestionGastos.modelo;

import java.time.LocalDate;
import java.util.List;

/**
 * Clase abstracta que representa una alerta de gasto.
 * Implementa el patrón Strategy para diferentes tipos de alertas (semanal, mensual).
 */
public abstract class Alerta {
    private String codigo;
    private double limiteGasto;
    private Categoria categoria; // null si es para todas las categorías
    private boolean activa;

    // Constructor
    public Alerta(String codigo, double limiteGasto, Categoria categoria) {
        this.codigo = codigo;
        this.limiteGasto = limiteGasto;
        this.categoria = categoria;
        this.activa = true; // Por defecto está activa
    }

    // Getters
    public String getCodigo() {
        return this.codigo;
    }

    public double getLimiteGasto() {
        return this.limiteGasto;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public boolean isActiva() {
        return this.activa;
    }

    // Setters
    public void setLimiteGasto(double limiteGasto) {
        this.limiteGasto = limiteGasto;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    /**
     * Método abstracto que define la estrategia de verificación de la alerta.
     * Cada subclase implementará su propia lógica (semanal, mensual, etc.)
     * 
     * @param gastos Lista de gastos del usuario
     * @param fechaActual Fecha actual para el cálculo
     * @return true si se ha superado el límite, false en caso contrario
     */
    public abstract boolean verificarAlerta(List<Gasto> gastos, LocalDate fechaActual);

    /**
     * Calcula el total de gastos filtrados por categoría (si aplica)
     * 
     * @param gastos Lista de gastos a filtrar
     * @return Total de gastos
     */
    protected double calcularTotalGastos(List<Gasto> gastos) {
        return gastos.stream()
            .filter(g -> this.categoria == null || g.getCategoria().equals(this.categoria))
            .mapToDouble(Gasto::getImporte)
            .sum();
    }

    /**
     * Método auxiliar para obtener una descripción del tipo de alerta
     */
    public abstract String getTipoAlerta();
}