package es.geekhub.beans;

/**
 *
 * @author agp00
 */
public class Categoria {

    private byte idCategoria;
    private String nombre;
    private String imagen;

    // Getters and Setters
    public byte getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(byte idCategoria) {
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

}
