package es.um.tds.gestionGastos.interfaz;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaCompartida;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CuentaCompartidaController {

    @FXML private ListView<String> listaCuentas;
    @FXML private TableView<Object> tablaSaldos; // Ajustar según tu modelo de datos

    private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();

    @FXML
    public void initialize() {
        // Cargar los nombres de las cuentas compartidas en la lista
        actualizarListaCuentas();
        
        // Al seleccionar una cuenta, podríamos mostrar sus saldos
        listaCuentas.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                mostrarSaldos(newVal);
            }
        });
    }

    private void actualizarListaCuentas() {
        listaCuentas.getItems().setAll(controladorPrincipal.getCuentas().keySet());
    }

    private void mostrarSaldos(String nombreCuenta) {
        var saldos = controladorPrincipal.obtenerSaldosDeCuenta(nombreCuenta);
        // Lógica para pintar saldos en la TableView
    }

    @FXML
    private void abrirModalNuevaCuenta() {
        // Aquí llamarías a abrir el modal para crear cuenta (equitativa o porcentual)
    }
}