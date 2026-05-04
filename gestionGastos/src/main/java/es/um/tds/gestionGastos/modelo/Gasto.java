package es.um.tds.gestionGastos.modelo;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Gasto {
    private double cantidad;
    private LocalDate fecha;
    private String descripcion;
    private Categoria categoria;
    private Usuario usuario;

    public Gasto() {}

    @JsonCreator
    public Gasto(
            @JsonProperty("cantidad") double cantidad,
            @JsonProperty("fecha") LocalDate fecha,
            @JsonProperty("descripcion") String descripcion,
            @JsonProperty("categoria") Categoria categoria,
            @JsonProperty("usuario") Usuario usuario) {
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.usuario = usuario;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void editar(double nuevaCantidad, LocalDate nuevaFecha, String nuevaDescripcion, Categoria nuevaCategoria) {
        this.cantidad = nuevaCantidad;
        this.fecha = nuevaFecha;
        this.descripcion = nuevaDescripcion;
        this.categoria = nuevaCategoria;
    }
}
