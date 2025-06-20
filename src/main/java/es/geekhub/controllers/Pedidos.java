package es.geekhub.controllers;

import es.geekhub.beans.LineaPedido;
import es.geekhub.beans.Pedido;
import es.geekhub.beans.Usuario;
import es.geekhub.dao.ILineaPedidoDAO;
import es.geekhub.dao.IPedidoDAO;
import es.geekhub.daofactory.DAOFactory;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet que gestiona la visualización de los pedidos finalizados por el
 * usuario registrado.
 *
 * <p>
 * Carga los pedidos finalizados de un usuario registrado y sus respectivas
 * líneas de pedido, y los muestra en la página de perfil.</p>
 *
 * <strong>Flujo:</strong>
 * <ol>
 * <li>Obtiene el usuario de la sesión.</li>
 * <li>Recupera los pedidos finalizados desde la base de datos.</li>
 * <li>Asocia las líneas de pedido a cada pedido.</li>
 * <li>Guarda los datos en la sesión y redirige a la vista de pedidos.</li>
 * </ol>
 *
 * @author agp00
 */
@WebServlet(name = "Pedidos", urlPatterns = {"/Pedidos"})
public class Pedidos extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        HttpSession session = request.getSession();
        session.removeAttribute("pedidosFinalizados");
        DAOFactory daof = new DAOFactory();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String url = "/JSP/perfil/pedidos.jsp";

        IPedidoDAO pedidoDAO = daof.getPedidoDAO();
        List<Pedido> pedidosFinalizados = pedidoDAO.getPedidosFinalizadosPorUsuario(usuario);

        // Cargar líneas de pedido para cada pedido finalizado
        ILineaPedidoDAO lineaPedidoDAO = daof.getLineaPedidoDAO();
        for (Pedido pedido : pedidosFinalizados) {
            List<LineaPedido> lineasPedido = lineaPedidoDAO.getLineasPedidoByIdPedido(pedido.getIdPedido());
            pedido.setLineasPedidos(lineasPedido);

            session.setAttribute("pedidosFinalizados", pedidosFinalizados);
        }

        request.getRequestDispatcher(url).forward(request, response);

    }

}
