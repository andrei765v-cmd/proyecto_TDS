package es.um.tds.gestionGastos.interfaz;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaCompartida;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CuentaCompartidaController {

	private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();

	@FXML private ComboBox<String> comboCuentas;
	@FXML private HBox balancesContainer; // El contenedor de las tarjetas 

    // Tabla de Gastos de la cuenta
    @FXML private TableView<Gasto> tablaGastosCuenta;
    @FXML private TableColumn<Gasto, String> colFecha;
    @FXML private TableColumn<Gasto, String> colDescripcion;
    @FXML private TableColumn<Gasto, String> colCategoria;
    @FXML private TableColumn<Gasto, String> colPagador;
    @FXML private TableColumn<Gasto, Double> colMonto;

    @FXML
    public void initialize() {
        // Cargar los nombres de las cuentas compartidas en la lista
    	colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPagador.setCellValueFactory(new PropertyValueFactory<>("usuario")); // Muestra el nombre del pagador
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        // Cargar cuentas del usuario activo inicialmente
        cargarCuentasUsuarioActivo();

        comboCuentas.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                actualizarVista(newVal);
            } else {
                balancesContainer.getChildren().clear();
                tablaGastosCuenta.getItems().clear();
            }
        });

        // Escuchar cambios de sesión
        controladorPrincipal.usuarioActivoProperty().addListener((obs, oldVal, newVal) -> {
            cargarCuentasUsuarioActivo();
        });

        // Escuchar adiciones de gastos
        controladorPrincipal.getGastosObservable().addListener((ListChangeListener<Gasto>) change -> {
            if (comboCuentas.getValue() != null) {
                actualizarVista(comboCuentas.getValue());
            }
        });

        configurarMenuContextual();
    }

    private void configurarMenuContextual() {
        ContextMenu contextMenu = new ContextMenu();
        
        MenuItem editarItem = new MenuItem("Editar Gasto");
        editarItem.setOnAction(e -> {
            Gasto seleccionado = tablaGastosCuenta.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                Usuario activo = controladorPrincipal.getUsuarioActivo();
                if (!seleccionado.getUsuario().equals(activo)) {
                    new Alert(Alert.AlertType.WARNING, "Solo puedes editar tus propios gastos.").showAndWait();
                    return;
                }
                abrirModalEdicion(seleccionado);
            }
        });
        
        MenuItem eliminarItem = new MenuItem("Eliminar Gasto");
        eliminarItem.setOnAction(e -> {
            Gasto seleccionado = tablaGastosCuenta.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Estás seguro de que quieres eliminar este gasto compartido?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.YES) {
                        controladorPrincipal.eliminarGasto(seleccionado, comboCuentas.getValue());
                    }
                });
            }
        });
        
        contextMenu.getItems().addAll(editarItem, eliminarItem);
        tablaGastosCuenta.setContextMenu(contextMenu);
    }

    private void abrirModalEdicion(Gasto gasto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/um/tds/gestionGastos/VistaAgregarGasto.fxml"));
            Parent root = loader.load();
            
            AgregarGastoController controller = loader.getController();
            controller.cargarGastoEditar(gasto, comboCuentas.getValue());
            
            Stage stage = new Stage();
            stage.setTitle("Editar Gasto Compartido");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarCuentasUsuarioActivo() {
        Usuario activo = controladorPrincipal.getUsuarioActivo();
        comboCuentas.getItems().clear();
        if (activo != null) {
            List<String> cuentasActivo = controladorPrincipal.getCuentas().values().stream()
                .filter(c -> c.getUsuarios().contains(activo))
                .map(CuentaCompartida::getNombre)
                .collect(Collectors.toList());
            comboCuentas.getItems().addAll(cuentasActivo);
            if (!cuentasActivo.isEmpty()) {
                comboCuentas.getSelectionModel().selectFirst();
            }
        }
    }

    private void actualizarVista(String nombreCuenta) {
        CuentaCompartida cuenta = controladorPrincipal.getCuentas().get(nombreCuenta);
        if (cuenta == null) return;

        // Tabla de Gastos
        List<Gasto> gastosFiltrados = controladorPrincipal.getGastosFiltradosDeCuenta(nombreCuenta);
        ObservableList<Gasto> gastos = FXCollections.observableArrayList(gastosFiltrados);
        SortedList<Gasto> sortedGastos = new SortedList<>(gastos);
        sortedGastos.comparatorProperty().bind(tablaGastosCuenta.comparatorProperty());
        tablaGastosCuenta.setItems(sortedGastos);

        // Tarjetas de Saldos
        balancesContainer.getChildren().clear(); // Limpiar tarjetas anteriores
        Map<Usuario, Double> saldos = controladorPrincipal.obtenerSaldosDeCuenta(nombreCuenta);

        for (Map.Entry<Usuario, Double> entry : saldos.entrySet()) {
            VBox card = crearTarjetaSaldo(entry.getKey().getNombre(), entry.getValue());
            balancesContainer.getChildren().add(card);
        }
    }
    
    private VBox crearTarjetaSaldo(String nombre, Double saldo) {
        VBox card = new VBox();
        card.getStyleClass().add("balance-card"); // Usar el estilo CSS

        Label lblNombre = new Label(nombre);
        lblNombre.getStyleClass().add("balance-name");

        String textoSaldo = (saldo >= 0 ? "+" : "") + String.format("%.2f EUR", saldo);
        Label lblSaldo = new Label(textoSaldo);
        lblSaldo.getStyleClass().add("balance-amount-prominent");

        // Color dinámico según el saldo
        if (saldo >= 0) lblSaldo.setStyle("-fx-text-fill: #27ae60;"); // Verde si le deben
        else lblSaldo.setStyle("-fx-text-fill: #e74c3c;"); // Rojo si debe

        card.getChildren().addAll(lblNombre, lblSaldo);
        return card;
    }

    @FXML
    private void onNuevoUsuario() {
        abrirVentanaModal("/es/um/tds/gestionGastos/VistaAgregarUsuario.fxml", "Nuevo Usuario");
    }

    @FXML
    private void onNuevaCuenta() {
        abrirVentanaModal("/es/um/tds/gestionGastos/VistaAgregarCuenta.fxml", "Nueva Cuenta");
        cargarCuentasUsuarioActivo();
    }
    
    @FXML
    private void onNuevaCategoria() {
        abrirVentanaModal("/es/um/tds/gestionGastos/VistaAgregarCategoria.fxml", "Nueva Categoría");
    }

    @FXML
    private void onConfigurarCuenta() {
        String nombreCuenta = comboCuentas.getValue();
        if (nombreCuenta == null) return;
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/es/um/tds/gestionGastos/VistaConfigurarCuenta.fxml"));
            Parent root = loader.load();
            
            ConfigurarCuentaController controller = loader.getController();
            controller.setCuenta(nombreCuenta);
            
            Stage stage = new Stage();
            stage.setTitle("Configurar Cuenta");
            stage.initStyle(javafx.stage.StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            
            // Refrescar combo y vista
            cargarCuentasUsuarioActivo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirVentanaModal(String fxml, String titulo) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(fxml));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle(titulo);
            stage.initStyle(javafx.stage.StageStyle.UNDECORATED); // Sin bordes de windows
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static class SaldoFila {
        private final String usuario;
        private final Double saldo;

        public SaldoFila(String usuario, Double saldo) {
            this.usuario = usuario;
            this.saldo = saldo;
        }
        public String getUsuario() { return usuario; }
        public Double getSaldo() { return saldo; }
    }
}