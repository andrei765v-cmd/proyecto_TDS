package es.um.tds.gestionGastos.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RepositorioCategorias {
    private static RepositorioCategorias unicaInstancia;
    private final ObservableList<Categoria> categorias = FXCollections.observableArrayList();

    private RepositorioCategorias() {
        categorias.add(new Categoria("Alimentación"));
        categorias.add(new Categoria("Transporte"));
        categorias.add(new Categoria("Entretenimiento"));
    }

    public static RepositorioCategorias getInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new RepositorioCategorias();
        }
        return unicaInstancia;
    }

    public Categoria crear(String nombre) {
        Categoria c = new Categoria(nombre);
        categorias.add(c);
        return c;
    }

    public ObservableList<Categoria> getCategorias() {
        return categorias;
    }
}
