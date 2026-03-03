package CuentaCompartida;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CuentaPorcentual extends CuentaCompartida {
	private Map<Usuario, Double> porcentajes;

	public CuentaPorcentual(String nombre, List<Usuario> participantes, Map<Usuario, Double> porcentajes) {
		super(nombre, participantes);
		validarYAsignarPorcentajes(porcentajes);
	}

	private void validarYAsignarPorcentajes(Map<Usuario, Double> porcentajes) {
		double suma = porcentajes.values().stream().mapToDouble(Double::doubleValue).sum();
		if ((suma - 100) != 0)
			throw new IllegalArgumentException("Suma != 100%");
		this.porcentajes = porcentajes;
	}

	@Override
	public Map<Usuario, Double> calcularSaldos() {
		double total = totalDeLaCuenta();
		// devuelve un mapa con lo que le corresponde a cada usuario según el porcentaje,
		// ejemplo: si usuario u paga 100 y debe pagar 40, el resultado en el mapa será +60 (le deben dinero)
		// ejemplo: si usuario v paga 0 y debe pagar 40, el resultado en el mapa será -40 (debe dinero)
		return participantes.stream().collect(Collectors.toMap(u -> u, u -> totalPagadoPor(u) - (total * (porcentajes.get(u) / 100.0))));
	}
}