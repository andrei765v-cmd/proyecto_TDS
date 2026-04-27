package es.um.tds.gestionGastos.modelo.CuentaCompartida;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;
import javafx.scene.Node;

public abstract class CuentaCompartida {
	private String nombre;
	protected List<Usuario> participantes;
	protected List<Gasto> gastos;
	
	public CuentaCompartida(String nombre, List<Usuario> participantes) {
		this.nombre = nombre;
		this.participantes = participantes;
		this.gastos = new ArrayList<>();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Gasto> getGastos() {
	    return Collections.unmodifiableList(gastos);
	}
	
	public List<Usuario> getUsuarios() {
		return Collections.unmodifiableList(participantes);
	}
	
	public Gasto registrarGasto(double cantidad, LocalDate fecha, String descripcion, Categoria categoria, Usuario pagador) {
	    Gasto gasto = new Gasto(cantidad, fecha, descripcion, categoria, pagador);
	    this.gastos.add(gasto);
	    return gasto;
	}

	public void addGasto(Gasto gasto) {
		if (!this.gastos.contains(gasto)) {
			this.gastos.add(gasto);
		}
	}

	public void eliminarGasto(Gasto gasto) {
		this.gastos.remove(gasto);
	}

	public abstract Map<Usuario, Double> calcularSaldos();

	public double totalPagadoPor(Usuario u) {	// Método auxiliar para sumar cuánto ha pagado un usuario
		return gastos.stream().filter(g -> g.getUsuario().equals(u))
				.mapToDouble(Gasto::getCantidad).sum();
	}

	protected double totalDeLaCuenta() {
		return gastos.stream().mapToDouble(Gasto::getCantidad).sum();
	}

}