package es.um.tds.gestionGastos.Controladores;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Alertas.Alerta;
import es.um.tds.gestionGastos.modelo.Alertas.AlertaMensual;
import es.um.tds.gestionGastos.modelo.Alertas.AlertaSemanal;
import es.um.tds.gestionGastos.modelo.Alertas.Notificacion;

public class ControladorAlerta {
    private final List<Alerta> alertas = new ArrayList<>();
    private final List<Notificacion> notificaciones = new ArrayList<>();

    public Alerta crearMensual(double limite, Categoria categoria) {
        Alerta a = new AlertaMensual(limite, categoria);
        alertas.add(a);
        return a;
    }

    public Alerta crearSemanal(double limite, Categoria categoria) {
        Alerta a = new AlertaSemanal(limite, categoria);
        alertas.add(a);
        return a;
    }

    public List<Alerta> getAlertas() {
        return Collections.unmodifiableList(alertas);
    }

    public void registrarNotificacion(Notificacion n) {
        notificaciones.add(n);
    }

    public List<Notificacion> getNotificaciones() {
        return Collections.unmodifiableList(notificaciones);
    }
}
