package es.um.tds.gestionGastos.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase que representa una cuenta de gasto compartida entre varios usuarios.
 * Utiliza el patrón Strategy para diferentes tipos de distribución.
 * El código se genera automáticamente con formato CC + 3 dígitos (CC001, CC002, etc.)
 */
public class CuentaCompartida {
    private static final AtomicInteger contadorCuentas = new AtomicInteger(0);
    
    private String codigo;
    private String nombre;
    private List<Usuario> miembros;
    private Map<Usuario, Double> saldos; // Saldo de cada miembro (+ debe cobrar, - debe pagar)
    private List<Gasto> gastos; // Gastos asociados a esta cuenta
    private EstrategiaDistribucion estrategiaDistribucion;

    /**
     * Constructor con código autogenerado
     */
    public CuentaCompartida(String nombre, List<Usuario> miembros, EstrategiaDistribucion estrategia) {
        this.codigo = generarCodigo();
        this.nombre = nombre;
        this.miembros = new ArrayList<>(miembros);
        this.saldos = new HashMap<>();
        this.gastos = new ArrayList<>();
        this.estrategiaDistribucion = estrategia;
        
        inicializarSaldos();
        vincularConMiembros();
    }

    /**
     * Constructor con código específico
     */
    public CuentaCompartida(String codigo, String nombre, List<Usuario> miembros, 
                           EstrategiaDistribucion estrategia) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.miembros = new ArrayList<>(miembros);
        this.saldos = new HashMap<>();
        this.gastos = new ArrayList<>();
        this.estrategiaDistribucion = estrategia;
        
        actualizarContador(codigo);
        inicializarSaldos();
        vincularConMiembros();
    }

    /**
     * Genera un código único en formato CC001, CC002, etc.
     */
    private static String generarCodigo() {
        int numero = contadorCuentas.incrementAndGet();
        return String.format("CC%03d", numero);
    }

    /**
     * Actualiza el contador basándose en un código existente
     */
    private static void actualizarContador(String codigo) {
        if (codigo != null && codigo.startsWith("CC")) {
            try {
                int numero = Integer.parseInt(codigo.substring(2));
                contadorCuentas.updateAndGet(current -> Math.max(current, numero));
            } catch (NumberFormatException e) {
                // Código inválido, ignorar
            }
        }
    }

    /**
     * Reinicia el contador
     */
    public static void reiniciarContador() {
        contadorCuentas.set(0);
    }

    /**
     * Inicializa todos los saldos a 0
     */
    private void inicializarSaldos() {
        for (Usuario miembro : miembros) {
            saldos.put(miembro, 0.0);
        }
    }

    /**
     * Vincula esta cuenta con cada miembro
     */
    private void vincularConMiembros() {
        for (Usuario miembro : miembros) {
            miembro.addCuentaCompartida(this);
        }
    }

    // Getters
    public String getCodigo() {
        return this.codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public List<Usuario> getMiembros() {
        return new ArrayList<>(miembros);
    }

    public Map<Usuario, Double> getSaldos() {
        return new HashMap<>(saldos);
    }

    public double getSaldoMiembro(Usuario miembro) {
        return saldos.getOrDefault(miembro, 0.0);
    }

    public List<Gasto> getGastos() {
        return new ArrayList<>(gastos);
    }

    public EstrategiaDistribucion getEstrategiaDistribucion() {
        return this.estrategiaDistribucion;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEstrategiaDistribucion(EstrategiaDistribucion estrategia) {
        this.estrategiaDistribucion = estrategia;
        // Recalcular saldos con la nueva estrategia
        recalcularSaldos();
    }

    /**
     * Añade un nuevo miembro a la cuenta
     */
    public void addMiembro(Usuario usuario) {
        if (!miembros.contains(usuario)) {
            miembros.add(usuario);
            saldos.put(usuario, 0.0);
            usuario.addCuentaCompartida(this);
        }
    }

    /**
     * Elimina un miembro de la cuenta
     */
    public void removeMiembro(Usuario usuario) {
        if (miembros.contains(usuario)) {
            miembros.remove(usuario);
            saldos.remove(usuario);
            usuario.removeCuentaCompartida(this);
        }
    }

    /**
     * Registra un gasto en la cuenta compartida
     */
    public void addGasto(Gasto gasto) {
        if (gasto.getCuentaCompartida() == this) {
            gastos.add(gasto);
            // Calcular la distribución usando la estrategia
            calcularDistribucion(gasto.getPropietario(), gasto.getImporte());
        }
    }

    /**
     * Elimina un gasto de la cuenta
     */
    public void removeGasto(Gasto gasto) {
        if (gastos.remove(gasto)) {
            // Recalcular todos los saldos desde cero
            recalcularSaldos();
        }
    }

    /**
     * Calcula la distribución de un gasto usando la estrategia configurada
     */
    private void calcularDistribucion(Usuario pagador, double importe) {
        Map<Usuario, Double> distribucion = estrategiaDistribucion.calcular(pagador, importe, miembros);
        
        // Actualizar los saldos con la distribución calculada
        for (Map.Entry<Usuario, Double> entry : distribucion.entrySet()) {
            Usuario miembro = entry.getKey();
            double cambio = entry.getValue();
            
            double saldoActual = saldos.get(miembro);
            saldos.put(miembro, saldoActual + cambio);
        }
    }

    /**
     * Recalcula todos los saldos desde cero (útil al eliminar gastos o cambiar estrategia)
     */
    private void recalcularSaldos() {
        // Reiniciar todos los saldos a 0
        for (Usuario miembro : miembros) {
            saldos.put(miembro, 0.0);
        }
        
        // Recalcular cada gasto
        for (Gasto gasto : gastos) {
            calcularDistribucion(gasto.getPropietario(), gasto.getImporte());
        }
    }

    /**
     * Calcula el total gastado en esta cuenta
     */
    public double getTotalGastado() {
        return gastos.stream()
            .mapToDouble(Gasto::getImporte)
            .sum();
    }

    /**
     * Obtiene un resumen de los saldos de la cuenta
     */
    public String obtenerResumenSaldos() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cuenta: ").append(nombre).append("\n");
        sb.append("Total gastado: ").append(String.format("%.2f€", getTotalGastado())).append("\n");
        sb.append("Estrategia: ").append(estrategiaDistribucion.toString()).append("\n");
        sb.append("Saldos:\n");
        
        for (Map.Entry<Usuario, Double> entry : saldos.entrySet()) {
            Usuario miembro = entry.getKey();
            double saldo = entry.getValue();
            
            if (saldo > 0) {
                sb.append("  ").append(miembro.getNombre()).append(": +")
                  .append(String.format("%.2f", saldo)).append("€ (le deben)\n");
            } else if (saldo < 0) {
                sb.append("  ").append(miembro.getNombre()).append(": ")
                  .append(String.format("%.2f", saldo)).append("€ (debe)\n");
            } else {
                sb.append("  ").append(miembro.getNombre()).append(": 0.00€ (equilibrado)\n");
            }
        }
        
        return sb.toString();
    }

    /**
     * Verifica si un usuario es miembro de esta cuenta
     */
    public boolean esMiembro(Usuario usuario) {
        return miembros.contains(usuario);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CuentaCompartida cuenta = (CuentaCompartida) obj;
        return codigo.equals(cuenta.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return String.format("CuentaCompartida[%s - %s - %d miembros]", 
            codigo, nombre, miembros.size());
    }
}