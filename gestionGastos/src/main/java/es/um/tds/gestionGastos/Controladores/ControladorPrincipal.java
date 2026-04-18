package es.um.tds.gestionGastos.Controladores;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaCompartida;

// Fachada Singleton (GRASP Controlador). Delega a los controladores de entidad.
public class ControladorPrincipal {

    private static ControladorPrincipal instancia;

    private final ControladorGasto controladorGasto;
    private final ControladorCuenta controladorCuenta;
    private final ControladorCategoria controladorCategoria;
    private final ControladorUsuario controladorUsuario;
    private final ControladorAlerta controladorAlerta;

    private ControladorPrincipal() {
        this.controladorGasto = new ControladorGasto();
        this.controladorCuenta = new ControladorCuenta();
        this.controladorCategoria = new ControladorCategoria();
        this.controladorUsuario = new ControladorUsuario();
        this.controladorAlerta = new ControladorAlerta();
    }

    public static synchronized ControladorPrincipal getInstancia() {
        if (instancia == null) {
            instancia = new ControladorPrincipal();
        }
        return instancia;
    }

    // Acceso directo a los controladores de entidad cuando la vista necesite detalle
    public ControladorGasto getControladorGasto() { return controladorGasto; }
    public ControladorCuenta getControladorCuenta() { return controladorCuenta; }
    public ControladorCategoria getControladorCategoria() { return controladorCategoria; }
    public ControladorUsuario getControladorUsuario() { return controladorUsuario; }
    public ControladorAlerta getControladorAlerta() { return controladorAlerta; }

    // --- Operaciones fachada (delegan) ---

    public void registrarGastoPersonal(double cantidad, LocalDate fecha, String descripcion, Categoria categoria) {
        controladorGasto.registrar(cantidad, fecha, descripcion, categoria);
    }

    public List<Gasto> getGastosPersonales() {
        return controladorGasto.getGastos();
    }

    public void crearCuentaEquitativa(String nombre, List<Usuario> participantes) {
        controladorCuenta.crearEquitativa(nombre, participantes);
    }

    public void crearCuentaPorcentual(String nombre, List<Usuario> participantes, Map<Usuario, Double> porcentajes) {
        controladorCuenta.crearPorcentual(nombre, participantes, porcentajes);
    }

    public Map<String, CuentaCompartida> getCuentas() {
        return controladorCuenta.getCuentas();
    }

    public Set<Categoria> getCategorias() {
        return controladorCategoria.getCategorias();
    }
}
