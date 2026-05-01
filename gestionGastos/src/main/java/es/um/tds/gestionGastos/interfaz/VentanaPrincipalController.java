package es.um.tds.gestionGastos.interfaz;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class VentanaPrincipalController {

	private ControladorPrincipal controladorPrincipal = ControladorPrincipal.getInstancia();
	
	private Map<String, Node> cacheVistas = new HashMap<>();

    @FXML private TabPane mainTabPane;
    @FXML private StackPane contentArea;
    @FXML private Tab tabMisGastos;
    @FXML private Tab tabDashboard;
    @FXML private Tab tabGastosCompartidos;
    @FXML private Tab tabAlertas;
    
    @FXML private ComboBox<Usuario> cbUsuarioActivo;
    @FXML private DatePicker dpDesde;
    @FXML private DatePicker dpHasta;
    @FXML private MenuButton btnMeses;
    @FXML private MenuButton btnCategorias;
    @FXML private Label lblTotalFiltrado;

    @FXML
    public void initialize() {
    	cargarTab(tabMisGastos);
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            cargarTab(newTab);
        });
        
        cargarFiltros();
        
        controladorPrincipal.getCategorias().addListener((ListChangeListener<Categoria>) change -> {
            btnCategorias.getItems().clear();
            for (Categoria cat : controladorPrincipal.getCategorias()) {
                CheckMenuItem item = new CheckMenuItem(cat.getNombre());
                item.setUserData(cat);
                btnCategorias.getItems().add(item);
            }
        });
        
        cbUsuarioActivo.setItems(controladorPrincipal.getUsuarios());
        cbUsuarioActivo.valueProperty().addListener((obs, oldVal, newVal) -> {
            controladorPrincipal.setUsuarioActivo(newVal);
            onAplicarFiltros(); // Refiltrar todo al cambiar de usuario
        });
        
        // Seleccionar primer usuario por defecto si hay
        if (!cbUsuarioActivo.getItems().isEmpty()) {
            cbUsuarioActivo.getSelectionModel().selectFirst();
        } else {
            actualizarTotalFiltrado(new ArrayList<>());
        }
    }

    private void cargarFiltros() {
        String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        for (int i = 0; i < meses.length; i++) {
            CheckMenuItem item = new CheckMenuItem(meses[i]);
            item.setUserData(i + 1);
            btnMeses.getItems().add(item);
        }
        
        for (Categoria cat : controladorPrincipal.getCategorias()) {
            CheckMenuItem item = new CheckMenuItem(cat.getNombre());
            item.setUserData(cat);
            btnCategorias.getItems().add(item);
        }
    }

    @FXML
    public void onAplicarFiltros() {
        Usuario activo = controladorPrincipal.getUsuarioActivo();
        if (activo == null) {
            controladorPrincipal.aplicarFiltros(g -> true);
            actualizarTotalFiltrado(new ArrayList<>());
            return;
        }

        LocalDate inicio = dpDesde.getValue();
        LocalDate fin = dpHasta.getValue();
        
        List<Integer> mesesSeleccionados = btnMeses.getItems().stream()
            .filter(item -> ((CheckMenuItem) item).isSelected())
            .map(item -> (Integer) item.getUserData())
            .collect(Collectors.toList());
            
        List<Categoria> categoriasSeleccionadas = btnCategorias.getItems().stream()
            .filter(item -> ((CheckMenuItem) item).isSelected())
            .map(item -> (Categoria) item.getUserData())
            .collect(Collectors.toList());
            
        java.util.function.Predicate<Gasto> predicate = g -> {
            boolean mesOk = (mesesSeleccionados.isEmpty() || mesesSeleccionados.contains(g.getFecha().getMonthValue()));
            boolean fechaOk = (inicio == null || !g.getFecha().isBefore(inicio)) && (fin == null || !g.getFecha().isAfter(fin));
            boolean catOk = (categoriasSeleccionadas.isEmpty() || categoriasSeleccionadas.contains(g.getCategoria()));
            return mesOk && fechaOk && catOk;
        };
        
        controladorPrincipal.aplicarFiltros(predicate);
        actualizarTotalFiltrado(new ArrayList<>(controladorPrincipal.getGastosObservable()));
    }

    private void actualizarTotalFiltrado(List<Gasto> gastos) {
        double total = gastos.stream().mapToDouble(Gasto::getCantidad).sum();
        lblTotalFiltrado.setText(String.format("€ %.2f", total));
    }

    private void cargarTab(Tab tab) {
        if (tab == tabMisGastos) {
            cargarVista("/es/um/tds/gestionGastos/VistaMisGastos.fxml");
        } else if (tab == tabDashboard) {
            cargarVista("/es/um/tds/gestionGastos/VistaDashboard.fxml");
        } else if (tab == tabGastosCompartidos) {
        	cargarVista("/es/um/tds/gestionGastos/VistaGastosCompartidos.fxml");
        } else if (tab == tabAlertas) {        	
        	cargarVista("/es/um/tds/gestionGastos/VistaAlertas.fxml");
        }
    }
    
    private void cargarVista(String fxml) {
        try {
            if (!cacheVistas.containsKey(fxml)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                Node vista = loader.load();
                cacheVistas.put(fxml, vista);
            }
            // Esto NO cambia el diseño de las pestañas arriba, solo cambia el centro
            contentArea.getChildren().setAll(cacheVistas.get(fxml)); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void onNuevoGasto() {
        abrirVentanaModal("/es/um/tds/gestionGastos/VistaAgregarGasto.fxml", "Añadir Gasto");
    }

    @FXML
    private void onNuevaAlerta() {
        abrirVentanaModal("/es/um/tds/gestionGastos/VistaAgregarAlerta.fxml", "Añadir Gasto");
    }

    @FXML
    private void onImportarCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar fichero CSV");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Ficheros CSV", "*.csv")
        );

        Stage stage = (Stage) mainTabPane.getScene().getWindow();
        File fichero = fileChooser.showOpenDialog(stage);

        if (fichero != null) {
            try {
                int importados = controladorPrincipal.importarGastosDesdeFichero(fichero);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Importación completada");
                alert.setHeaderText(null);
                alert.setContentText(importados + " gasto(s) importados correctamente desde " + fichero.getName());
                alert.showAndWait();
            } catch (Exception e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error al importar");
                error.setHeaderText("No se pudo importar el fichero");
                error.setContentText(e.getMessage());
                error.showAndWait();
            }
        }
    }

    private void abrirVentanaModal(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.initStyle(StageStyle.UNDECORATED); // Modales sin bordes
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}