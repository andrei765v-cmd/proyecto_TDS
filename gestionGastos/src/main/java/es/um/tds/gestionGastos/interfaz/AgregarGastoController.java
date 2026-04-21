package es.um.tds.gestionGastos.interfaz;

import java.time.LocalDate;
import java.util.function.UnaryOperator;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class AgregarGastoController {

    @FXML private TextField txtMonto;
    @FXML private TextField txtDescripcion;
    @FXML private ComboBox<Usuario> comboUsuario;
    @FXML private ComboBox<String> comboCuenta;
    @FXML private ComboBox<Categoria> comboCategoria;
    @FXML private DatePicker pickerFecha;

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
        
        txtMonto.setTextFormatter(new TextFormatter<>(filter));

        // Cargar usuarios
        comboUsuario.getItems().addAll(controladorPrincipal.getUsuarios());
        
        // Cargar categorias
        comboCategoria.getItems().addAll(controladorPrincipal.getCategorias());
        
        // Fecha por defecto
        pickerFecha.setValue(LocalDate.now());
        
        comboCuenta.getItems().addAll(controladorPrincipal.getCuentas().keySet());
    }

    @FXML
    void guardarGasto(ActionEvent event) {    	
    	try {
            // Validación de datos
    		String montoStr = txtMonto.getText();
            String descripcion = txtDescripcion.getText();
            Usuario usuario = comboUsuario.getValue();
            String cuenta = comboCuenta.getValue();
            LocalDate fecha = pickerFecha.getValue();
            Categoria categoria = comboCategoria.getValue();

            if (/*monto == null || monto.isEmpty() ||*/ descripcion == null || descripcion.isEmpty() || usuario == null || cuenta == null || categoria == null) {
            	mostrarError("Campos incompletos", "Por favor, rellena todos los campos.");
                return;
            }
            double monto = Double.parseDouble(montoStr);

            System.out.println("Guardando gasto de " + usuario + ": " + descripcion + " por un valor de " + monto + "€");
            // Creacion del gasto
            controladorPrincipal.registrarGastoEnCuenta(monto, fecha, descripcion, categoria, usuario, cuenta);
            System.out.println("Gasto registrado con éxito a través del ControladorPrincipal.");
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarError("Error de formato", "El monto debe ser un número válido.");
        }
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