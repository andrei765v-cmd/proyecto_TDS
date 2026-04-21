package es.um.tds.gestionGastos.interfaz;

import java.util.Map;
import java.util.stream.Collectors;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;
import es.um.tds.gestionGastos.modelo.CuentaCompartida.CuentaCompartida;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CuentaCompartidaController {

	private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();

	@FXML private ComboBox<String> comboCuentas;
	@FXML private HBox balancesContainer; // El contenedor de las tarjetas 

 // Tabla de abajo: Gastos de la cuenta
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
        
        // Cargar cuentas
        comboCuentas.getItems().addAll(controladorPrincipal.getCuentas().keySet());
        
        comboCuentas.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                actualizarVista(newVal);
            }
        });
    }
    
    private void actualizarVista(String nombreCuenta) {
        CuentaCompartida cuenta = controladorPrincipal.getCuentas().get(nombreCuenta);
        if (cuenta == null) return;

        // --- PARTE INFERIOR: TABLA DE GASTOS ---
        ObservableList<Gasto> gastos = FXCollections.observableArrayList(cuenta.getGastos());
        SortedList<Gasto> sortedGastos = new SortedList<>(gastos);
        sortedGastos.comparatorProperty().bind(tablaGastosCuenta.comparatorProperty());
        tablaGastosCuenta.setItems(sortedGastos);

        // --- PARTE SUPERIOR: TARJETAS DE SALDOS ---
        balancesContainer.getChildren().clear(); // Limpiar tarjetas anteriores
        Map<Usuario, Double> saldos = controladorPrincipal.obtenerSaldosDeCuenta(nombreCuenta);

        for (Map.Entry<Usuario, Double> entry : saldos.entrySet()) {
            VBox card = crearTarjetaSaldo(entry.getKey().getNombre(), entry.getValue());
            balancesContainer.getChildren().add(card);
        }
    }
    
    private VBox crearTarjetaSaldo(String nombre, Double saldo) {
        VBox card = new VBox();
        card.getStyleClass().add("balance-card"); // Usa tu estilo CSS 

        Label lblNombre = new Label(nombre);
        lblNombre.getStyleClass().add("balance-name");

        String textoSaldo = (saldo >= 0 ? "+" : "") + String.format("%.2f €", saldo);
        Label lblSaldo = new Label(textoSaldo);
        
        // Color dinámico según el saldo
        if (saldo >= 0) lblSaldo.setStyle("-fx-text-fill: #27ae60;"); // Verde si le deben
        else lblSaldo.setStyle("-fx-text-fill: #e74c3c;"); // Rojo si debe

        card.getChildren().addAll(lblNombre, lblSaldo);
        return card;
    }
    
    private void actualizarTarjetasSaldos(Map<Usuario, Double> saldos) {
        // Limpiamos las tarjetas de "prueba" que haya en el FXML
        balancesContainer.getChildren().clear();

        for (Map.Entry<Usuario, Double> entrada : saldos.entrySet()) {
            Usuario u = entrada.getKey();
            Double saldo = entrada.getValue();

            // Creamos el VBox de la tarjeta
            VBox card = new VBox();
            card.getStyleClass().add("balance-card");

            // Label del nombre
            Label lblNombre = new Label(u.getNombre());
            lblNombre.getStyleClass().add("balance-name");

            // Label del monto
            String signo = saldo >= 0 ? "+ " : "";
            Label lblMonto = new Label(signo + String.format("%.2f €", saldo));
            lblMonto.getStyleClass().add("balance-amount");

            // Aplicar color según si debe o le deben 
            if (saldo >= 0) {
                lblMonto.getStyleClass().add("text-positive");
            } else {
                lblMonto.getStyleClass().add("text-negative");
            }

            card.getChildren().addAll(lblNombre, lblMonto);
            balancesContainer.getChildren().add(card);
        }
    }

    @FXML
    private void abrirModalNuevaCuenta() {
        // Aquí llamarías a abrir el modal para crear cuenta (equitativa o porcentual)
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