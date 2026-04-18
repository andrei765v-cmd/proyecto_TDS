package es.um.tds.gestionGastos.Controladores;

import java.io.IOException;

import es.um.tds.gestionGastos.modelo.Gasto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControladorPrincipal {

	private ControladorDashboard controladorDashboard;
	
    @FXML private TabPane mainTabPane;
    @FXML private StackPane contentArea; // Contenedor central
    @FXML private Tab tabMisGastos;
    @FXML private Tab tabDashboard;
    @FXML private Tab tabGastosCompartidos;
    @FXML private Tab tabAlertas;
    @FXML private TableView<Gasto> tablaGastos;
    
    private Node vistaOriginalTabla; // tabla inicial
    
    @FXML
    public void initialize() {
        vistaOriginalTabla = contentArea.getChildren().get(0);
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == tabAlertas) {
                cargarVista("/es/um/tds/gestionGastos/VistaAlertas.fxml");
            } else if (newTab == tabGastosCompartidos) {
                cargarVista("/es/um/tds/gestionGastos/VistaGastosCompartidos.fxml");
            } else if (newTab == tabMisGastos) {
                contentArea.getChildren().setAll(vistaOriginalTabla);	// mostrar la tabla original
            } else if (newTab == tabDashboard) {
                cargarVista("/es/um/tds/gestionGastos/VistaDashboard.fxml");
            }
        });
    }
    
    private void cargarVista(String fxml) {
        try {
            Node vista = FXMLLoader.load(getClass().getResource(fxml));
            contentArea.getChildren().setAll(vista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void abrirModalNuevoGasto() {
        abrirModal("/es/um/tds/gestionGastos/VistaAgregarGasto.fxml", "Añadir Gasto");
    }
    
    @FXML
    private void abrirModalNuevaAlerta() {
        abrirModal("/es/um/tds/gestionGastos/VistaAgregarAlerta.fxml", "Añadir Alerta");
    }
    
    private void abrirModal(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            
            stage.initStyle(StageStyle.UNDECORATED);	// Quita la cabecera de Windows
            stage.initModality(Modality.APPLICATION_MODAL);
            
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Pausa la ejecución hasta que se cierre el modal
            
            //refrescarTabla();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}