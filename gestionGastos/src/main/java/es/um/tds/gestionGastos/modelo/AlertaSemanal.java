package es.um.tds.gestionGastos.modelo;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Alerta que verifica gastos semanales.
 * Implementa el patrón Strategy.
 */
public class AlertaSemanal extends Alerta {

    public AlertaSemanal(String codigo, double limiteGasto, Categoria categoria) {
        super(codigo, limiteGasto, categoria);
    }

    @Override
    public boolean verificarAlerta(List<Gasto> gastos, LocalDate fechaActual) {
        if (!isActiva()) {
            return false;
        }

        // Obtener los gastos de la semana actual
        List<Gasto> gastosSemanaActual = filtrarGastosSemanaActual(gastos, fechaActual);
        
        // Calcular el total
        double totalSemanal = calcularTotalGastos(gastosSemanaActual);
        
        // Verificar si se superó el límite
        return totalSemanal > getLimiteGasto();
    }

    /**
     * Filtra los gastos que pertenecen a la semana actual
     */
    private List<Gasto> filtrarGastosSemanaActual(List<Gasto> gastos, LocalDate fechaActual) {
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int semanaActual = fechaActual.get(weekFields.weekOfWeekBasedYear());
        int añoActual = fechaActual.get(weekFields.weekBasedYear());

        return gastos.stream()
            .filter(g -> {
                LocalDate fechaGasto = g.getFecha();
                int semanaGasto = fechaGasto.get(weekFields.weekOfWeekBasedYear());
                int añoGasto = fechaGasto.get(weekFields.weekBasedYear());
                return semanaGasto == semanaActual && añoGasto == añoActual;
            })
            .collect(Collectors.toList());
    }

    @Override
    public String getTipoAlerta() {
        return "Semanal";
    }
}