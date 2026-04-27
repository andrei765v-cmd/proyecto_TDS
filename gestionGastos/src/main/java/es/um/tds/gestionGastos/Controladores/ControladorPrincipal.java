package es.um.tds.gestionGastos.Controladores;

import java.time.LocalDate;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import es.um.tds.gestionGastos.modelo.Importador.Importador;
import es.um.tds.gestionGastos.modelo.Importador.ImportadorFactory;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaCompartida;
import es.um.tds.gestionGastos.modelo.RepositorioAlertas;
import es.um.tds.gestionGastos.modelo.RepositorioCategorias;
import es.um.tds.gestionGastos.modelo.RepositorioCuentas;
import es.um.tds.gestionGastos.modelo.RepositorioGastos;
import es.um.tds.gestionGastos.modelo.RepositorioUsuarios;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Fachada Singleton (GRASP Controlador). Delega en los Repositorios del modelo.
public class ControladorPrincipal {

    private static ControladorPrincipal instancia;
    
    // Lista observable para la interfaz (JavaFX)
    private ObservableList<Gasto> todosLosGastos = FXCollections.observableArrayList();
    
    // Usuario activo de la sesión
    private ObjectProperty<Usuario> usuarioActivo = new SimpleObjectProperty<>();
    
    // Filtro global actual
    private Predicate<Gasto> filtroActual = g -> true;
    
    private ControladorPrincipal() {
        // Cargar gastos existentes si hubiera persistencia
        todosLosGastos.addAll(RepositorioGastos.getInstancia().getGastos());
    }

    public static synchronized ControladorPrincipal getInstancia() {
        if (instancia == null) {
            instancia = new ControladorPrincipal();
        }
        return instancia;
    }

    public ObservableList<Gasto> getGastosObservable() {
        return todosLosGastos;
    }

    public ObjectProperty<Usuario> usuarioActivoProperty() {
        return usuarioActivo;
    }
    
    public Usuario getUsuarioActivo() {
        return usuarioActivo.get();
    }
    
    public void setUsuarioActivo(Usuario u) {
        this.usuarioActivo.set(u);
    }
    
    // --- Gestión de Filtros ---
    public void aplicarFiltros(Predicate<Gasto> filtro) {
        this.filtroActual = filtro;
        Usuario activo = getUsuarioActivo();
        if (activo != null) {
            List<Gasto> todos = getGastosPersonales(activo);
            List<Gasto> filtrados = todos.stream().filter(filtro).collect(Collectors.toList());
            todosLosGastos.setAll(filtrados);
        } else {
            todosLosGastos.clear();
        }
    }
    
    public Predicate<Gasto> getFiltroActual() {
        return filtroActual;
    }
    
    public List<Gasto> getGastosFiltradosDeCuenta(String nombreCuenta) {
        CuentaCompartida cuenta = RepositorioCuentas.getInstancia().getCuenta(nombreCuenta);
        if (cuenta == null) return new ArrayList<>();
        return cuenta.getGastos().stream().filter(filtroActual).collect(Collectors.toList());
    }

    // --- Operaciones delegadas ---
    
    // Usuarios
    public ObservableList<Usuario> getUsuarios() {
        return RepositorioUsuarios.getInstancia().getUsuarios();
    }
    
    public Usuario registrarUsuario(String nombre) {
        return RepositorioUsuarios.getInstancia().registrar(nombre);
    }

    // Gastos
    public void registrarGastoEnCuenta(double cantidad, LocalDate fecha, String descripcion, Categoria categoria, Usuario usuario, String nombreCuenta) {
        CuentaCompartida cuenta = RepositorioCuentas.getInstancia().getCuenta(nombreCuenta);
        if (cuenta != null) {
            Gasto nuevoGasto = cuenta.registrarGasto(cantidad, fecha, descripcion, categoria, usuario);
            RepositorioGastos.getInstancia().addGasto(nuevoGasto);
            todosLosGastos.add(nuevoGasto);
            RepositorioAlertas.getInstancia().evaluarAlertas();
        }
    }
    
    public void registrarGastoPersonal(double cantidad, LocalDate fecha, String descripcion, Categoria categoria, Usuario usuario) {
        Gasto nuevo = RepositorioGastos.getInstancia().registrarGastoPersonal(cantidad, fecha, descripcion, categoria, usuario);
        todosLosGastos.add(nuevo);
        RepositorioAlertas.getInstancia().evaluarAlertas();
    }

    /**
     * Importa gastos desde un fichero externo usando el patrón Adaptador + Factory.
     * @param fichero El fichero a importar.
     * @return Número de gastos importados correctamente.
     * @throws Exception Si el formato no está soportado o el fichero tiene errores.
     */
    public int importarGastosDesdeFichero(File fichero) throws Exception {
        String nombre = fichero.getName();
        String extension = nombre.substring(nombre.lastIndexOf('.') + 1);

        Importador importador = ImportadorFactory.crear(extension);
        Usuario activo = getUsuarioActivo();
        if (activo == null) throw new Exception("No hay un usuario activo seleccionado.");

        List<Gasto> importados = importador.importarGastos(fichero, activo);

        // Registrar en la lista observable y evaluar alertas
        aplicarFiltros(filtroActual);
        RepositorioAlertas.getInstancia().evaluarAlertas();

        return importados.size();
    }
    
