package es.um.tds.gestionGastos.interfaz;

import java.io.IOException;
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

public class VentanaPrincipalController {

    @FXML private TabPane mainTabPane;
    @FXML private StackPane contentArea;
    @FXML private Tab tabMisGastos;
    @FXML private Tab tabCompartidos;
    @FXML private Tab tabAlertas;

    @FXML
    public void initialize() {
        // Al iniciar, cargamos la pestaña que esté seleccionada por defecto
        cargarTab(mainTabPane.getSelectionModel().getSelectedItem());

        // Escuchar cambios de pestaña
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            cargarTab(newTab);
        });
    }

    private void cargarTab(Tab tab) {
        if (tab == null) return;
        
        if (tab == tabMisGastos) {
            cargarSubVista("/es/um/tds/gestionGastos/VistaMisGastos.fxml");
        } else if (tab == tabCompartidos) {
            cargarSubVista("/es/um/tds/gestionGastos/VistaCompartidos.fxml");
        } else if (tab == tabAlertas) {
            cargarSubVista("/es/um/tds/gestionGastos/VistaAlertas.fxml");
        }
    }

    private void cargarSubVista(String ruta) {
        try {
            Node nodo = FXMLLoader.load(getClass().getResource(ruta));
            contentArea.getChildren().setAll(nodo);
        } catch (IOException e) {
            System.err.println("Error cargando la subvista: " + ruta);
            e.printStackTrace();
        }
    }

    @FXML
    private void onNuevoGasto() {
        abrirVentanaModal("/es/um/tds/gestionGastos/VistaNuevoGasto.fxml", "Añadir Gasto");
    }

    @FXML
    private void onNuevoUsuario() {
        abrirVentanaModal("/es/um/tds/gestionGastos/VistaNuevoUsuario.fxml", "Nuevo Usuario");
    }

    private void abrirVentanaModal(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}