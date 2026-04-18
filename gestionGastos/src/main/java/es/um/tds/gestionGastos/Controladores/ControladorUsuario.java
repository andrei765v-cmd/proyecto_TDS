package es.um.tds.gestionGastos.Controladores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import es.um.tds.gestionGastos.modelo.Usuario;

public class ControladorUsuario {
    private final List<Usuario> usuarios = new ArrayList<>();

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

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }
}
