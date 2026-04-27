package es.um.tds.gestionGastos.modelo.Alertas;

import java.time.LocalDate;

public class Notificacion {
	private String titulo;
	private String mensaje;
	private LocalDate fecha;
	private es.um.tds.gestionGastos.modelo.Usuario usuario;

	public Notificacion(String mensaje, String titulo, es.um.tds.gestionGastos.modelo.Usuario usuario) {
		this.titulo = titulo;
		this.mensaje = mensaje;
		this.usuario = usuario;
		this.fecha = LocalDate.now(); // fecha actual
	}

	public es.um.tds.gestionGastos.modelo.Usuario getUsuario() {
		return usuario;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	@Override
	public String toString() {
		return "[" + fecha + "] " + mensaje;
	}
}