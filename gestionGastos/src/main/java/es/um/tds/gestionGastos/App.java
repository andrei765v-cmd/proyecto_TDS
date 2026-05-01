package es.um.tds.gestionGastos;

import es.um.tds.gestionGastos.cli.CLI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
		// Si se pasa el argumento "--cli", arranca el modo de consola
		for (String arg : args) {
			if (arg.equalsIgnoreCase("--cli")) {
				new CLI().iniciar();
				return;
			}
		}
		// Por defecto, arranca la interfaz gráfica
		launch(args);
	}
}