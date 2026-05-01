package es.um.tds.gestionGastos.interfaz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.Usuario;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AgregarCuentaController {

    @FXML private TextField txtNombreCuenta;
    @FXML private ComboBox<String> comboTipoCuenta;
    @FXML private VBox vboxUsuarios;

    private List<Usuario> todosLosUsuarios;
    // Mapeo entre CheckBox de usuario y su campo de texto para el porcentaje (si aplica)
    private Map<Usuario, HBox> userRows = new HashMap<>();
    private Map<Usuario, CheckBox> userChecks = new HashMap<>();
    private Map<Usuario, TextField> userPercentages = new HashMap<>();

    @FXML
    public void initialize() {
        comboTipoCuenta.getItems().addAll("Equitativa", "Porcentual");
        comboTipoCuenta.setValue("Equitativa");

        todosLosUsuarios = new ArrayList<>(ControladorPrincipal.getInstancia().getUsuarios());

        comboTipoCuenta.valueProperty().addListener((obs, oldVal, newVal) -> {
            actualizarVistaUsuarios(newVal.equals("Porcentual"));
        });

        actualizarVistaUsuarios(false);
    }

    private void actualizarVistaUsuarios(boolean esPorcentual) {
        vboxUsuarios.getChildren().clear();
        userRows.clear();
        userChecks.clear();
        userPercentages.clear();

        for (Usuario u : todosLosUsuarios) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);

            CheckBox check = new CheckBox(u.getNombre());
            check.setStyle("-fx-text-fill: #b2b2b2;");
            userChecks.put(u, check);
            row.getChildren().add(check);

            if (esPorcentual) {
                TextField txtPorcentaje = new TextField();
                txtPorcentaje.setPromptText("%");
                txtPorcentaje.setPrefWidth(50);
                txtPorcentaje.setStyle("-fx-text-fill: white; -fx-background-color: #2b2b2b; -fx-border-color: #444; -fx-border-radius: 3; -fx-background-radius: 3;");
                txtPorcentaje.setDisable(true); // deshabilitado hasta que se marque el checkbox
                
                check.selectedProperty().addListener((obs, oldV, newV) -> {
                    txtPorcentaje.setDisable(!newV);
                });

                userPercentages.put(u, txtPorcentaje);
                row.getChildren().add(txtPorcentaje);
            }

            userRows.put(u, row);
            vboxUsuarios.getChildren().add(row);
        }
    }

    @FXML
    private void onGuardar() {
        String nombre = txtNombreCuenta.getText();
        if (nombre == null || nombre.trim().isEmpty()) {
            mostrarError("El nombre de la cuenta no puede estar vacío.");
            return;
        }

        List<Usuario> seleccionados = new ArrayList<>();
        for (Usuario u : todosLosUsuarios) {
            if (userChecks.get(u).isSelected()) {
                seleccionados.add(u);
            }
        }

        if (seleccionados.size() < 2) {
            mostrarError("Debe seleccionar al menos 2 usuarios.");
            return;
        }

        try {
            boolean esPorcentual = comboTipoCuenta.getValue().equals("Porcentual");
            if (!esPorcentual) {
                ControladorPrincipal.getInstancia().crearCuentaEquitativa(nombre.trim(), seleccionados);
            } else {
                Map<Usuario, Double> porcentajes = new HashMap<>();
                double suma = 0;
                for (Usuario u : seleccionados) {
                    String val = userPercentages.get(u).getText();
                    double p = Double.parseDouble(val.replace(",", "."));
                    porcentajes.put(u, p);
                    suma += p;
                }
                if (Math.abs(suma - 100.0) > 0.01) {
                    mostrarError("La suma de porcentajes debe ser 100%. Actualmente es: " + suma);
                    return;
                }
                ControladorPrincipal.getInstancia().crearCuentaPorcentual(nombre.trim(), seleccionados, porcentajes);
            }
            cerrarModal();
        } catch (NumberFormatException e) {
            mostrarError("Los porcentajes deben ser valores numéricos válidos.");
        } catch (Exception e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        cerrarModal();
    }

    private void cerrarModal() {
        Stage stage = (Stage) txtNombreCuenta.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
