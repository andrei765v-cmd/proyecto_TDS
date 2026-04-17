package es.um.tds.gestionGastos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) {
	    try {
	        // 1. Cargamos el archivo FXML
	        // Importante: asegúrate de que el nombre coincida exactamente
	        Parent root = FXMLLoader.load(getClass().getResource("VistaPrincipal.fxml"));

	        // 2. Creamos la escena con el contenido del FXML
	        Scene scene = new Scene(root);

	        // 3. Configuramos el escenario
	        primaryStage.setTitle("Aplicacion TDS");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Error: No se ha podido cargar el archivo FXML. Revisa el nombre y la ubicación.");
	    }
	}

    public static void main(String[] args) {
        launch(args);
    }
}