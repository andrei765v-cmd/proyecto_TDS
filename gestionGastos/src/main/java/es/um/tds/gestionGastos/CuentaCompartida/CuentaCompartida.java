package CuentaCompartida;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CuentaCompartida {
	private String nombre;
	protected List<Usuario> participantes;
	protected Map<Gasto, Usuario> registroPagos; // Mapa para saber quién pagó cada gasto registrado en una cuenta

	public CuentaCompartida(String nombre, List<Usuario> participantes) {
		this.nombre = nombre;
		this.participantes = participantes;
		this.registroPagos = new HashMap<>();
	}

	public String getNombre() {
		return nombre;
	}

	public void registrarGasto(double cantidad, LocalDate fecha, String descripcion, Categoria categoria, Usuario pagador) {
		Gasto nuevoGasto = new Gasto(cantidad, fecha, descripcion, categoria);
		this.registroPagos.put(nuevoGasto, pagador);
	}

	public abstract Map<Usuario, Double> calcularSaldos();

	protected double totalPagadoPor(Usuario u) {	// Método auxiliar para sumar cuánto ha pagado un usuario
		return registroPagos.entrySet().stream().filter(n -> n.getValue().equals(u))
				.mapToDouble(n -> n.getKey().getCantidad()).sum();
	}

	protected double totalDeLaCuenta() {
		return registroPagos.keySet().stream().mapToDouble(Gasto::getCantidad).sum();
	}
}