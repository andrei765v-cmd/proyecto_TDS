package es.um.tds.gestionGastos.interfaz;

import java.util.HashMap;
import java.util.Map;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.Usuario;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaCompartida;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaPorcentual;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ConfigurarCuentaController {

    @FXML private TextField txtNombre;
    @FXML private VBox vboxProporcional;
    @FXML private VBox vboxParticipantes;
    @FXML private Label lblTotalPorcentaje;

    private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
    private String nombreOriginal;
    private CuentaCompartida cuenta;
    private Map<Usuario, TextField> fieldsPorcentaje = new HashMap<>();

    public void setCuenta(String nombre) {
        this.nombreOriginal = nombre;
        this.cuenta = controladorPrincipal.getCuentas().get(nombre);
        
        txtNombre.setText(nombre);
        
        if (cuenta instanceof CuentaPorcentual) {
            vboxProporcional.setVisible(true);
            vboxProporcional.setManaged(true);
            cargarParticipantesPorcentajes((CuentaPorcentual) cuenta);
        } else {
            vboxProporcional.setVisible(false);
            vboxProporcional.setManaged(false);
        }
    }

    private void cargarParticipantesPorcentajes(CuentaPorcentual cp) {
        vboxParticipantes.getChildren().clear();
        Map<Usuario, Double> porcentajes = cp.getPorcentajes();
        
        for (Usuario u : cp.getUsuarios()) {
            HBox row = new HBox(10);
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            
            Label lblUser = new Label(u.getNombre());
            HBox.setHgrow(lblUser, Priority.ALWAYS);
            lblUser.setMaxWidth(Double.MAX_VALUE);
            lblUser.setStyle("-fx-text-fill: white;");
            
            TextField txtP = new TextField(String.valueOf(porcentajes.get(u)));
            txtP.setPrefWidth(60);
            txtP.getStyleClass().add("dark-input");
            
            txtP.textProperty().addListener((obs, oldV, newV) -> actualizarTotal());
            
            fieldsPorcentaje.put(u, txtP);
            row.getChildren().addAll(lblUser, new Label("%"), txtP);
            vboxParticipantes.getChildren().add(row);
        }
        actualizarTotal();
    }

    private void actualizarTotal() {
        double total = 0;
        for (TextField f : fieldsPorcentaje.values()) {
            try {
                total += Double.parseDouble(f.getText());
            } catch (NumberFormatException ignored) {}
        }
        lblTotalPorcentaje.setText("Total: " + String.format("%.2f", total) + "%");
        if (Math.abs(total - 100) < 0.01) {
            lblTotalPorcentaje.setStyle("-fx-text-fill: #00ff88;");
        } else {
            lblTotalPorcentaje.setStyle("-fx-text-fill: #ff4d4d;");
        }
    }

    @FXML
    private void onGuardar() {
        String nuevoNombre = txtNombre.getText().trim();
        if (nuevoNombre.isEmpty()) {
            mostrarError("Nombre inválido", "El nombre de la cuenta no puede estar vacío.");
            return;
        }

        try {
            // Actualizar nombre
            controladorPrincipal.actualizarCuenta(nombreOriginal, nuevoNombre);
            
            // Actualizar porcentajes si aplica
            if (cuenta instanceof CuentaPorcentual) {
                Map<Usuario, Double> nuevosPorcentajes = new HashMap<>();
                double suma = 0;
                for (Map.Entry<Usuario, TextField> entry : fieldsPorcentaje.entrySet()) {
                    double val = Double.parseDouble(entry.getValue().getText());
                    nuevosPorcentajes.put(entry.getKey(), val);
                    suma += val;
                }
                
                if (Math.abs(suma - 100) > 0.01) {
                    mostrarError("Error de porcentajes", "La suma de los porcentajes debe ser exactamente 100%. Actual: " + suma + "%");
                    return;
                }
                
                ((CuentaPorcentual) cuenta).setPorcentajes(nuevosPorcentajes);
            }
            
            cerrar();
        } catch (Exception e) {
            mostrarError("Error al guardar", e.getMessage());
        }
    }

    @FXML
    private void onEliminar() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que quieres eliminar la cuenta '" + nombreOriginal + "'? Esta acción no se puede deshacer.", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                controladorPrincipal.eliminarCuenta(nombreOriginal);
                cerrar();
            }
        });
    }

    @FXML
    private void onCancelar() {
        cerrar();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cerrar() {
        ((Stage) txtNombre.getScene().getWindow()).close();
    }
}
