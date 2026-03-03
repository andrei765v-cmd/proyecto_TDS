package es.um.tds.gestionGastos.Controladores;

import es.um.tds.gestionGastos.modelo.CuentaCompartida.*;
import es.um.tds.gestionGastos.modelo.Usuario;
import es.um.tds.gestionGastos.modelo.Alertas.Categoria;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ControladorCuentaCompartida {
	private Map<String, CuentaCompartida> cuentas;
	private Set<Categoria> categorias;

	public ControladorCuentaCompartida() {
		this.cuentas = new HashMap<>();
		this.categorias = new HashSet<>();
		inicializarCategoriasPredefinidas();
	}

	private void inicializarCategoriasPredefinidas() {
		categorias.add(new Categoria("Alimentación"));
		categorias.add(new Categoria("Transporte"));
		categorias.add(new Categoria("Entretenimiento"));
	}

	public Set<Categoria> getCategoriasDisponibles() {
		return Collections.unmodifiableSet(categorias);
	}

	public void crearCategoria(String nombre) throws Exception {
		Categoria nuevaCategoria = new Categoria(nombre);
		if (!categorias.add(nuevaCategoria)) {
			throw new Exception("Error: La categoría '" + nombre + "' ya existe.");
		}
	}

	public void crearCuentaEquitativa(String nombre, List<Usuario> participantes) {
		CuentaCompartida nuevaCuenta = new CuentaEquitativa(nombre, participantes);
		cuentas.put(nombre, nuevaCuenta);
	}

	public void crearCuentaPorcentual(String nombre, List<Usuario> participantes, Map<Usuario, Double> porcentajes) {
		// La validación del 100% ocurre en el constructor de CuentaPorcentual
		CuentaCompartida nuevaCuenta = new CuentaPorcentual(nombre, participantes, porcentajes);
		cuentas.put(nombre, nuevaCuenta);
	}

	public void registrarGastoEnCuenta(String nombreCuenta, double cantidad, String descripcion, Categoria categoria, Usuario pagador) {
		CuentaCompartida cuenta = cuentas.get(nombreCuenta);
		if (cuenta != null) {
			// Usamos LocalDate.now() para la fecha actual
			cuenta.registrarGasto(cantidad, LocalDate.now(), descripcion, categoria, pagador);
		}
	}

	public Map<Usuario, Double> obtenerSaldosDeCuenta(String nombreCuenta) {
		CuentaCompartida cuenta = cuentas.get(nombreCuenta);
		if (cuenta == null) {
			return new HashMap<>();
		}
		return cuenta.calcularSaldos();
	}
}