package es.um.tds.gestionGastos.Controladores;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import es.um.tds.gestionGastos.modelo.Categoria;

public class ControladorCategoria {
    private final Set<Categoria> categorias = new HashSet<>();

    public ControladorCategoria() {
        categorias.add(new Categoria("Alimentación"));
        categorias.add(new Categoria("Transporte"));
        categorias.add(new Categoria("Entretenimiento"));
    }

    public Categoria crear(String nombre) {
        Categoria c = new Categoria(nombre);
        categorias.add(c);
        return c;
    }

    public Set<Categoria> getCategorias() {
        return Collections.unmodifiableSet(categorias);
    }
}
