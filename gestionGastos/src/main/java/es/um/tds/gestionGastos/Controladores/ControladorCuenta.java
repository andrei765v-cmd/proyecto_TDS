package es.um.tds.gestionGastos.Controladores;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.um.tds.gestionGastos.modelo.Usuario;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaCompartida;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaEquitativa;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaPorcentual;

public class ControladorCuenta {
    private final Map<String, CuentaCompartida> cuentas = new HashMap<>();

    public void crearEquitativa(String nombre, List<Usuario> participantes) {
        cuentas.put(nombre, new CuentaEquitativa(nombre, participantes));
    }

    public void crearPorcentual(String nombre, List<Usuario> participantes, Map<Usuario, Double> porcentajes) {
        cuentas.put(nombre, new CuentaPorcentual(nombre, participantes, porcentajes));
    }

    public Map<String, CuentaCompartida> getCuentas() {
        return Collections.unmodifiableMap(cuentas);
    }
}
