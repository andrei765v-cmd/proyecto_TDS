package es.um.tds.gestionGastos.alertas;

import java.time.LocalDate;

public class Notificacion {
	private String mensaje;
	private LocalDate fecha;

	public Notificacion(String mensaje) {
		this.mensaje = mensaje;
		this.fecha = LocalDate.now(); // fecha actual
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