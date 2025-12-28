package es.um.tds.gestionGastos.modelo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase que representa una categoría de gasto.
 * Las categorías permiten organizar y clasificar los gastos del usuario.
 * El código se genera automáticamente con formato C + 3 dígitos (C001, C002, etc.)
 */
public class Categoria {
    private static final AtomicInteger contadorCategorias = new AtomicInteger(0);
    
    private String codigo;
    private String nombre;
    private String descripcion;

    /**
     * Constructor que genera automáticamente el código de categoría
     */
    public Categoria(String nombre, String descripcion) {
        this.codigo = generarCodigo();
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /**
     * Constructor con código específico
     */
    public Categoria(String codigo, String nombre, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        
        // Actualizar el contador si el código es mayor
        actualizarContador(codigo);
    }

    /**
     * Genera un código único en formato C001, C002, etc.
     */
    private static String generarCodigo() {
        int numero = contadorCategorias.incrementAndGet();
        return String.format("C%03d", numero);
    }

    /**
     * Actualiza el contador basándose en un código existente
     * Esto es útil cuando cargamos datos desde persistencia
     */
    private static void actualizarContador(String codigo) {
        if (codigo != null && codigo.startsWith("C")) {
            try {
                int numero = Integer.parseInt(codigo.substring(1));
                contadorCategorias.updateAndGet(current -> Math.max(current, numero));
            } catch (NumberFormatException e) {
                // Código inválido, ignorar
            }
        }
    }

    /**
     * Reinicia el contador
     */
    public static void reiniciarContador() {
        contadorCategorias.set(0);
    }

    // Getters
    public String getCodigo() {
        return this.codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Categoria categoria = (Categoria) obj;
        return codigo.equals(categoria.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s [%s] - %s", codigo, nombre, descripcion);
    }
}