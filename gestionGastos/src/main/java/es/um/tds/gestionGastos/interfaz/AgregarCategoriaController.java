package es.um.tds.gestionGastos.interfaz;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AgregarCategoriaController {

    @FXML private TextField txtNombre;

    @FXML
    void guardar(ActionEvent event) {
        String nombre = txtNombre.getText();

        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarError("Campos incompletos", "Por favor, introduce un nombre para la categoría.");
            return;
        }

        ControladorPrincipal.getInstancia().crearCategoria(nombre.trim());
        cerrarVentana();
    }

    @FXML
    void cancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
