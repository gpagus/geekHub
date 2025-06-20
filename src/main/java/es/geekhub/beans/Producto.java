package es.geekhub.beans;

import java.io.Serializable;

/**
 * Representa un producto en la aplicación.
 * 
 * Un producto incluye información como su identificador, categoría asociada,
 * nombre, descripción, precio, marca e imagen.
 * 
 * Implementa Serializable para permitir la transferencia y almacenamiento de objetos.
 * 
 * @author agp00
 */
public class Producto implements Serializable{
    private Short idProducto;
    private Categoria categoria;
    private String nombre;
    private String descripcion;
    private double precio;
    private String marca;
    private String imagen;

    public Short getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Short idProducto) {
        this.idProducto = idProducto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
    
}
