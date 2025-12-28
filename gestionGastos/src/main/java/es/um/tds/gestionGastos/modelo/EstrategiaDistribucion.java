package es.um.tds.gestionGastos.modelo;

import java.util.List;
import java.util.Map;

/**
 * Interfaz que define la estrategia de distribución de gastos en una cuenta compartida.
 * Patrón Strategy - permite diferentes algoritmos de distribución.
 */
public interface EstrategiaDistribucion {
    /**
     * Calcula la distribución de un gasto entre los miembros según la estrategia.
     * 
     * @param pagador Usuario que pagó el gasto
     * @param importe Cantidad pagada
     * @param miembros Lista de todos los miembros de la cuenta
     * @return Mapa con la cantidad que cada miembro debe pagar/recibir
     */
    Map<Usuario, Double> calcular(Usuario pagador, double importe, List<Usuario> miembros);
}