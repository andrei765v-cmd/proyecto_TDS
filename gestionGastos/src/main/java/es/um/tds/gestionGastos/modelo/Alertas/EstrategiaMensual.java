package es.um.tds.gestionGastos.modelo.Alertas;

import java.time.LocalDate;
import java.util.List;
import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;

public class EstrategiaMensual implements EstrategiaEvaluacion {
    @Override
    public double calcularGastoActual(List<Gasto> todosLosGastos, Categoria categoria) {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);

        return todosLosGastos.stream()
            .filter(g -> !g.getFecha().isBefore(inicioMes))
            .filter(g -> categoria == null || g.getCategoria().equals(categoria))
            .mapToDouble(Gasto::getCantidad)
            .sum();
    }
}
