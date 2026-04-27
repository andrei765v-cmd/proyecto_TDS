package es.um.tds.gestionGastos.modelo.Alertas;

import java.util.List;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;

public class Alerta {
	private double limite;
	private Categoria categoria;
	private EstrategiaEvaluacion estrategia;
	private Usuario usuario;
	private boolean estadoSuperada = false;

	public Alerta(double limite, Categoria categoria, EstrategiaEvaluacion estrategia, Usuario usuario) {
		this.limite = limite;
		this.categoria = categoria;
		this.estrategia = estrategia;
		this.usuario = usuario;
	}

	public double getLimite() {
		return limite;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public EstrategiaEvaluacion getEstrategia() {
		return estrategia;
	}

	public double calcularGastoActual(List<Gasto> todosLosGastos) {
		return estrategia.calcularGastoActual(todosLosGastos, categoria);
	}

	public boolean esSuperada(List<Gasto> gastos) {
		return calcularGastoActual(gastos) > limite;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public int comprobarYActualizarEstado(List<Gasto> gastos) {
		boolean actualmenteSuperada = esSuperada(gastos);
		if (actualmenteSuperada && !estadoSuperada) {
			estadoSuperada = true;
			return 1; // Recién superada
		} else if (!actualmenteSuperada && estadoSuperada) {
			estadoSuperada = false; // Reset if period rolled over or expenses deleted
			return -1; // Recién revertida
		}
		return 0; // Sin cambios
	}
}