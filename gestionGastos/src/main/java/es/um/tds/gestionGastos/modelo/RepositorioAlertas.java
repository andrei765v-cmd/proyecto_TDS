package es.um.tds.gestionGastos.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import es.um.tds.gestionGastos.modelo.Alertas.Alerta;
import es.um.tds.gestionGastos.modelo.Alertas.EstrategiaMensual;
import es.um.tds.gestionGastos.modelo.Alertas.EstrategiaSemanal;
import es.um.tds.gestionGastos.modelo.Alertas.Notificacion;
import es.um.tds.gestionGastos.modelo.persistencia.IRepositorioAlertas;
import es.um.tds.gestionGastos.modelo.persistencia.JsonStore;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RepositorioAlertas implements IRepositorioAlertas {
    private static final String FICHERO_ALERTAS = "alertas.json";
    private static final String FICHERO_NOTIS = "notificaciones.json";
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

    @Override
    public Alerta crearAlertaMensual(double limite, Categoria categoria, Usuario usuario) {
        Alerta a = new Alerta(limite, categoria, new EstrategiaMensual(), usuario);
        alertas.add(a);
        return a;
    }

    @Override
    public Alerta crearAlertaSemanal(double limite, Categoria categoria, Usuario usuario) {
        Alerta a = new Alerta(limite, categoria, new EstrategiaSemanal(), usuario);
        alertas.add(a);
        return a;
    }

    @Override
    public List<Alerta> getAlertas() {
        return Collections.unmodifiableList(alertas);
    }

    @Override
    public ObservableList<Alerta> getAlertasObservable() {
        return alertas;
    }

    @Override
    public List<Alerta> getAlertasDelUsuario(Usuario usuario) {
        List<Alerta> alertasUsuario = new ArrayList<>();
        for (Alerta a : alertas) {
            if (a.getUsuario() != null && a.getUsuario().equals(usuario)) {
                alertasUsuario.add(a);
            }
        }
        return alertasUsuario;
    }

    @Override
    public void registrarNotificacion(Notificacion n) {
        notificaciones.add(n);
    }

    @Override
    public ObservableList<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    @Override
    public List<Notificacion> getNotificacionesDelUsuario(Usuario usuario) {
        List<Notificacion> result = new ArrayList<>();
        for (Notificacion n : notificaciones) {
            if (n.getUsuario() != null && n.getUsuario().equals(usuario)) {
                result.add(n);
            }
        }
        return result;
    }

    @Override
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

    @Override
    public void cargar() {
        List<Alerta> a = JsonStore.leer(FICHERO_ALERTAS, new TypeReference<List<Alerta>>() {}, List.of());
        alertas.setAll(a);
        List<Notificacion> n = JsonStore.leer(FICHERO_NOTIS, new TypeReference<List<Notificacion>>() {}, List.of());
        notificaciones.setAll(n);
    }

    @Override
    public void guardar() {
        JsonStore.escribir(FICHERO_ALERTAS, alertas);
        JsonStore.escribir(FICHERO_NOTIS, notificaciones);
    }
}
