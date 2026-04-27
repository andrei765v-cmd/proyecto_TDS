package es.um.tds.gestionGastos.modelo.Alertas;

import java.util.List;
import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;

public interface EstrategiaEvaluacion {
    double calcularGastoActual(List<Gasto> todosLosGastos, Categoria categoria);
}
