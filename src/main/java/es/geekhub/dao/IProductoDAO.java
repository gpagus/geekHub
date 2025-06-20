package es.geekhub.dao;

import es.geekhub.beans.Filtros;
import es.geekhub.beans.Producto;
import java.util.List;

/**
 *
 * @author agp00
 */
public interface IProductoDAO {
    public List<Producto> productosAleatorios();
    public List<Producto> obtenerProductosPorFiltros(Filtros filtros);
    public List<Producto> buscarProductos(String query);
    public List<String> obtenerMarcasDisponibles();
    public Producto getProductoById(Short idProducto);
    public void closeConnection();
}
