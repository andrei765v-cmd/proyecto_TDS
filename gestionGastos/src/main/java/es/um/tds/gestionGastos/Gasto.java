package es.um.tds.gestionGastos;

import java.time.LocalDate;

import es.um.tds.gestionGastos.alertas.Categoria;

public class Gasto {
    private double cantidad;       
    private LocalDate fecha;      
    private String descripcion;    
    private Categoria categoria; 

    public Gasto(double cantidad, LocalDate fecha, String descripcion, Categoria categoria) {
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
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

    public void editar(double nuevaCantidad, LocalDate nuevaFecha, String nuevaDescripcion, Categoria nuevaCategoria) {
        this.cantidad = nuevaCantidad;
        this.fecha = nuevaFecha;
        this.descripcion = nuevaDescripcion;
        this.categoria = nuevaCategoria;
    }
}