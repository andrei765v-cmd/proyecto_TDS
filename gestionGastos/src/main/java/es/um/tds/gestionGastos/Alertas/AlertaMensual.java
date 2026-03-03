package es.um.tds.gestionGastos.alertas;

import java.time.LocalDate;
import java.util.List;

import es.um.tds.gestionGastos.Gasto;

public class AlertaMensual extends Alerta {

    public AlertaMensual(double limite, Categoria categoria) {
        super(limite, categoria);
    }

    @Override
    public double calcularGastoActual(List<Gasto> todosLosGastos) {
        LocalDate inicioMes = LocalDate.now().withDayOfMonth(1);

        return todosLosGastos.stream()
            .filter(g -> !g.getFecha().isBefore(inicioMes)) // gastos desde dia 1
            .filter(g -> getCategoria() == null || g.getCategoria().equals(getCategoria()))
            .mapToDouble(Gasto::getCantidad)
            .sum();
    }
}