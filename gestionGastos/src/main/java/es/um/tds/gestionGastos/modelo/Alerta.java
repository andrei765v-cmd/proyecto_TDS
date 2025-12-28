package es.um.tds.gestionGastos.modelo;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase abstracta que representa una alerta de gasto.
 * Implementa el patrón Strategy para diferentes tipos de alertas (semanal, mensual).
 * El código se genera automáticamente con formato A + 3 dígitos (A001, A002, etc.)
 */
public abstract class Alerta {
    private static final AtomicInteger contadorAlertas = new AtomicInteger(0);
    
    private String codigo;
    private double limiteGasto;
    private Categoria categoria; // null si es para todas las categorías
    private boolean activa;

    /**
     * Constructor con código autogenerado
     */
    public Alerta(double limiteGasto, Categoria categoria) {
        this.codigo = generarCodigo();
        this.limiteGasto = limiteGasto;
        this.categoria = categoria;
        this.activa = true; // Por defecto está activa
    }

    /**
     * Constructor con código específico 
     */
    public Alerta(String codigo, double limiteGasto, Categoria categoria) {
        this.codigo = codigo;
        this.limiteGasto = limiteGasto;
        this.categoria = categoria;
        this.activa = true;
        
        actualizarContador(codigo);
    }

    /**
     * Genera un código único en formato A001, A002, etc.
     */
    private static String generarCodigo() {
        int numero = contadorAlertas.incrementAndGet();
        return String.format("A%03d", numero);
    }

    /**
     * Actualiza el contador basándose en un código existente
     */
    private static void actualizarContador(String codigo) {
        if (codigo != null && codigo.startsWith("A")) {
            try {
                int numero = Integer.parseInt(codigo.substring(1));
                contadorAlertas.updateAndGet(current -> Math.max(current, numero));
            } catch (NumberFormatException e) {
                // Código inválido, ignorar
            }
        }
    }

    /**
     * Reinicia el contador
     */
    public static void reiniciarContador() {
        contadorAlertas.set(0);
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
     * 
     * @param gastos Lista de gastos del usuario
     * @param fechaActual Fecha actual para el cálculo
     * @return true si se ha superado el límite, false en caso contrario
     */
    public abstract boolean verificarAlerta(List<Gasto> gastos, LocalDate fechaActual);

    /**
     * Calcula el total de gastos filtrados por categoría 
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Alerta alerta = (Alerta) obj;
        return codigo.equals(alerta.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        String categoriaStr;
        if (categoria != null) {
            categoriaStr = categoria.getNombre();
        } else {
            categoriaStr = "Todas";
        }
        return String.format("Alerta[%s - %s - %.2f€ - %s]", 
            codigo, getTipoAlerta(), limiteGasto, categoriaStr);
    }
}