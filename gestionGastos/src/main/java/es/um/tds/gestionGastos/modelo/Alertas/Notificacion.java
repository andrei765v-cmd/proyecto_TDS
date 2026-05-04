package es.um.tds.gestionGastos.modelo.Alertas;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import es.um.tds.gestionGastos.modelo.Usuario;

public class Notificacion {
    private String titulo;
    private String mensaje;
    private LocalDate fecha;
    private Usuario usuario;

    public Notificacion() {}

    @JsonCreator
    public Notificacion(
            @JsonProperty("mensaje") String mensaje,
            @JsonProperty("titulo") String titulo,
            @JsonProperty("usuario") Usuario usuario) {
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.usuario = usuario;
        this.fecha = LocalDate.now();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "[" + fecha + "] " + mensaje;
    }
}
