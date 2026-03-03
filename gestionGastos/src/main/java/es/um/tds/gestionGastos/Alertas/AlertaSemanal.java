package es.um.tds.gestionGastos.alertas;

import java.time.LocalDate;
import java.util.List;

import es.um.tds.gestionGastos.Gasto;

public class AlertaSemanal extends Alerta {
    
    public AlertaSemanal(double limite, Categoria categoria) {
        super(limite, categoria);
    }

    @Override
    public double calcularGastoActual(List<Gasto> todosLosGastos) {
        LocalDate haceUnaSemana = LocalDate.now().minusWeeks(1);
        
        return todosLosGastos.stream()
            .filter(g -> g.getFecha().isAfter(haceUnaSemana)) // gastos última semana
            .filter(g -> getCategoria() == null || g.getCategoria().equals(getCategoria())) // filtrar por categoría si la hay
            .mapToDouble(Gasto::getCantidad)
				.sum();
	}
}