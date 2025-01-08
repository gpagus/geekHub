package es.geekhub.dao;

import es.geekhub.beans.Producto;
import java.util.List;

/**
 *
 * @author agp00
 */
public interface IProductoDAO {
    public List<Producto> productosAleatorios();
    public void closeConnection();
}
