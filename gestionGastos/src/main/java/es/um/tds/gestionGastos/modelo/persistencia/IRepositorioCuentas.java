package es.um.tds.gestionGastos.modelo.persistencia;

import java.util.List;
import java.util.Map;

import es.um.tds.gestionGastos.modelo.Usuario;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaCompartida;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaEquitativa;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaPorcentual;

public interface IRepositorioCuentas {
    CuentaEquitativa crearEquitativa(String nombre, List<Usuario> participantes) throws Exception;
    CuentaPorcentual crearPorcentual(String nombre, List<Usuario> participantes, Map<Usuario, Double> porcentajes) throws Exception;
    CuentaCompartida getCuenta(String nombre);
    Map<String, CuentaCompartida> getCuentas();
    void eliminarCuenta(String nombre);
    void actualizarCuenta(String nombreViejo, String nombreNuevo) throws Exception;
    void cargar();
    void guardar();
}
