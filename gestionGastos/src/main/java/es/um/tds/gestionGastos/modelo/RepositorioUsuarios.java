package es.um.tds.gestionGastos.modelo;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;

import es.um.tds.gestionGastos.modelo.persistencia.IRepositorioUsuarios;
import es.um.tds.gestionGastos.modelo.persistencia.JsonStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RepositorioUsuarios implements IRepositorioUsuarios {
    private static final String FICHERO = "usuarios.json";
    private static RepositorioUsuarios unicaInstancia;
    private final ObservableList<Usuario> usuarios = FXCollections.observableArrayList();

    private RepositorioUsuarios() {}

    public static RepositorioUsuarios getInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new RepositorioUsuarios();
        }
        return unicaInstancia;
    }

    @Override
    public Usuario registrar(String nombre) {
        Optional<Usuario> existente = getUsuario(nombre);
        if (existente.isPresent()) return existente.get();
        Usuario u = new Usuario(nombre);
        usuarios.add(u);
        return u;
    }

    @Override
    public Optional<Usuario> getUsuario(String nombre) {
        return usuarios.stream().filter(u -> u.getNombre().equalsIgnoreCase(nombre)).findFirst();
    }

    @Override
    public ObservableList<Usuario> getUsuarios() {
        return usuarios;
    }

    @Override
    public void cargar() {
        List<Usuario> cargados = JsonStore.leer(FICHERO, new TypeReference<List<Usuario>>() {}, List.of());
        usuarios.setAll(cargados);
        if (usuarios.isEmpty()) {
            registrar("Yo");
        }
    }

    @Override
    public void guardar() {
        JsonStore.escribir(FICHERO, usuarios);
    }
}
