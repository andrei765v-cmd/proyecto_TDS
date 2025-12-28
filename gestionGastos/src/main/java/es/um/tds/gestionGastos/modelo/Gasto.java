package es.um.tds.gestionGastos.modelo;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase que representa un gasto individual.
 * Un gasto puede ser personal o estar asociado a una cuenta compartida.
 * El código se genera automáticamente con formato G + 3 dígitos (G001, G002, etc.)
 */
public class Gasto {
    private static final AtomicInteger contadorGastos = new AtomicInteger(0);
    
    private String codigo;
    private double importe;
    private LocalDate fecha;
    private String descripcion;
    private Categoria categoria;
    private Usuario propietario; // Usuario que registró el gasto
    private CuentaCompartida cuentaCompartida; // null si es gasto personal

    /**
     * Constructor para gasto personal
     */
    public Gasto(double importe, LocalDate fecha, String descripcion, 
                 Categoria categoria, Usuario propietario) {
        this.codigo = generarCodigo();
        this.importe = importe;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.propietario = propietario;
        this.cuentaCompartida = null;
    }

    /**
     * Constructor para gasto de cuenta compartida
     */
    public Gasto(double importe, LocalDate fecha, String descripcion,
                 Categoria categoria, Usuario propietario, CuentaCompartida cuentaCompartida) {
        this(importe, fecha, descripcion, categoria, propietario);
        this.cuentaCompartida = cuentaCompartida;
    }

    /**
     * Constructor con código específico
     */
    public Gasto(String codigo, double importe, LocalDate fecha, String descripcion, 
                 Categoria categoria, Usuario propietario) {
        this.codigo = codigo;
        this.importe = importe;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.propietario = propietario;
        this.cuentaCompartida = null;
        
        actualizarContador(codigo);
    }

    /**
     * Constructor con código específico para gasto compartido
     */
    public Gasto(String codigo, double importe, LocalDate fecha, String descripcion,
                 Categoria categoria, Usuario propietario, CuentaCompartida cuentaCompartida) {
        this(codigo, importe, fecha, descripcion, categoria, propietario);
        this.cuentaCompartida = cuentaCompartida;
    }

    /**
     * Genera un código único en formato G001, G002, etc.
     */
    private static String generarCodigo() {
        int numero = contadorGastos.incrementAndGet();
        return String.format("G%03d", numero);
    }

    /**
     * Actualiza el contador basándose en un código existente
     */
    private static void actualizarContador(String codigo) {
        if (codigo != null && codigo.startsWith("G")) {
            try {
                int numero = Integer.parseInt(codigo.substring(1));
                contadorGastos.updateAndGet(current -> Math.max(current, numero));
            } catch (NumberFormatException e) {
                // Código inválido, ignorar
            }
        }
    }

    /**
     * Reinicia el contador (útil para tests)
     */
    public static void reiniciarContador() {
        contadorGastos.set(0);
    }

    // Getters
    public String getCodigo() {
        return this.codigo;
    }

    public double getImporte() {
        return this.importe;
    }

    public LocalDate getFecha() {
        return this.fecha;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public Usuario getPropietario() {
        return this.propietario;
    }

    public CuentaCompartida getCuentaCompartida() {
        return this.cuentaCompartida;
    }

    // Setters
    public void setImporte(double importe) {
        this.importe = importe;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    // Métodos de negocio
    public boolean esGastoCompartido() {
        return this.cuentaCompartida != null;
    }

    public boolean esGastoPersonal() {
        return this.cuentaCompartida == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Gasto gasto = (Gasto) obj;
        return codigo.equals(gasto.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        String tipo = esGastoCompartido() ? "[COMPARTIDO]" : "[PERSONAL]";
        return String.format("%s %s %.2f€ - %s - %s (%s)", 
            codigo, tipo, importe, descripcion, categoria.getNombre(), fecha);
    }
}