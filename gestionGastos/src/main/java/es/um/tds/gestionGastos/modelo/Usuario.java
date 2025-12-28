package es.um.tds.gestionGastos.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Clase que representa un usuario del sistema.
 * Un usuario tiene su propia "cuenta personal" de gastos y puede participar
 * en múltiples cuentas compartidas.
 * El código se genera automáticamente con formato U + 3 dígitos (U001, U002, etc.)
 */
public class Usuario {
    private static final AtomicInteger contadorUsuarios = new AtomicInteger(0);
    
    private String codigo;
    private String nombre;
    private String email;
    private String password;
    private List<Gasto> gastos; // Todos los gastos del usuario (personales y compartidos)
    private List<Categoria> categorias;
    private List<Alerta> alertas;
    private List<CuentaCompartida> cuentasCompartidas;
    private List<Notificacion> notificaciones;

    /**
     * Constructor con código autogenerado
     */
    public Usuario(String nombre, String email, String password) {
        this.codigo = generarCodigo();
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.gastos = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.alertas = new ArrayList<>();
        this.cuentasCompartidas = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
    }

    /**
     * Constructor con código específico
     */
    public Usuario(String codigo, String nombre, String email, String password) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.gastos = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.alertas = new ArrayList<>();
        this.cuentasCompartidas = new ArrayList<>();
        this.notificaciones = new ArrayList<>();
        
        actualizarContador(codigo);
    }

    /**
     * Genera un código único en formato U001, U002, etc.
     */
    private static String generarCodigo() {
        int numero = contadorUsuarios.incrementAndGet();
        return String.format("U%03d", numero);
    }

    /**
     * Actualiza el contador basándose en un código existente
     */
    private static void actualizarContador(String codigo) {
        if (codigo != null && codigo.startsWith("U")) {
            try {
                int numero = Integer.parseInt(codigo.substring(1));
                contadorUsuarios.updateAndGet(current -> Math.max(current, numero));
            } catch (NumberFormatException e) {
                // Código inválido, ignorar
            }
        }
    }

    /**
     * Reinicia el contador (útil para tests)
     */
    public static void reiniciarContador() {
        contadorUsuarios.set(0);
    }

    // Getters básicos
    public String getCodigo() {
        return this.codigo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public List<Gasto> getGastos() {
        return this.gastos;
    }

    public List<Categoria> getCategorias() {
        return this.categorias;
    }

    public List<Alerta> getAlertas() {
        return this.alertas;
    }

    public List<CuentaCompartida> getCuentasCompartidas() {
        return this.cuentasCompartidas;
    }

    public List<Notificacion> getNotificaciones() {
        return this.notificaciones;
    }

    // Setters 
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Métodos de negocio para gestionar gastos
    public void addGasto(Gasto gasto) {
        if (gasto.getPropietario().equals(this)) {
            this.gastos.add(gasto);
            
            // Si es gasto compartido, también añadirlo a la cuenta compartida
            if (gasto.esGastoCompartido()) {
                gasto.getCuentaCompartida().addGasto(gasto);
            }
        }
    }

    public void removeGasto(Gasto gasto) {
        this.gastos.remove(gasto);
        
        // Si es gasto compartido, también quitarlo de la cuenta
        if (gasto.esGastoCompartido()) {
            gasto.getCuentaCompartida().removeGasto(gasto);
        }
    }

    /**
     * Obtiene solo los gastos personales (no compartidos)
     */
    public List<Gasto> getGastosPersonales() {
        return gastos.stream()
            .filter(Gasto::esGastoPersonal)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene solo los gastos compartidos
     */
    public List<Gasto> getGastosCompartidos() {
        return gastos.stream()
            .filter(Gasto::esGastoCompartido)
            .collect(Collectors.toList());
    }

    /**
     * Calcula el total gastado (personal + compartido)
     */
    public double getTotalGastado() {
        return gastos.stream()
            .mapToDouble(Gasto::getImporte)
            .sum();
    }

    /**
     * Calcula el total de gastos personales
     */
    public double getTotalGastosPersonales() {
        return getGastosPersonales().stream()
            .mapToDouble(Gasto::getImporte)
            .sum();
    }

    // Métodos de negocio para gestionar categorías
    public void addCategoria(Categoria categoria) {
        if (!this.categorias.contains(categoria)) {
            this.categorias.add(categoria);
        }
    }

    public void removeCategoria(Categoria categoria) {
        this.categorias.remove(categoria);
    }

    public Categoria buscarCategoriaPorNombre(String nombre) {
        return categorias.stream()
            .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
            .findFirst()
            .orElse(null);
    }

    // Métodos de negocio para gestionar alertas
    public void addAlerta(Alerta alerta) {
        this.alertas.add(alerta);
    }

    public void removeAlerta(Alerta alerta) {
        this.alertas.remove(alerta);
    }

    /**
     * Verifica todas las alertas activas y genera notificaciones si procede
     */
    public void verificarAlertas() {
        LocalDate fechaActual = LocalDate.now();
        
        for (Alerta alerta : alertas) {
            if (alerta.isActiva() && alerta.verificarAlerta(gastos, fechaActual)) {
                // Generar notificación
                String mensaje = String.format(
                    "¡Alerta! Has superado el límite de %.2f€ en tu alerta %s",
                    alerta.getLimiteGasto(),
                    alerta.getTipoAlerta()
                );
                
                Notificacion notif = new Notificacion(mensaje, alerta);
                addNotificacion(notif);
            }
        }
    }

    // Métodos de negocio para gestionar cuentas compartidas
    public void addCuentaCompartida(CuentaCompartida cuenta) {
        if (!this.cuentasCompartidas.contains(cuenta)) {
            this.cuentasCompartidas.add(cuenta);
        }
    }

    public void removeCuentaCompartida(CuentaCompartida cuenta) {
        this.cuentasCompartidas.remove(cuenta);
    }

    /**
     * Calcula el balance total en todas las cuentas compartidas
     * (positivo = le deben dinero, negativo = debe dinero)
     */
    public double getBalanceCuentasCompartidas() {
        return cuentasCompartidas.stream()
            .mapToDouble(cuenta -> cuenta.getSaldoMiembro(this))
            .sum();
    }

    // Métodos de negocio para gestionar notificaciones
    public void addNotificacion(Notificacion notificacion) {
        this.notificaciones.add(notificacion);
    }

    public void removeNotificacion(Notificacion notificacion) {
        this.notificaciones.remove(notificacion);
    }

    /**
     * Obtiene las notificaciones no leídas
     */
    public List<Notificacion> getNotificacionesNoLeidas() {
        return notificaciones.stream()
            .filter(n -> !n.isLeida())
            .collect(Collectors.toList());
    }

    /**
     * Marca todas las notificaciones como leídas
     */
    public void marcarTodasNotificacionesLeidas() {
        notificaciones.forEach(Notificacion::marcarComoLeida);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return codigo.equals(usuario.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Usuario[%s - %s - %s]", codigo, nombre, email);
    }
}