package es.um.tds.gestionGastos.modelo;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String codigo;
    private String nombre;
    private String email;
    private String password;
    private List<Gasto> gastos;
    private List<Categoria> categorias;
    private List<Alerta> alertas;
    private List<CuentaCompartida> cuentasCompartidas;

    // Constructor
    public Usuario(String codigo, String nombre, String email, String password) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.gastos = new ArrayList<>();
        this.categorias = new ArrayList<>();
        this.alertas = new ArrayList<>();
        this.cuentasCompartidas = new ArrayList<>();
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

    // Setters (solo para datos modificables)
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
        this.gastos.add(gasto);
    }

    public void removeGasto(Gasto gasto) {
        this.gastos.remove(gasto);
    }

    // Métodos de negocio para gestionar categorías
    public void addCategoria(Categoria categoria) {
        this.categorias.add(categoria);
    }

    public void removeCategoria(Categoria categoria) {
        this.categorias.remove(categoria);
    }

    // Métodos de negocio para gestionar alertas
    public void addAlerta(Alerta alerta) {
        this.alertas.add(alerta);
    }

    public void removeAlerta(Alerta alerta) {
        this.alertas.remove(alerta);
    }

    // Métodos de negocio para gestionar cuentas compartidas
    public void addCuentaCompartida(CuentaCompartida cuenta) {
        this.cuentasCompartidas.add(cuenta);
    }

    public void removeCuentaCompartida(CuentaCompartida cuenta) {
        this.cuentasCompartidas.remove(cuenta);
    }
}