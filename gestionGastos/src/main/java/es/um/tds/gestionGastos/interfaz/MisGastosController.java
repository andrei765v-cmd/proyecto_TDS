package es.um.tds.gestionGastos.interfaz;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;

public class MisGastosController {

    private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    private FilteredList<Gasto> gastosFiltrados;
    @FXML private TableView<Gasto> tablaGastos;
    @FXML private TableColumn<Gasto, String> colFecha;
    @FXML private TableColumn<Gasto, String> colDescripcion;
    @FXML private TableColumn<Gasto, String> colCategoria;
    @FXML private TableColumn<Gasto, String> colUsuario;
    @FXML private TableColumn<Gasto, Double> colMonto;

    @FXML public void initialize() {
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
        
        configurarMenuContextual();
    }
    
    @FXML
    private void onImportarCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar fichero CSV");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Ficheros CSV", "*.csv")
        );
        
        Stage stage = (Stage) tablaGastos.getScene().getWindow();
        File fichero = fileChooser.showOpenDialog(stage);
        
        if (fichero != null) {
            try {
                int importados = controladorPrincipal.importarGastosDesdeFichero(fichero);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Importación completada");
                alert.setHeaderText(null);
                alert.setContentText(importados + " gasto(s) importados correctamente desde " + fichero.getName());
                alert.showAndWait();
            } catch (Exception e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error al importar");
                error.setHeaderText("No se pudo importar el fichero");
                error.setContentText(e.getMessage());
                error.showAndWait();
            }
        }
    }
    
    private void configurarMenuContextual() {
        ContextMenu contextMenu = new ContextMenu();
        
        MenuItem editarItem = new MenuItem("Editar Gasto");
        editarItem.setOnAction(e -> {
            Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                // Solo el propietario del gasto puede editarlo
                Usuario activo = controladorPrincipal.getUsuarioActivo();
                if (!seleccionado.getUsuario().equals(activo)) {
                    new Alert(Alert.AlertType.WARNING, "Solo puedes editar tus propios gastos.").showAndWait();
                    return;
                }
                abrirModalEdicion(seleccionado);
            }
        });
        
        MenuItem eliminarItem = new MenuItem("Eliminar Gasto");
        eliminarItem.setOnAction(e -> {
            Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que quieres eliminar este gasto?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        controladorPrincipal.eliminarGasto(seleccionado, null);
                    }
                });
            }
        });
        
        contextMenu.getItems().addAll(editarItem, eliminarItem);
        tablaGastos.setContextMenu(contextMenu);
    }
    
    private void abrirModalEdicion(Gasto gasto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/um/tds/gestionGastos/VistaAgregarGasto.fxml"));
            Parent root = loader.load();
            
            AgregarGastoController controller = loader.getController();
            controller.cargarGastoEditar(gasto, null);
            
            Stage stage = new Stage();
            stage.setTitle("Editar Gasto");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}