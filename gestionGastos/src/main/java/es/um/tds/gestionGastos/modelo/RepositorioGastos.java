package es.um.tds.gestionGastos.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioGastos {
    private static RepositorioGastos unicaInstancia;
    private final List<Gasto> todosLosGastos = new ArrayList<>();

    private RepositorioGastos() {}

    public static RepositorioGastos getInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new RepositorioGastos();
        }
        return unicaInstancia;
    }

    public Gasto registrarGastoPersonal(double cantidad, LocalDate fecha, String descripcion, Categoria categoria, Usuario usuario) {
        Gasto nuevo = new Gasto(cantidad, fecha, descripcion, categoria, usuario);
        todosLosGastos.add(nuevo);
        return nuevo;
    }

    // Usado internamente o por las cuentas
    public void addGasto(Gasto gasto) {
        todosLosGastos.add(gasto);
    }

    public void removeGasto(Gasto gasto) {
        todosLosGastos.remove(gasto);
    }

    public List<Gasto> getGastos() {
        return Collections.unmodifiableList(todosLosGastos);
    }

    public List<Gasto> getGastosPersonales(Usuario usuario) {
        return todosLosGastos.stream()
                .filter(g -> g.getUsuario().equals(usuario))
                .collect(Collectors.toList());
    }

    // Métodos de filtrado según requerimiento (Experto)
    public List<Gasto> filtrarGastos(List<Integer> meses, LocalDate inicio, LocalDate fin, List<Categoria> categorias) {
        return todosLosGastos.stream().filter(g -> {
            boolean mesOk = (meses == null || meses.isEmpty() || meses.contains(g.getFecha().getMonthValue()));
            boolean fechaOk = (inicio == null || !g.getFecha().isBefore(inicio)) && (fin == null || !g.getFecha().isAfter(fin));
            boolean catOk = (categorias == null || categorias.isEmpty() || categorias.contains(g.getCategoria()));
            return mesOk && fechaOk && catOk;
        }).collect(Collectors.toList());
    }
}