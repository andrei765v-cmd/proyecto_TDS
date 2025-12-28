package es.um.tds.gestionGastos.modelo;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase que representa una notificación generada cuando se supera el límite de una alerta.
 * Las notificaciones se almacenan en un historial para que puedan ser consultadas posteriormente.
 * El código se genera automáticamente con formato N + 3 dígitos (N001, N002, etc.)
 */
public class Notificacion {
    private static final AtomicInteger contadorNotificaciones = new AtomicInteger(0);
    
    private String codigo;
    private String mensaje;
    private LocalDateTime fechaHora;
    private Alerta alerta; // Referencia a la alerta que generó esta notificación
    private boolean leida;

    /**
     * Constructor con código autogenerado
     */
    public Notificacion(String mensaje, Alerta alerta) {
        this.codigo = generarCodigo();
        this.mensaje = mensaje;
        this.alerta = alerta;
        this.fechaHora = LocalDateTime.now();
        this.leida = false; // Por defecto no está leída
    }

    /**
     * Constructor con código específico
     */
    public Notificacion(String codigo, String mensaje, Alerta alerta, LocalDateTime fechaHora, boolean leida) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.alerta = alerta;
        this.fechaHora = fechaHora;
        this.leida = leida;
        
        actualizarContador(codigo);
    }

    /**
     * Genera un código único en formato N001, N002, etc.
     */
    private static String generarCodigo() {
        int numero = contadorNotificaciones.incrementAndGet();
        return String.format("N%03d", numero);
    }

    /**
     * Actualiza el contador basándose en un código existente
     */
    private static void actualizarContador(String codigo) {
        if (codigo != null && codigo.startsWith("N")) {
            try {
                int numero = Integer.parseInt(codigo.substring(1));
                contadorNotificaciones.updateAndGet(current -> Math.max(current, numero));
            } catch (NumberFormatException e) {
                // Código inválido, ignorar
            }
        }
    }

    /**
     * Reinicia el contador
     */
    public static void reiniciarContador() {
        contadorNotificaciones.set(0);
    }

    // Getters
    public String getCodigo() {
        return this.codigo;
    }

    public String getMensaje() {
        return this.mensaje;
    }

    public LocalDateTime getFechaHora() {
        return this.fechaHora;
    }

    public Alerta getAlerta() {
        return this.alerta;
    }

    public boolean isLeida() {
        return this.leida;
    }

    // Setter
    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    /**
     * Marca la notificación como leída
     */
    public void marcarComoLeida() {
        this.leida = true;
    }

    /**
     * Genera el mensaje completo de la notificación con información detallada
     */
    public String getMensajeCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(fechaHora).append("] ");
        sb.append(mensaje);
        
        if (alerta.getCategoria() != null) {
            sb.append(" - Categoría: ").append(alerta.getCategoria().getNombre());
        }
        
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Notificacion notif = (Notificacion) obj;
        return codigo.equals(notif.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return getMensajeCompleto() + (leida ? " [LEÍDA]" : " [NO LEÍDA]");
    }
}