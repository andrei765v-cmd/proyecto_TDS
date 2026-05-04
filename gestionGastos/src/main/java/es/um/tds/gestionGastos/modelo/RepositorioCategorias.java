package es.um.tds.gestionGastos.modelo;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import es.um.tds.gestionGastos.modelo.persistencia.IRepositorioCategorias;
import es.um.tds.gestionGastos.modelo.persistencia.JsonStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RepositorioCategorias implements IRepositorioCategorias {
    private static final String FICHERO = "categorias.json";
    private static RepositorioCategorias unicaInstancia;
    private final ObservableList<Categoria> categorias = FXCollections.observableArrayList();

    private RepositorioCategorias() {}

    public static RepositorioCategorias getInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new RepositorioCategorias();
        }
        return unicaInstancia;
    }

    @Override
    public Categoria crear(String nombre) {
        Categoria c = new Categoria(nombre);
        categorias.add(c);
        return c;
    }

    @Override
    public ObservableList<Categoria> getCategorias() {
        return categorias;
    }

    @Override
    public void cargar() {
        List<Categoria> cargadas = JsonStore.leer(FICHERO, new TypeReference<List<Categoria>>() {}, List.of());
        categorias.setAll(cargadas);
    }

    @Override
    public void guardar() {
        JsonStore.escribir(FICHERO, categorias);
    }
}
