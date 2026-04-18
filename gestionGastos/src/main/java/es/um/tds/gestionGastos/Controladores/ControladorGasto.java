package es.um.tds.gestionGastos.Controladores;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.um.tds.gestionGastos.modelo.Categoria;
import es.um.tds.gestionGastos.modelo.Gasto;

public class ControladorGasto {
    private final List<Gasto> gastos = new ArrayList<>();

    public void registrar(double cantidad, LocalDate fecha, String descripcion, Categoria categoria) {
        gastos.add(new Gasto(cantidad, fecha, descripcion, categoria));
    }

    public List<Gasto> getGastos() {
        return Collections.unmodifiableList(gastos);
    }
}
