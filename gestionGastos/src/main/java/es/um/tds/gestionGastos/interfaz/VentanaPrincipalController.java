package es.um.tds.gestionGastos.interfaz;

import java.io.IOException;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.Gasto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class VentanaPrincipalController {

	private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
	
    @FXML private TabPane mainTabPane;
    @FXML private StackPane contentArea;	// Contenedor central
    @FXML private Tab tabMisGastos;
    @FXML private Tab tabDashboard;
    @FXML private Tab tabGastosCompartidos;
    @FXML private Tab tabAlertas;
    
    // tabla
    @FXML private TableView<Gasto> tablaGastos;
    @FXML private TableColumn<Gasto, String> colFecha;
    @FXML private TableColumn<Gasto, String> colDescripcion;
    @FXML private TableColumn<Gasto, String> colCategoria;
    @FXML private TableColumn<Gasto, String> colUsuario;
    @FXML private TableColumn<Gasto, Double> colMonto;
    
    private Node vistaOriginalTabla;	// tabla inicial
    
    @FXML
    public void initialize() {
    	// configurar columnas tabla
    	colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        
        // Guardamos la vista inicial para la navegación
        if (!contentArea.getChildren().isEmpty()) {
            vistaOriginalTabla = contentArea.getChildren().get(0);
        }
        // Cargar datos iniciales si los hubiera
        refrescarTabla();
        
        // Escuchar cambios de pestaña
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            cargarTab(newTab);
        });
    }

    private void cargarTab(Tab tab) {
        if (tab == null) return;
        
        
        if (tab == tabMisGastos) {
        	contentArea.getChildren().setAll(vistaOriginalTabla);	// mostrar la tabla original
        } else if (tab == tabGastosCompartidos) {
            cargarVista("/es/um/tds/gestionGastos/VistaGastosCompartidos.fxml");
        } else if (tab == tabAlertas) {
            cargarVista("/es/um/tds/gestionGastos/VistaAlertas.fxml");
        } else if (tab == tabDashboard) {
            cargarVista("/es/um/tds/gestionGastos/VistaDashboard.fxml");
        }
    }

    private void cargarVista(String fxml) {
        try {
            Node vista = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().setAll(vista);
        } catch (IOException e) {
        	System.err.println("Error cargando la vista: " + fxml);
            e.printStackTrace();
        }
    }
    
    @FXML
    private void refrescarTabla() {
        // Obtener datos del controlador
        tablaGastos.getItems().setAll(controladorPrincipal.getGastosPersonales());
    }
    
    @FXML
    private void onNuevoGasto() {
        abrirVentanaModal("/es/um/tds/gestionGastos/VistaAgregarGasto.fxml", "Añadir Gasto");
    }

    @FXML
    private void onNuevaAlerta() {
        abrirVentanaModal("/es/um/tds/gestionGastos/VistaAgregarAlerta.fxml", "Añadir Gasto");
    }
    
    @FXML
    private void onNuevoUsuario() {
        abrirVentanaModal("/es/um/tds/gestionGastos/VistaAgregarUsuario.fxml", "Nuevo Usuario");
    }

    @FXML
    private void onNuevaCuenta() {
    	abrirVentanaModal("/es/um/tds/gestionGastos/VistaAgregarCuenta.fxml", "Nueva Cuenta");
    }
    private void abrirVentanaModal(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            refrescarTabla();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}