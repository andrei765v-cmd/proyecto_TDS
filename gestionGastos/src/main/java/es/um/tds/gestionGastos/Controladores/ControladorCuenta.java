package es.um.tds.gestionGastos.Controladores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.um.tds.gestionGastos.modelo.Usuario;
import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaCompartida;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaEquitativa;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaPorcentual;

public class ControladorCuenta {
    private final Map<String, CuentaCompartida> cuentas = new HashMap<>();

    public ControladorCuenta() {
    	List<Usuario> usuarios = new ArrayList<>();
    	usuarios.add(new Usuario("prueba"));
    	usuarios.add(new Usuario("prueba1"));
    	usuarios.add(new Usuario("prueba2"));
    	usuarios.add(new Usuario("prueba3"));
    	cuentas.put("pollo", new CuentaEquitativa("cuenta-prueba", usuarios));
    }
    
    public void crearEquitativa(String nombre, List<Usuario> participantes) throws Exception {
        if (cuentas.containsKey(nombre)) {
            throw new Exception("Ya existe una cuenta con el nombre: " + nombre);
        }
        cuentas.put(nombre, new CuentaEquitativa(nombre, participantes));
    }

    public void crearPorcentual(String nombre, List<Usuario> participantes, Map<Usuario, Double> porcentajes) throws Exception{
    	if (cuentas.containsKey(nombre)) {
            throw new Exception("Ya existe una cuenta con el nombre: " + nombre);
        }
    	cuentas.put(nombre, new CuentaPorcentual(nombre, participantes, porcentajes));
    }

    public Map<String, CuentaCompartida> getCuentas() {
        return Collections.unmodifiableMap(cuentas);
    }
    
    // registar los gastos de una cuenta
    public Gasto registrarGastoEnCuenta(String nombreCuenta, double cantidad, LocalDate fecha, String descripcion, Categoria categoria, Usuario usuario) {
        CuentaCompartida cuenta = cuentas.get(nombreCuenta);
        if (cuenta != null) {
            return cuenta.registrarGasto(cantidad, fecha, descripcion, categoria, usuario);
        }
        return null;
    }
    
    // obtener los saldos de una cuenta
    public Map<Usuario, Double> obtenerSaldosDeCuenta(String nombreCuenta) {
    	CuentaCompartida cuenta = cuentas.get(nombreCuenta);
    	return (cuenta == null) ? new HashMap<>() : cuenta.calcularSaldos();
    }
    
    // obtener los gastos de un usuario
    public List<Gasto> getGastosPersonales(Usuario usuario) {
    	// Buscamos en todas las cuentas donde el usuario participa
        return cuentas.values().stream()
        		.flatMap(cuenta -> cuenta.getGastos().stream()) // lista de gastos
                .filter(gasto -> gasto.getUsuario().equals(usuario)) // usuario de ese gasto
                .collect(Collectors.toList());
    }
}


