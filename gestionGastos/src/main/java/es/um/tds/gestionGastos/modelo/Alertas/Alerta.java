package es.um.tds.gestionGastos.modelo.Alertas;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;

public class Alerta {
    private double limite;
    private Categoria categoria;
    private EstrategiaEvaluacion estrategia;
    private Usuario usuario;
    private boolean estadoSuperada = false;

    public Alerta() {}

    @JsonCreator
    public Alerta(
            @JsonProperty("limite") double limite,
            @JsonProperty("categoria") Categoria categoria,
            @JsonProperty("estrategia") EstrategiaEvaluacion estrategia,
            @JsonProperty("usuario") Usuario usuario) {
        this.limite = limite;
        this.categoria = categoria;
        this.estrategia = estrategia;
        this.usuario = usuario;
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public EstrategiaEvaluacion getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(EstrategiaEvaluacion estrategia) {
        this.estrategia = estrategia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isEstadoSuperada() {
        return estadoSuperada;
    }

    public void setEstadoSuperada(boolean estadoSuperada) {
        this.estadoSuperada = estadoSuperada;
    }

    public double calcularGastoActual(List<Gasto> todosLosGastos) {
        return estrategia.calcularGastoActual(todosLosGastos, categoria);
    }

    public boolean esSuperada(List<Gasto> gastos) {
        return calcularGastoActual(gastos) > limite;
    }

    public int comprobarYActualizarEstado(List<Gasto> gastos) {
        boolean actualmenteSuperada = esSuperada(gastos);
        if (actualmenteSuperada && !estadoSuperada) {
            estadoSuperada = true;
            return 1;
        } else if (!actualmenteSuperada && estadoSuperada) {
            estadoSuperada = false;
            return -1;
        }
        return 0;
    }
}
