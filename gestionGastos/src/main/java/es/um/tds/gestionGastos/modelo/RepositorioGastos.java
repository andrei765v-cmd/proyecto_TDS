package es.um.tds.gestionGastos.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;

import es.um.tds.gestionGastos.modelo.persistencia.IRepositorioGastos;
import es.um.tds.gestionGastos.modelo.persistencia.JsonStore;

public class RepositorioGastos implements IRepositorioGastos {
    private static final String FICHERO = "gastos.json";
    private static RepositorioGastos unicaInstancia;
    private final List<Gasto> todosLosGastos = new ArrayList<>();

    private RepositorioGastos() {}

    public static RepositorioGastos getInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new RepositorioGastos();
        }
        return unicaInstancia;
    }

    @Override
    public Gasto registrarGastoPersonal(double cantidad, LocalDate fecha, String descripcion, Categoria categoria, Usuario usuario) {
        Gasto nuevo = new Gasto(cantidad, fecha, descripcion, categoria, usuario);
        todosLosGastos.add(nuevo);
        return nuevo;
    }

    @Override
    public void addGasto(Gasto gasto) {
        todosLosGastos.add(gasto);
    }

    @Override
    public void removeGasto(Gasto gasto) {
        todosLosGastos.remove(gasto);
    }

    @Override
    public List<Gasto> getGastos() {
        return Collections.unmodifiableList(todosLosGastos);
    }

    @Override
    public List<Gasto> getGastosPersonales(Usuario usuario) {
        return todosLosGastos.stream()
                .filter(g -> g.getUsuario().equals(usuario))
                .collect(Collectors.toList());
    }

    @Override
    public List<Gasto> filtrarGastos(List<Integer> meses, LocalDate inicio, LocalDate fin, List<Categoria> categorias) {
        return todosLosGastos.stream().filter(g -> {
            boolean mesOk = (meses == null || meses.isEmpty() || meses.contains(g.getFecha().getMonthValue()));
            boolean fechaOk = (inicio == null || !g.getFecha().isBefore(inicio)) && (fin == null || !g.getFecha().isAfter(fin));
            boolean catOk = (categorias == null || categorias.isEmpty() || categorias.contains(g.getCategoria()));
            return mesOk && fechaOk && catOk;
        }).collect(Collectors.toList());
    }

    @Override
    public void cargar() {
        List<Gasto> cargados = JsonStore.leer(FICHERO, new TypeReference<List<Gasto>>() {}, List.of());
        todosLosGastos.clear();
        todosLosGastos.addAll(cargados);
    }

    @Override
    public void guardar() {
        JsonStore.escribir(FICHERO, todosLosGastos);
    }
}
