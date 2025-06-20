package es.geekhub.dao;

import es.geekhub.beans.LineaPedido;
import java.util.List;

/**
 *
 * @author agp00
 */
public interface ILineaPedidoDAO {
    
    public LineaPedido obtenerLineaPedido(Short idPedido, Short idProducto);
    public boolean guardarLineaPedido(LineaPedido lineaPedido);
    public boolean actualizarLineaPedido(LineaPedido lineaPedido);
    public List<LineaPedido> getLineasPedidoByIdPedido(Short idPedido);
    public boolean eliminarLineaPedido(Short idPedido, Short idProducto);
    public void closeConnection();
    
    
}
