package es.um.tds.gestionGastos.modelo.CuentaCompartida;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import es.um.tds.gestionGastos.modelo.Usuario;

public class CuentaEquitativa extends CuentaCompartida {

    public CuentaEquitativa() {
        super();
    }

    @JsonCreator
    public CuentaEquitativa(
            @JsonProperty("nombre") String nombre,
            @JsonProperty("participantes") List<Usuario> participantes) {
        super(nombre, participantes != null ? participantes : new ArrayList<>());
    }

    @Override
    public Map<Usuario, Double> calcularSaldos() {
        double cuota = participantes.isEmpty() ? 0 : totalDeLaCuenta() / participantes.size();
        return participantes.stream().collect(Collectors.toMap(u -> u, u -> totalPagadoPor(u) - cuota));
    }
}
