package es.um.tds.gestionGastos.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Optional;

public class RepositorioUsuarios {
    private static RepositorioUsuarios unicaInstancia;
    private final ObservableList<Usuario> usuarios = FXCollections.observableArrayList();

    private RepositorioUsuarios() {
        usuarios.add(new Usuario("prueba 1"));
        usuarios.add(new Usuario("2 abeurp"));
        usuarios.add(new Usuario("pepe"));
    }

    public static RepositorioUsuarios getInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new RepositorioUsuarios();
        }
        return unicaInstancia;
    }

    public Usuario registrar(String nombre) {
        Optional<Usuario> existente = getUsuario(nombre);
        if (existente.isPresent()) return existente.get();
        Usuario u = new Usuario(nombre);
        usuarios.add(u);
        return u;
    }

    public Optional<Usuario> getUsuario(String nombre) {
        return usuarios.stream().filter(u -> u.getNombre().equalsIgnoreCase(nombre)).findFirst();
    }

    public ObservableList<Usuario> getUsuarios() {
        return usuarios;
    }
}