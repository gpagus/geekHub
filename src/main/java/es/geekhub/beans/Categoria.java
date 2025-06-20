package es.geekhub.beans;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa una categoría en la aplicación, que incluye un identificador, 
 * un nombre y una imagen asociada.
 * 
 * Implementa Serializable para permitir la transferencia de objetos.
 * Sobrescribe los métodos hashCode y equals para comparaciones basadas en atributos.
 * 
 * @author agp00
 */
public class Categoria implements Serializable {

    private Byte idCategoria;
    private String nombre;
    private String imagen;

    // Getters and Setters
    public Byte getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Byte idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Categoria other = (Categoria) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.imagen, other.imagen)) {
            return false;
        }
        return Objects.equals(this.idCategoria, other.idCategoria);
    }
    
    
    

}
