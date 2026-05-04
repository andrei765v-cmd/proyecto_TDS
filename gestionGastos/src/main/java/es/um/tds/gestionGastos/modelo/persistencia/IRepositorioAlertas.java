package es.um.tds.gestionGastos.modelo.persistencia;

import java.util.List;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Usuario;
import es.um.tds.gestionGastos.modelo.Alertas.Alerta;
import es.um.tds.gestionGastos.modelo.Alertas.Notificacion;
import javafx.collections.ObservableList;

public interface IRepositorioAlertas {
    Alerta crearAlertaMensual(double limite, Categoria categoria, Usuario usuario);
    Alerta crearAlertaSemanal(double limite, Categoria categoria, Usuario usuario);
    List<Alerta> getAlertas();
    ObservableList<Alerta> getAlertasObservable();
    List<Alerta> getAlertasDelUsuario(Usuario usuario);
    void registrarNotificacion(Notificacion n);
    ObservableList<Notificacion> getNotificaciones();
    List<Notificacion> getNotificacionesDelUsuario(Usuario usuario);
    void evaluarAlertas();
    void cargar();
    void guardar();
}
