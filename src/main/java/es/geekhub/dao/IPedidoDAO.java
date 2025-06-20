package es.geekhub.dao;

import es.geekhub.beans.Pedido;
import es.geekhub.beans.Usuario;
import java.util.List;

/**
 *
 * @author agp00
 */
public interface IPedidoDAO {
    
    public Pedido obtenerPedidoCarrito(Usuario usuario);
    public int crearPedido(Pedido pedido);
    public boolean actualizarPedido(Pedido pedido);
    public void eliminarPedido(Short idPedido);
    public void finalizarPedido(Short idPedido);
    public List<Pedido> getPedidosFinalizadosPorUsuario(Usuario usuario);
    public void closeConnection();
    
    
    
}
