package es.um.tds.gestionGastos.interfaz;

import java.util.List;

import es.um.tds.gestionGastos.Controladores.ControladorPrincipal;
import es.um.tds.gestionGastos.modelo.Alertas.Alerta;
import es.um.tds.gestionGastos.modelo.Alertas.Notificacion;
import es.um.tds.gestionGastos.modelo.RepositorioAlertas;
import es.um.tds.gestionGastos.modelo.Usuario;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class AlertasController {

    @FXML private Label lblConteoAlertas;
    @FXML private VBox vboxAlertas;
    @FXML private VBox vboxAlertasConfiguradas;
    
    @FXML
    public void initialize() {
        RepositorioAlertas.getInstancia().getNotificaciones().addListener((ListChangeListener<Notificacion>) change -> {
            cargarAlertas();
        });
        RepositorioAlertas.getInstancia().getAlertasObservable().addListener((ListChangeListener<Alerta>) change -> {
            cargarAlertas();
        });
        ControladorPrincipal.getInstancia().usuarioActivoProperty().addListener((obs, oldVal, newVal) -> {
            cargarAlertas();
        });
        cargarAlertas();
    }

    private void cargarAlertas() {
        Usuario activo = ControladorPrincipal.getInstancia().getUsuarioActivo();
        if (activo != null) {
            List<Alerta> configuradas = RepositorioAlertas.getInstancia().getAlertasDelUsuario(activo);
            vboxAlertasConfiguradas.getChildren().clear();
            for (Alerta a : configuradas) {
                HBox card = new HBox();
                card.getStyleClass().add("alert-config-card");
                card.setPrefWidth(Double.MAX_VALUE);
                
                Label lbl = new Label("Límite de " + a.getLimite() + "€ en " + (a.getCategoria() != null ? a.getCategoria().getNombre() : "General"));
                lbl.getStyleClass().add("alert-config-text");
                
                card.getChildren().add(lbl);
                vboxAlertasConfiguradas.getChildren().add(card);
            }
            if (configuradas.isEmpty()) {
                Label lblVacio = new Label("No has configurado ninguna alerta.");
                lblVacio.setStyle("-fx-text-fill: #b2b2b2;");
                vboxAlertasConfiguradas.getChildren().add(lblVacio);
            }
        }

        List<Notificacion> notificaciones = RepositorioAlertas.getInstancia().getNotificacionesDelUsuario(activo);

        lblConteoAlertas.setText(String.valueOf(notificaciones.size()));
        vboxAlertas.getChildren().clear();

        // Mostrar en orden inverso: las más recientes primero
        for (int i = notificaciones.size() - 1; i >= 0; i--) {
            vboxAlertas.getChildren().add(crearTarjetaAlerta(notificaciones.get(i)));
        }
        
        if (notificaciones.isEmpty()) {
            Label lblVacio = new Label("No hay notificaciones todavía.");
            lblVacio.setStyle("-fx-text-fill: #b2b2b2;");
            vboxAlertas.getChildren().add(lblVacio);
        }
    }

    private HBox crearTarjetaAlerta(Notificacion n) {
        HBox hbox = new HBox(15.0);
        boolean esEstabilizado = n.getTitulo().contains("estabilizado");
        
        hbox.getStyleClass().addAll("alert-card", esEstabilizado ? "alert-success" : "alert-warning");
        
        Circle circle = new Circle(6.0, esEstabilizado ? Color.web("#2ecc71") : Color.web("#ffcc00"));
        
        VBox vboxText = new VBox(5.0);
        HBox.setHgrow(vboxText, Priority.ALWAYS);
        
        Label lblHeader = new Label(n.getTitulo());
        lblHeader.getStyleClass().add("alert-header");
        
        Label lblBody = new Label(n.getMensaje());
        lblBody.getStyleClass().add("alert-body");
        lblBody.setWrapText(true);
        
        vboxText.getChildren().addAll(lblHeader, lblBody);
        
        hbox.getChildren().addAll(circle, vboxText);
        return hbox;
    }
}
