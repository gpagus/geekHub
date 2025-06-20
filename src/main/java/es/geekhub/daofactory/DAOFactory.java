package es.geekhub.daofactory;

import es.geekhub.dao.CategoriaDAO;
import es.geekhub.dao.ICategoriaDAO;
import es.geekhub.dao.ILineaPedidoDAO;
import es.geekhub.dao.IPedidoDAO;
import es.geekhub.dao.IProductoDAO;
import es.geekhub.dao.IUsuarioDAO;
import es.geekhub.dao.LineaPedidoDAO;
import es.geekhub.dao.PedidoDAO;
import es.geekhub.dao.ProductoDAO;
import es.geekhub.dao.UsuarioDAO;

/**
 * Fábrica para crear instancias de los DAO utilizados en la aplicación.
 *
 * <p>
 * Proporciona métodos para obtener implementaciones específicas de las
 * interfaces DAO, permitiendo el acceso a las operaciones de la base de datos
 * para distintas entidades.</p>
 *
 * <p>
 * Los DAO disponibles son:</p>
 * <ul>
 * <li>{@link IProductoDAO}</li>
 * <li>{@link ICategoriaDAO}</li>
 * <li>{@link IUsuarioDAO}</li>
 * <li>{@link IPedidoDAO}</li>
 * <li>{@link ILineaPedidoDAO}</li>
 * </ul>
 *
 * @author agp00
 */
public class DAOFactory {

    public IProductoDAO getProductoDAO() {
        return new ProductoDAO();
    }

    public ICategoriaDAO getCategoriaDAO() {
        return new CategoriaDAO();
    }

    public IUsuarioDAO getUsuarioDAO() {
        return new UsuarioDAO();
    }

    public IPedidoDAO getPedidoDAO() {
        return new PedidoDAO();
    }

    public ILineaPedidoDAO getLineaPedidoDAO() {
        return new LineaPedidoDAO();
    }

}
