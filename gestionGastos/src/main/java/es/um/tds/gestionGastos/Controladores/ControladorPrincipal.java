package es.um.tds.gestionGastos.Controladores;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;

public class ControladorPrincipal {

    @FXML private TabPane mainTabPane;
    @FXML private StackPane contentArea; // El contenedor central
    @FXML private Tab tabAlertas;

    @FXML
    public void initialize() {
        // Escuchamos el cambio de selección en las pestañas
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == tabAlertas) {
                cargarVistaAlertas();
            }
            // Aquí podrías añadir else if para las otras pestañas
        });
    }

    private void cargarVistaAlertas() {
        try {
            // Cargamos el FXML de alertas que diseñamos antes
            Node vistaAlertas = FXMLLoader.load(getClass().getResource("/es/um/tds/gestionGastos/VistaAlertas.fxml"));
            
            // Limpiamos el centro y ponemos la nueva vista
            contentArea.getChildren().setAll(vistaAlertas);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}