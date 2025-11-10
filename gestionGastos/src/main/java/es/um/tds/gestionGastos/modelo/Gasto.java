package es.um.tds.gestionGastos.modelo;
import java.time.LocalDate;
public class Gasto {
    private float importe;
    private LocalDate fecha;
    private Categoria categoria; 
    private String codigo;
    public Gasto(float importe,LocalDate fecha,Categoria categoria,String codigo){
        this.importe = importe;
        this.fecha = fecha;
        this.categoria = categoria;
        this.codigo = codigo;
    }

    public void setImporte(float importe){
        this.importe = importe;
    }
    public void setFecha(LocalDate fecha){
        this.fecha = fecha;
    }
    public void setCategoria(Categoria categoria){
        this.categoria = categoria;
    }
    public float getImporte(){
        return this.importe;
    }
    public Categoria getCategoria(){
        return this.categoria;
    }
    public LocalDate getFecha(){
        return this.fecha;
    }
    public String getCodigo(){
        return this.codigo;
    }
}