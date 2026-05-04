package es.um.tds.gestionGastos.modelo.CuentaCompartida;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import es.um.tds.gestionGastos.modelo.Usuario;

public class CuentaPorcentual extends CuentaCompartida {
    private Map<String, Double> porcentajes;

    public CuentaPorcentual() {
        super();
        this.porcentajes = new HashMap<>();
    }

    @JsonCreator
    public CuentaPorcentual(
            @JsonProperty("nombre") String nombre,
            @JsonProperty("participantes") List<Usuario> participantes,
            @JsonProperty("porcentajes") Map<String, Double> porcentajesJson) {
        super(nombre, participantes != null ? participantes : new ArrayList<>());
        this.porcentajes = porcentajesJson != null ? new HashMap<>(porcentajesJson) : new HashMap<>();
    }

    public static CuentaPorcentual crear(String nombre, List<Usuario> participantes, Map<Usuario, Double> porcentajes) {
        CuentaPorcentual c = new CuentaPorcentual(nombre, participantes, new HashMap<>());
        c.setPorcentajes(porcentajes);
        return c;
    }

    private void validarYAsignar(Map<Usuario, Double> porcentajes) {
        double suma = porcentajes.values().stream().mapToDouble(Double::doubleValue).sum();
        if ((suma - 100) != 0)
            throw new IllegalArgumentException("Suma != 100%");
        this.porcentajes = porcentajes.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getNombre(), Map.Entry::getValue));
    }

    @JsonIgnore
    public void setPorcentajes(Map<Usuario, Double> porcentajes) {
        validarYAsignar(porcentajes);
    }

    @JsonIgnore
    public Map<Usuario, Double> getPorcentajes() {
        return participantes.stream()
                .filter(u -> porcentajes.containsKey(u.getNombre()))
                .collect(Collectors.toMap(u -> u, u -> porcentajes.get(u.getNombre())));
    }

    @JsonProperty("porcentajes")
    public Map<String, Double> getPorcentajesPorNombre() {
        return porcentajes;
    }

    @Override
    public Map<Usuario, Double> calcularSaldos() {
        double total = totalDeLaCuenta();
        return participantes.stream().collect(Collectors.toMap(u -> u,
                u -> totalPagadoPor(u) - (total * (porcentajes.getOrDefault(u.getNombre(), 0.0) / 100.0))));
    }
}
