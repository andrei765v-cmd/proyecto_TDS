package es.um.tds.gestionGastos.modelo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Alerta que verifica gastos mensuales.
 * Implementa el patrón Strategy.
 */
public class AlertaMensual extends Alerta {

    public AlertaMensual(String codigo, double limiteGasto, Categoria categoria) {
        super(codigo, limiteGasto, categoria);
    }

    @Override
    public boolean verificarAlerta(List<Gasto> gastos, LocalDate fechaActual) {
        if (!isActiva()) {
            return false;
        }

        // Obtener los gastos del mes actual
        List<Gasto> gastosMesActual = filtrarGastosMesActual(gastos, fechaActual);
        
        // Calcular el total
        double totalMensual = calcularTotalGastos(gastosMesActual);
        
        // Verificar si se superó el límite
        return totalMensual > getLimiteGasto();
    }

    /**
     * Filtra los gastos que pertenecen al mes actual
     */
    private List<Gasto> filtrarGastosMesActual(List<Gasto> gastos, LocalDate fechaActual) {
        int mesActual = fechaActual.getMonthValue();
        int añoActual = fechaActual.getYear();

        return gastos.stream()
            .filter(g -> {
                LocalDate fechaGasto = g.getFecha();
                return fechaGasto.getMonthValue() == mesActual && 
                       fechaGasto.getYear() == añoActual;
            })
            .collect(Collectors.toList());
    }

    @Override
    public String getTipoAlerta() {
        return "Mensual";
    }
}