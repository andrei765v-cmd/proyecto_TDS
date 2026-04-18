package es.um.tds.gestionGastos.Controladores;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;

public class ControladorAgregarGasto {

    @FXML private TextField txtMonto;
    @FXML private TextField txtDescripcion;
    @FXML private ComboBox<String> comboCategoria;
    @FXML private DatePicker pickerFecha;

    @FXML
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
        txtMonto.setTextFormatter(textFormatter);
//        comboCategoria.getItems().addAll("Alimentación", "Transporte", "Ocio");
    	
    }

    @FXML
    void guardarGasto(ActionEvent event) {
    	String monto = txtMonto.getText();
        String descripcion = txtDescripcion.getText();

        if (monto == null || monto.isEmpty() || descripcion == null || descripcion.isEmpty()) {
            mostrarError("Campos obligatorios", "Por favor, rellena el importe y la descripción.");
            return;
        }
    	System.out.println("Guardando gasto: " + descripcion + " por un valor de " + monto + "€");
    	// Lógica de negocio para guardar.
        cerrarVentana();
    }

    @FXML
    void cancelar(ActionEvent event) {
    	cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtMonto.getScene().getWindow();
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