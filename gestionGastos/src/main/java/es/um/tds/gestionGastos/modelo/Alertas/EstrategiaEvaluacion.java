package es.um.tds.gestionGastos.modelo.Alertas;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = EstrategiaMensual.class, name = "mensual"),
    @JsonSubTypes.Type(value = EstrategiaSemanal.class, name = "semanal")
})
public interface EstrategiaEvaluacion {
    double calcularGastoActual(List<Gasto> todosLosGastos, Categoria categoria);
}
