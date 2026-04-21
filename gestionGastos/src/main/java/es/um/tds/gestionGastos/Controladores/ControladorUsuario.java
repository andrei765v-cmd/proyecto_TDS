package es.um.tds.gestionGastos.Controladores;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import es.um.tds.gestionGastos.modelo.Usuario;

public class ControladorUsuario {
    private final Set<Usuario> usuarios = new HashSet<>();

    public ControladorUsuario() {
        usuarios.add(new Usuario("prueba 1"));
        usuarios.add(new Usuario("2 abeurp"));
        usuarios.add(new Usuario("pepe"));
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

    public Set<Usuario> getUsuarios() {
        return Collections.unmodifiableSet(usuarios);
    }
}
