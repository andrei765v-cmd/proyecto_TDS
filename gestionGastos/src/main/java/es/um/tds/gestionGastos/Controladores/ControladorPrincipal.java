package es.um.tds.gestionGastos.Controladores;

import java.time.LocalDate;
import java.util.*;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;
import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.*;

public class ControladorPrincipal {

    private static ControladorPrincipal instancia;

    // El constructor es PRIVADO para el Singleton
    private ControladorPrincipal() {
        this.gastosPersonales = new ArrayList<>();
        this.cuentas = new HashMap<>();
        this.categorias = new HashSet<>();
        inicializarCategoriasPredefinidas();
    }

    public static synchronized ControladorPrincipal getInstancia() {
        if (instancia == null) {
            instancia = new ControladorPrincipal();
        }
        return instancia;
    }

    private List<Gasto> gastosPersonales;
    private Map<String, CuentaCompartida> cuentas;
    private Set<Categoria> categorias;

    private void inicializarCategoriasPredefinidas() {
        categorias.add(new Categoria("Alimentación"));
        categorias.add(new Categoria("Transporte"));
        categorias.add(new Categoria("Entretenimiento"));
    }

    // Métodos de Lógica de Negocio
    public void registrarGastoPersonal(double cantidad, LocalDate fecha, String descripcion, Categoria categoria) {
        Gasto nuevo = new Gasto(cantidad, fecha, descripcion, categoria);
        this.gastosPersonales.add(nuevo);
    }

    public List<Gasto> getGastosPersonales() {
        return Collections.unmodifiableList(gastosPersonales);
    }

    public void crearCuentaEquitativa(String nombre, List<Usuario> participantes) {
        cuentas.put(nombre, new CuentaEquitativa(nombre, participantes));
    }

    public void crearCuentaPorcentual(String nombre, List<Usuario> participantes, Map<Usuario, Double> porcentajes) {
        cuentas.put(nombre, new CuentaPorcentual(nombre, participantes, porcentajes));
    }

    public Map<String, CuentaCompartida> getCuentas() {
        return Collections.unmodifiableMap(cuentas);
    }
}