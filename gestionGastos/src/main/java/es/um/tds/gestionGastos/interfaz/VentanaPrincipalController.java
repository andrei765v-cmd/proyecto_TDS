package es.um.tds.gestionGastos.interfaz;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class VentanaPrincipalController {

	private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
	
	private Map<String, Node> cacheVistas = new HashMap<>();

    @FXML private TabPane mainTabPane;
    @FXML private StackPane contentArea;
    @FXML private Tab tabMisGastos;
    @FXML private Tab tabDashboard;
    @FXML private Tab tabGastosCompartidos;
    @FXML private Tab tabAlertas;
    
    private Node vistaActual;
    
    @FXML
    public void initialize() {
    	cargarTab(tabMisGastos);
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            cargarTab(newTab);
        });
        }

    private Node cargarNodo(String fxml) {
        try {
            return FXMLLoader.load(getClass().getResource(fxml));
        } catch (IOException e) {
        	System.err.println("Error cargando la vista: " + fxml);
            e.printStackTrace();
            return null;
        }
    }

    private void cargarTab(Tab tab) {
        if (tab == tabMisGastos) {
            cargarVista("/es/um/tds/gestionGastos/VistaMisGastos.fxml");
        } else if (tab == tabDashboard) {
            cargarVista("/es/um/tds/gestionGastos/VistaDashboard.fxml");
        } else if (tab == tabGastosCompartidos) {
        	cargarVista("/es/um/tds/gestionGastos/VistaGastosCompartidos.fxml");
        } else if (tab == tabAlertas) {        	
        	cargarVista("/es/um/tds/gestionGastos/VistaAlertas.fxml");
        }
    }
    
    private void cargarVista(String fxml) {
        try {
            if (!cacheVistas.containsKey(fxml)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                Node vista = loader.load();
                cacheVistas.put(fxml, vista);
            }
            // Esto NO cambia el diseño de las pestañas arriba, solo cambia el centro
            contentArea.getChildren().setAll(cacheVistas.get(fxml)); 
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}