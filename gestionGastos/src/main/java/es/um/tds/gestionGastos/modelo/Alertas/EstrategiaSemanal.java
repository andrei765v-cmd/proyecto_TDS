package es.um.tds.gestionGastos.modelo.Alertas;

import java.time.LocalDate;
import java.util.List;
import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;

public class EstrategiaSemanal implements EstrategiaEvaluacion {
    @Override
    public double calcularGastoActual(List<Gasto> todosLosGastos, Categoria categoria) {
        LocalDate haceUnaSemana = LocalDate.now().minusWeeks(1);

        return todosLosGastos.stream()
            .filter(g -> g.getFecha().isAfter(haceUnaSemana))
            .filter(g -> categoria == null || g.getCategoria().equals(categoria))
            .mapToDouble(Gasto::getCantidad)
            .sum();
    }
}
