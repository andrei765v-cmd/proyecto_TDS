package es.um.tds.gestionGastos.modelo.persistencia;

import java.time.LocalDate;
import java.util.List;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;

public interface IRepositorioGastos {
    Gasto registrarGastoPersonal(double cantidad, LocalDate fecha, String descripcion, Categoria categoria, Usuario usuario);
    void addGasto(Gasto gasto);
    void removeGasto(Gasto gasto);
    List<Gasto> getGastos();
    List<Gasto> getGastosPersonales(Usuario usuario);
    List<Gasto> filtrarGastos(List<Integer> meses, LocalDate inicio, LocalDate fin, List<Categoria> categorias);
    void cargar();
    void guardar();
}
