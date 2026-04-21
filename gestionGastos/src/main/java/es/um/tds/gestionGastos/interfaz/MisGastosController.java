package es.um.tds.gestionGastos.interfaz;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MisGastosController {

    private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    private FilteredList<Gasto> gastosFiltrados;
    
    @FXML private ComboBox<Usuario> comboUsuario;
    @FXML private TableView<Gasto> tablaGastos;
    @FXML private TableColumn<Gasto, String> colFecha;
    @FXML private TableColumn<Gasto, String> colDescripcion;
    @FXML private TableColumn<Gasto, String> colCategoria;
    @FXML private TableColumn<Gasto, String> colUsuario;
    @FXML private TableColumn<Gasto, Double> colMonto;

    @FXML
    public void initialize() {
        // Configurar columnas de la tabla 
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        ObservableList<Gasto> masterData = controladorPrincipal.getGastosObservable();
        gastosFiltrados = new FilteredList<>(masterData, p -> true);
        SortedList<Gasto> sortedData = new SortedList<>(gastosFiltrados);
        sortedData.comparatorProperty().bind(tablaGastos.comparatorProperty());
        tablaGastos.setItems(sortedData);
        
        // Cargar usuarios
        comboUsuario.getItems().addAll(controladorPrincipal.getUsuarios());

	    comboUsuario.valueProperty().addListener((observable, viejoUsuario, nuevoUsuario) -> {
	        gastosFiltrados.setPredicate(gasto -> {
	            // Si no hay nada seleccionado, mostramos todos los gastos
	            if (nuevoUsuario == null) {
	                return true;
	            }
	            // Comparamos el usuario del gasto con el seleccionado en el combo
	            return gasto.getUsuario().equals(nuevoUsuario);
	        });
	    });
    }
}