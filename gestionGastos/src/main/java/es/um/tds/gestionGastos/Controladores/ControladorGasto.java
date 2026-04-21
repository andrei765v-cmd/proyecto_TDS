package es.um.tds.gestionGastos.Controladores;

import java.time.LocalDate;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ControladorGasto {
    private final ObservableList<Gasto> todosLosGastos = FXCollections.observableArrayList();

    public void registrarGastoPersonal(double cantidad, LocalDate fecha, String descripcion, Categoria categoria, Usuario usuario) {
        Gasto nuevo = new Gasto(cantidad, fecha, descripcion, categoria, usuario);
        todosLosGastos.add(nuevo); 
    }
    
    public ObservableList<Gasto> getGastos() {
        return todosLosGastos;
    }
    
    public ObservableList<Gasto> getGastosPersonales(Usuario usuario) {
        return todosLosGastos;
    }
}
