package es.um.tds.gestionGastos.modelo.persistencia;

import java.util.Optional;

import es.um.tds.gestionGastos.modelo.Usuario;
import javafx.collections.ObservableList;

public interface IRepositorioUsuarios {
    Usuario registrar(String nombre);
    Optional<Usuario> getUsuario(String nombre);
    ObservableList<Usuario> getUsuarios();
    void cargar();
    void guardar();
}
