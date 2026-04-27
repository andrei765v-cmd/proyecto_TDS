package es.um.tds.gestionGastos.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.um.tds.gestionGastos.modelo.Alertas.Alerta;
import es.um.tds.gestionGastos.modelo.Alertas.EstrategiaMensual;
import es.um.tds.gestionGastos.modelo.Alertas.EstrategiaSemanal;
import es.um.tds.gestionGastos.modelo.Alertas.Notificacion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RepositorioAlertas {
    private static RepositorioAlertas unicaInstancia;
    private final ObservableList<Alerta> alertas = FXCollections.observableArrayList();
    private final ObservableList<Notificacion> notificaciones = FXCollections.observableArrayList();

    private RepositorioAlertas() {}

    public static RepositorioAlertas getInstancia() {
        if (unicaInstancia == null) {
            unicaInstancia = new RepositorioAlertas();
        }
        return unicaInstancia;
    }

    public Alerta crearAlertaMensual(double limite, Categoria categoria, Usuario usuario) {
        Alerta a = new Alerta(limite, categoria, new EstrategiaMensual(), usuario);
        alertas.add(a);
        return a;
    }

    public Alerta crearAlertaSemanal(double limite, Categoria categoria, Usuario usuario) {
        Alerta a = new Alerta(limite, categoria, new EstrategiaSemanal(), usuario);
        alertas.add(a);
        return a;
    }

    public List<Alerta> getAlertas() {
        return Collections.unmodifiableList(alertas);
    }
    
    public ObservableList<Alerta> getAlertasObservable() {
        return alertas;
    }
    
    public List<Alerta> getAlertasDelUsuario(Usuario usuario) {
        List<Alerta> alertasUsuario = new ArrayList<>();
        for(Alerta a : alertas) {
            if(a.getUsuario() != null && a.getUsuario().equals(usuario)) {
                alertasUsuario.add(a);
            }
        }
        return alertasUsuario;
    }

    public void registrarNotificacion(Notificacion n) {
        notificaciones.add(n);
    }

    public ObservableList<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public List<Notificacion> getNotificacionesDelUsuario(Usuario usuario) {
        List<Notificacion> result = new ArrayList<>();
        for (Notificacion n : notificaciones) {
            if (n.getUsuario() != null && n.getUsuario().equals(usuario)) {
                result.add(n);
            }
        }
        return result;
    }

    // Experto en Información: Evaluar alertas
    public void evaluarAlertas() {
        for (Alerta alerta : alertas) {
            List<Gasto> gastosUsuario = RepositorioGastos.getInstancia().getGastosPersonales(alerta.getUsuario());
            int estado = alerta.comprobarYActualizarEstado(gastosUsuario);
            
            if (estado == 1) {
                String tipoStr = (alerta.getEstrategia() instanceof EstrategiaSemanal) ? "semanal" : "mensual";
                String titulo = "Límite " + tipoStr + " superado en " + (alerta.getCategoria() != null ? alerta.getCategoria().getNombre() : "General");
                Notificacion n = new Notificacion("Has gastado más de " + alerta.getLimite() + "€.", titulo, alerta.getUsuario());
                registrarNotificacion(n);
            } else if (estado == -1) {
                String tipoStr = (alerta.getEstrategia() instanceof EstrategiaSemanal) ? "semanal" : "mensual";
                String titulo = "Límite " + tipoStr + " estabilizado en " + (alerta.getCategoria() != null ? alerta.getCategoria().getNombre() : "General");
                Notificacion n = new Notificacion("Ya no superas el límite de " + alerta.getLimite() + "€.", titulo, alerta.getUsuario());
                registrarNotificacion(n);
            }
        }
    }
}
