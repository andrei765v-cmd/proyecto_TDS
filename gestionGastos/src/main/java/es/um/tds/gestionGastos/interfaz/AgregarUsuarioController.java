package es.um.tds.gestionGastos.interfaz;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AgregarUsuarioController {

    @FXML private TextField txtNombre;

    @FXML
    private void onGuardar() {
        String nombre = txtNombre.getText();
        if (nombre != null && !nombre.trim().isEmpty()) {
            ControladorPrincipal.getInstancia().registrarUsuario(nombre.trim());
            cerrarModal();
        }
    }

    @FXML
    private void onCancelar() {
        cerrarModal();
    }

    private void cerrarModal() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}
