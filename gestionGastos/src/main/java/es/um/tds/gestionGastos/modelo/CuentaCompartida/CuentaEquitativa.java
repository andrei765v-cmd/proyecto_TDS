package es.um.tds.gestionGastos.modelo.CuentaCompartida;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.um.tds.gestionGastos.modelo.Usuario;

public class CuentaEquitativa extends CuentaCompartida {
	
	public CuentaEquitativa(String nombre, List<Usuario> participantes) {
		super(nombre, participantes);
	}

	@Override
	public Map<Usuario, Double> calcularSaldos() {
		double cuota = participantes.isEmpty() ? 0 : totalDeLaCuenta() / participantes.size();	// suma el total de gastos de la cuenta y los divide entre los participantes que la compartan.
		// devuelve la diferencia entre lo que ha pagado el individuo y lo que deberia haber pagado,
		// ejemplo: si hay 3 usuarios y el usuario u ha pagado 30 y los otros 0 el resultado sera de +20 para u y -10 para los otros dos.
		return participantes.stream().collect(Collectors.toMap(u -> u, u -> totalPagadoPor(u) - cuota));
	}
}