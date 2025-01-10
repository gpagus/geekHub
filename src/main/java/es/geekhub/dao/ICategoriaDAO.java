package es.geekhub.dao;

import es.geekhub.beans.Categoria;
import java.util.List;

/**
 *
 * @author agp00
 */
public interface ICategoriaDAO {

    public void closeConnection();
    public List<Categoria> obtenerCategoriasConProductos();
}
