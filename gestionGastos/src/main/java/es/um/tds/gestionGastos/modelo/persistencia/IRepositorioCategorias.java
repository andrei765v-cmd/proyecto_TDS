package es.um.tds.gestionGastos.modelo.persistencia;

import es.um.tds.gestionGastos.modelo.Categoria;
import javafx.collections.ObservableList;

public interface IRepositorioCategorias {
    Categoria crear(String nombre);
    ObservableList<Categoria> getCategorias();
    void cargar();
    void guardar();
}
