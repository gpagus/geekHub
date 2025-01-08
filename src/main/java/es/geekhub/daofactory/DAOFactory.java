package es.geekhub.daofactory;

import es.geekhub.dao.IProductoDAO;
import es.geekhub.dao.ProductoDAO;

/**
 *
 * @author agp00
 */
public class DAOFactory {
    
    public IProductoDAO getProductoDAO() {
    return new ProductoDAO();
   }
    
}
