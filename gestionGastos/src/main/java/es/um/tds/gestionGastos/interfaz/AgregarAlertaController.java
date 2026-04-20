package es.um.tds.gestionGastos.interfaz;

import java.util.function.UnaryOperator;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.Categoria;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class AgregarAlertaController {

    @FXML private ToggleGroup periodoGroup;
    @FXML private ComboBox<Categoria> comboCategoriaAlerta;
    @FXML private TextField txtLimite;

    private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();

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
        txtLimite.setTextFormatter(new TextFormatter<>(filter));

        // Cargar categorías
        comboCategoriaAlerta.getItems().addAll(controladorPrincipal.getCategorias());
    }
    
    @FXML
    void activarAlerta() {
        try {
            String limiteStr = txtLimite.getText();
            RadioButton seleccionado = (RadioButton) periodoGroup.getSelectedToggle();
            Categoria categoria = comboCategoriaAlerta.getValue();

            // Validaciones básicas de UI
            if (limiteStr == null || limiteStr.isEmpty() || seleccionado == null || categoria == null) {
                mostrarError("Campos incompletos", "Por favor, rellena el límite, selecciona un periodo y una categoría.");
                return;
            }

            double limite = Double.parseDouble(limiteStr);
            String tipoAlerta = seleccionado.getText(); // Mensual o Semanal

            // Creacion de la alerta
            if (tipoAlerta.equalsIgnoreCase("Mensual")) {
            	controladorPrincipal.registrarAlertaMensual(limite, categoria);
            } else {
                controladorPrincipal.registrarAlertaSemanal(limite, categoria);
            }

            System.out.println("Alerta " + tipoAlerta + " creada con éxito para " + categoria.getNombre());
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarError("Error de formato", "El límite debe ser un número válido.");
        }
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