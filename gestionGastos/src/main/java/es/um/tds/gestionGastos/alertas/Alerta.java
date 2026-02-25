package es.um.tds.gestionGastos.alertas;

import java.util.List;

import es.um.tds.gestionGastos.Gasto;

public abstract class Alerta {
	private double limite;
	private Categoria categoria;

	public Alerta(double limite, Categoria categoria) {
		this.limite = limite;
		this.categoria = categoria;
	}

	public double getLimite() {
		return limite;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	// patron strategy, las subclases deciden la implementacion
	public abstract double calcularGastoActual(List<Gasto> todosLosGastos);

	public boolean esSuperada(List<Gasto> gastos) {
		return calcularGastoActual(gastos) > limite;
	}
}