package es.um.tds.gestionGastos.modelo.Importador;

import java.io.File;
import java.util.List;

import es.um.tds.gestionGastos.modelo.Gasto;
import es.um.tds.gestionGastos.modelo.Usuario;

/**
 * Interfaz Importador (Patrón Adaptador).
 * Define el contrato que toda fuente de datos externa debe cumplir.
 */
public interface Importador {
    /**
     * Importa una lista de gastos desde un fichero.
     * @param fichero El fichero a importar.
     * @param usuarioActivo El usuario al que se asignarán los gastos.
     * @return Lista de gastos importados.
     * @throws Exception Si el fichero tiene un formato incorrecto o no puede leerse.
     */
    List<Gasto> importarGastos(File fichero, Usuario usuarioActivo) throws Exception;
}
