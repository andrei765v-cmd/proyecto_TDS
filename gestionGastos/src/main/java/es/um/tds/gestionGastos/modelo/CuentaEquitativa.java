package es.um.tds.gestionGastos.modelo;

import java.util.List;

/**
 * Cuenta compartida con distribución equitativa.
 * Todos los miembros pagan la misma parte del gasto.
 */
public class CuentaEquitativa extends CuentaCompartida {

    public CuentaEquitativa(String codigo, String nombre, List<String> miembros) {
        super(codigo, nombre, miembros);
    }

    @Override
    public void registrarGasto(String pagador, double importe) {
        // Verificar que el pagador es miembro de la cuenta
        if (!getMiembros().contains(pagador)) {
            throw new IllegalArgumentException("El pagador no es miembro de esta cuenta");
        }

        int numMiembros = getMiembros().size();
        double parteIndividual = importe / numMiembros;

        // El pagador recibe el importe total menos su parte
        actualizarSaldo(pagador, importe - parteIndividual);

        // Los demás miembros deben su parte
        for (String miembro : getMiembros()) {
            if (!miembro.equals(pagador)) {
                actualizarSaldo(miembro, -parteIndividual);
            }
        }
    }
}