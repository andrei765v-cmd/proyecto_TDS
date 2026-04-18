package es.um.tds.gestionGastos.Controladores;

import java.util.function.UnaryOperator;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class ControladorAgregarAlerta {

    @FXML private ToggleGroup periodoGroup;
    @FXML private ComboBox<String> comboCategoriaAlerta;
    @FXML private TextField txtLimite;

    public void initialize() {
    	// Filtro que solo permite números y un punto decimal
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        };
        
        TextFormatter<Double> textFormatter = new TextFormatter<>(filter);
        txtLimite.setTextFormatter(textFormatter);	
    }
    
    @FXML
    void activarAlerta() {
        
    	String limite = txtLimite.getText();
    	RadioButton seleccionado = (RadioButton) periodoGroup.getSelectedToggle();
    	String tipo = seleccionado.getText();
    	
    	if (limite == null || limite.isEmpty()) {
            mostrarError("Campos obligatorios", "Por favor, rellene el limite");
            return;
        }
    	
        System.out.println("Alerta activa: " + tipo + " con límite " + limite);
        // Lógica de negocio para guardar.
        cerrarVentana();
    }

    @FXML
    void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtLimite.getScene().getWindow();
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