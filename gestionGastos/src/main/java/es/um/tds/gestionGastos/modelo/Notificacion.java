package es.um.tds.gestionGastos.modelo;

import java.time.LocalDateTime;

/**
 * Clase que representa una notificación generada cuando se supera el límite de una alerta.
 * Las notificaciones se almacenan en un historial para que puedan ser consultadas posteriormente.
 */
public class Notificacion {
    private String codigo;
    private String mensaje;
    private LocalDateTime fechaHora;
    private Alerta alerta; // Referencia a la alerta que generó esta notificación
    private boolean leida;

    // Constructor
    public Notificacion(String codigo, String mensaje, Alerta alerta) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.alerta = alerta;
        this.fechaHora = LocalDateTime.now();
        this.leida = false; // Por defecto no está leída
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
    public String toString() {
        return getMensajeCompleto() + (leida ? " [LEÍDA]" : " [NO LEÍDA]");
    }
}