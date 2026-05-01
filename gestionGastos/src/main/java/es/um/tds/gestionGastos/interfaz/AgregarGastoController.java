package es.um.tds.gestionGastos.interfaz;

import java.time.LocalDate;
import java.util.function.UnaryOperator;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaCompartida;
import es.um.tds.gestionGastos.modelo.Gasto;
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
    @FXML private ComboBox<String> comboCuenta;
    @FXML private ComboBox<Categoria> comboCategoria;
    @FXML private DatePicker pickerFecha;

    private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();

    // Variables para modo edición
    private Gasto gastoAEditar = null;
    private String cuentaOriginal = null;

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

        // Cargar categorias
        comboCategoria.setItems(controladorPrincipal.getCategorias());

        // Fecha por defecto
        pickerFecha.setValue(LocalDate.now());

        comboCuenta.getItems().add("Ninguna (Personal)");
        comboCuenta.getItems().addAll(controladorPrincipal.getCuentas().keySet());
    }

    public void cargarGastoEditar(Gasto g, String cuenta) {
        this.gastoAEditar = g;
        this.cuentaOriginal = cuenta;

        txtMonto.setText(String.valueOf(g.getCantidad()));
        txtDescripcion.setText(g.getDescripcion());
        pickerFecha.setValue(g.getFecha());
        comboCategoria.setValue(g.getCategoria());

        if (cuenta != null) {
            comboCuenta.setValue(cuenta);
        } else {
            comboCuenta.setValue("Ninguna (Personal)");
        }

        // Cambiar texto de los botones si hubiera un label o algo (opcional)
        // Por ahora lo controlaremos en guardarGasto
    }

    @FXML
    void guardarGasto(ActionEvent event) {
        try {
            // Validación de datos
            String montoStr = txtMonto.getText();
            String descripcion = txtDescripcion.getText();
            Usuario usuario = controladorPrincipal.getUsuarioActivo();
            String cuenta = comboCuenta.getValue();
            LocalDate fecha = pickerFecha.getValue();
            Categoria categoria = comboCategoria.getValue();

            if (descripcion == null || descripcion.isEmpty() || usuario == null || categoria == null) {
                mostrarError("Campos incompletos", "Por favor, rellena todos los campos.");
                return;
            }

            double monto = Double.parseDouble(montoStr);

            System.out.println("Guardando gasto de " + usuario.getNombre() + ": " + descripcion + " por un valor de " + monto + "EUR");

            if (gastoAEditar != null) {
                // Modo EDICION
                String cuentaFinal = (cuenta != null && !cuenta.equals("Ninguna (Personal)")) ? cuenta : null;
                try {
                    controladorPrincipal.modificarGasto(gastoAEditar, monto, fecha, descripcion, categoria, cuentaOriginal, cuentaFinal);
                } catch (IllegalArgumentException ex) {
                    mostrarError("Cuenta no válida", ex.getMessage());
                    return;
                }
                System.out.println("Gasto editado con éxito.");
            } else {
                // Modo CREACION
                if (cuenta != null && !cuenta.equals("Ninguna (Personal)")) {
                    // Comprobar que el usuario pertenece a la cuenta seleccionada
                    CuentaCompartida c = controladorPrincipal.getCuentas().get(cuenta);
                    if (c != null && !c.getUsuarios().contains(usuario)) {
                        mostrarError("Usuario inválido", "El usuario seleccionado no pertenece a la cuenta compartida '" + cuenta + "'.");
                        return;
                    }
                    // Creacion del gasto en cuenta compartida
                    controladorPrincipal.registrarGastoEnCuenta(monto, fecha, descripcion, categoria, usuario, cuenta);
                    System.out.println("Gasto registrado con éxito a través del ControladorPrincipal en la cuenta "
                            + cuenta + ".");
                } else {
                    // Creación de gasto personal
                    controladorPrincipal.registrarGastoPersonal(monto, fecha, descripcion, categoria, usuario);
                    System.out.println("Gasto personal registrado con éxito a través del ControladorPrincipal.");
                }
            }

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