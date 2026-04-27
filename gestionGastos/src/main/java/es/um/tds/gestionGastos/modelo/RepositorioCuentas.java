package es.um.tds.gestionGastos.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaCompartida;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaEquitativa;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaPorcentual;

public class RepositorioCuentas {
    private static RepositorioCuentas unicaInstancia;
    private final Map<String, CuentaCompartida> cuentas = new HashMap<>();

    private RepositorioCuentas() {
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("prueba"));
        usuarios.add(new Usuario("prueba1"));
        usuarios.add(new Usuario("prueba2"));
        usuarios.add(new Usuario("prueba3"));
        cuentas.put("pollo", new CuentaEquitativa("cuenta-prueba", usuarios));
    }

    public static RepositorioCuentas getInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new RepositorioCuentas();
        }
        return unicaInstancia;
    }

    public CuentaEquitativa crearEquitativa(String nombre, List<Usuario> participantes) throws Exception {
        if (cuentas.containsKey(nombre)) {
            throw new Exception("Ya existe una cuenta con el nombre: " + nombre);
        }
        CuentaEquitativa cuenta = new CuentaEquitativa(nombre, participantes);
        cuentas.put(nombre, cuenta);
        return cuenta;
    }

    public CuentaPorcentual crearPorcentual(String nombre, List<Usuario> participantes, Map<Usuario, Double> porcentajes) throws Exception {
        if (cuentas.containsKey(nombre)) {
            throw new Exception("Ya existe una cuenta con el nombre: " + nombre);
        }
        CuentaPorcentual cuenta = new CuentaPorcentual(nombre, participantes, porcentajes);
        cuentas.put(nombre, cuenta);
        return cuenta;
    }

    public CuentaCompartida getCuenta(String nombre) {
        return cuentas.get(nombre);
    }

    public Map<String, CuentaCompartida> getCuentas() {
        return Collections.unmodifiableMap(cuentas);
    }

    public void eliminarCuenta(String nombre) {
        cuentas.remove(nombre);
    }

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
}