    public List<Gasto> getGastosPersonales(Usuario usuario) {
        return RepositorioGastos.getInstancia().getGastosPersonales(usuario);
    }

    public List<Gasto> getGastos() {
        return RepositorioGastos.getInstancia().getGastos();
    }
    
    public void modificarGasto(Gasto gasto, double cantidad, LocalDate fecha, String descripcion, Categoria categoria, String cuentaAnterior, String nuevaCuenta) {
        // Validar que el usuario del gasto pertenece a la nueva cuenta antes de hacer nada
        if (nuevaCuenta != null) {
            CuentaCompartida cn = RepositorioCuentas.getInstancia().getCuenta(nuevaCuenta);
            if (cn != null && !cn.getUsuarios().contains(gasto.getUsuario())) {
                throw new IllegalArgumentException(
                    "El usuario '" + gasto.getUsuario().getNombre() + "' no pertenece a la cuenta '" + nuevaCuenta + "'.");
            }
        }

        gasto.editar(cantidad, fecha, descripcion, categoria);

        // Si cambia de cuenta, retirar de la anterior y añadir a la nueva
        boolean cuentaCambia = !java.util.Objects.equals(cuentaAnterior, nuevaCuenta);
        if (cuentaCambia) {
            if (cuentaAnterior != null) {
                CuentaCompartida ca = RepositorioCuentas.getInstancia().getCuenta(cuentaAnterior);
                if (ca != null) ca.eliminarGasto(gasto);
            }
            if (nuevaCuenta != null) {
                CuentaCompartida cn = RepositorioCuentas.getInstancia().getCuenta(nuevaCuenta);
                if (cn != null) cn.addGasto(gasto); // addGasto ya evita duplicados internamente
            }
        }

        // Refrescar listas observables (los cambios in-place en el objeto Gasto no disparan JavaFX)
        int idx = todosLosGastos.indexOf(gasto);
        if (idx >= 0) {
            todosLosGastos.set(idx, gasto); // Fuerza notificación a todas las vistas observadoras
        }
        aplicarFiltros(filtroActual);
        RepositorioAlertas.getInstancia().evaluarAlertas();
    }

    public void eliminarGasto(Gasto gasto, String cuenta) {
        RepositorioGastos.getInstancia().removeGasto(gasto);
        // Eliminar de TODAS las cuentas que lo contengan (por si acaso)
        for (CuentaCompartida c : RepositorioCuentas.getInstancia().getCuentas().values()) {
            c.eliminarGasto(gasto);
        }
        todosLosGastos.remove(gasto);
        aplicarFiltros(filtroActual);
        RepositorioAlertas.getInstancia().evaluarAlertas();
    }

    // Cuentas
    public void crearCuentaEquitativa(String nombre, List<Usuario> participantes) throws Exception {
        RepositorioCuentas.getInstancia().crearEquitativa(nombre, participantes);
    }

    public void crearCuentaPorcentual(String nombre, List<Usuario> participantes, Map<Usuario, Double> porcentajes) throws Exception {
        RepositorioCuentas.getInstancia().crearPorcentual(nombre, participantes, porcentajes);
    }

    public void eliminarCuenta(String nombre) {
        RepositorioCuentas.getInstancia().eliminarCuenta(nombre);
    }

    public void actualizarCuenta(String nombreViejo, String nombreNuevo) throws Exception {
        RepositorioCuentas.getInstancia().actualizarCuenta(nombreViejo, nombreNuevo);
    }

    public Map<String, CuentaCompartida> getCuentas() {
        return RepositorioCuentas.getInstancia().getCuentas();
    }
    
    public Map<Usuario, Double> obtenerSaldosDeCuenta(String nombreCuenta) {
        CuentaCompartida cuenta = RepositorioCuentas.getInstancia().getCuenta(nombreCuenta);
        return cuenta != null ? cuenta.calcularSaldos() : Map.of();
    }
    
    // Categorias
    public ObservableList<Categoria> getCategorias() {
        return RepositorioCategorias.getInstancia().getCategorias();
    }
    
    public Categoria crearCategoria(String nombre) {
        return RepositorioCategorias.getInstancia().crear(nombre);
    }
    
    // Alertas
    public void registrarAlertaMensual(double limite, Categoria cat) {
        Usuario activo = getUsuarioActivo();
        if (activo != null) {
            RepositorioAlertas.getInstancia().crearAlertaMensual(limite, cat, activo);
        }
    }

    public void registrarAlertaSemanal(double limite, Categoria cat) {
        Usuario activo = getUsuarioActivo();
        if (activo != null) {
            RepositorioAlertas.getInstancia().crearAlertaSemanal(limite, cat, activo);
        }
    }
}