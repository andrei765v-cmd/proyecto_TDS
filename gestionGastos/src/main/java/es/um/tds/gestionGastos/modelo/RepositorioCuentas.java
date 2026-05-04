package es.um.tds.gestionGastos.modelo;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaCompartida;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaEquitativa;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaPorcentual;
import es.um.tds.gestionGastos.modelo.persistencia.IRepositorioCuentas;
import es.um.tds.gestionGastos.modelo.persistencia.JsonStore;

public class RepositorioCuentas implements IRepositorioCuentas {
    private static final String FICHERO = "cuentas.json";
    private static RepositorioCuentas unicaInstancia;
    private final Map<String, CuentaCompartida> cuentas = new HashMap<>();

    private RepositorioCuentas() {}

    public static RepositorioCuentas getInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new RepositorioCuentas();
        }
        return unicaInstancia;
    }

    @Override
    public CuentaEquitativa crearEquitativa(String nombre, List<Usuario> participantes) throws Exception {
        if (cuentas.containsKey(nombre)) {
            throw new Exception("Ya existe una cuenta con el nombre: " + nombre);
        }
        CuentaEquitativa cuenta = new CuentaEquitativa(nombre, participantes);
        cuentas.put(nombre, cuenta);
        return cuenta;
    }

    @Override
    public CuentaPorcentual crearPorcentual(String nombre, List<Usuario> participantes, Map<Usuario, Double> porcentajes) throws Exception {
        if (cuentas.containsKey(nombre)) {
            throw new Exception("Ya existe una cuenta con el nombre: " + nombre);
        }
        CuentaPorcentual cuenta = CuentaPorcentual.crear(nombre, participantes, porcentajes);
        cuentas.put(nombre, cuenta);
        return cuenta;
    }

    @Override
    public CuentaCompartida getCuenta(String nombre) {
        return cuentas.get(nombre);
    }

    @Override
    public Map<String, CuentaCompartida> getCuentas() {
        return Collections.unmodifiableMap(cuentas);
    }

    @Override
    public void eliminarCuenta(String nombre) {
        cuentas.remove(nombre);
    }

    @Override
    public void actualizarCuenta(String nombreViejo, String nombreNuevo) throws Exception {
        if (!nombreViejo.equals(nombreNuevo) && cuentas.containsKey(nombreNuevo)) {
            throw new Exception("Ya existe una cuenta con el nombre: " + nombreNuevo);
        }
        CuentaCompartida cuenta = cuentas.remove(nombreViejo);
        if (cuenta != null) {
            cuenta.setNombre(nombreNuevo);
            cuentas.put(nombreNuevo, cuenta);
        }
    }

    @Override
    public void cargar() {
        Map<String, CuentaCompartida> cargadas = JsonStore.leer(FICHERO,
                new TypeReference<Map<String, CuentaCompartida>>() {}, Map.of());
        cuentas.clear();
        cuentas.putAll(cargadas);
    }

    @Override
    public void guardar() {
        JsonStore.escribir(FICHERO, cuentas);
    }
}
