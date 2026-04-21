package es.um.tds.gestionGastos.Controladores;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaCompartida;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Fachada Singleton (GRASP Controlador). Delega a los controladores de entidad.
public class ControladorPrincipal {

    private static ControladorPrincipal instancia;

    private final ControladorGasto controladorGasto;
    private final ControladorCuenta controladorCuenta;
    private final ControladorCategoria controladorCategoria;
    private final ControladorUsuario controladorUsuario;
    private final ControladorAlerta controladorAlerta;
    
    private ObservableList<Gasto> todosLosGastos = FXCollections.observableArrayList();
    
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

    // --- Operaciones fachada ---
    
    
    
    public ObservableList<Gasto> getGastosObservable() {
        return todosLosGastos;
    }
    
    
    
    
    // Usuarios
    public Set<Usuario> getUsuarios() {
        return controladorUsuario.getUsuarios();
    }
    // Gastos
    // registra el gasto en la cuenta
    public void registrarGastoEnCuenta(double cantidad, LocalDate fecha, String descripcion, Categoria categoria, Usuario usuario, String nombreCuenta) {
        Gasto nuevoGasto = controladorCuenta.registrarGastoEnCuenta(nombreCuenta, cantidad, fecha, descripcion, categoria, usuario);
        if (nuevoGasto != null) {
            todosLosGastos.add(nuevoGasto);
        }
    }
    
    public void registrarGastoPersonal(double cantidad, LocalDate fecha, String descripcion, Categoria categoria, Usuario usuario) {
        Gasto nuevo = new Gasto(cantidad, fecha, descripcion, categoria, usuario);
        // IMPORTANTE: Añadir a la lista observable que la tabla está mirando
        todosLosGastos.add(nuevo);
    }
    
    /*
    public void registrarGastoPersonal(double cantidad, LocalDate fecha, String descripcion, Categoria categoria, Usuario usuario) {
        controladorGasto.registrarGastoPersonal(cantidad, fecha, descripcion, categoria, usuario);
    }
    */
    public ObservableList<Gasto> getGastosPersonales(Usuario usuario) {
        // En un caso real, aquí filtrarías o devolverías la lista que maneja ControladorCuenta
        // Para este ejemplo, usaremos la lista compartida
        return controladorGasto.getGastosPersonales(usuario);
    }

    public ObservableList<Gasto> getGastos() {
        return controladorGasto.getGastos();
    }
    
    // Cuentas
    public void crearCuentaEquitativa(String nombre, List<Usuario> participantes) throws Exception {
        controladorCuenta.crearEquitativa(nombre, participantes);
    }

    public void crearCuentaPorcentual(String nombre, List<Usuario> participantes, Map<Usuario, Double> porcentajes) throws Exception {
        controladorCuenta.crearPorcentual(nombre, participantes, porcentajes);
    }

    public Map<String, CuentaCompartida> getCuentas() {
        return controladorCuenta.getCuentas();
    }
    
    public Map<Usuario, Double> obtenerSaldosDeCuenta(String nombreCuenta) {
    	return controladorCuenta.obtenerSaldosDeCuenta(nombreCuenta);
    }
    
    // Categorias
    public Set<Categoria> getCategorias() {
        return controladorCategoria.getCategorias();
    }
    
    // Alertas
    public void registrarAlertaMensual(double limite, Categoria cat) {
        controladorAlerta.crearMensual(limite, cat);
    }

    public void registrarAlertaSemanal(double limite, Categoria cat) {
        controladorAlerta.crearSemanal(limite, cat);
    }

}
