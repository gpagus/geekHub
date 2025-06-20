package es.geekhub.controllers;

import com.google.gson.JsonObject;
import es.geekhub.beans.LineaPedido;
import es.geekhub.beans.Pedido;
import es.geekhub.beans.Producto;
import es.geekhub.beans.Usuario;
import es.geekhub.dao.ILineaPedidoDAO;
import es.geekhub.dao.IPedidoDAO;
import es.geekhub.daofactory.DAOFactory;
import es.geekhub.models.Utils;
import es.geekhub.models.UtilsCookie;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet que gestiona las operaciones de cambio de cantidad en el carrito de compras.
 * 
 * <p>Permite aumentar o disminuir la cantidad de un producto en el carrito, tanto
 * para usuarios anónimos como registrados. Actualiza la información del carrito
 * y recalcula el importe total e IVA del pedido.</p>
 * 
 * <p>Responde con un JSON que contiene información actualizada del carrito y 
 * el pedido.</p>
 * 
 * <strong>Acciones disponibles:</strong>
 * <ul>
 *   <li><strong>aumentar:</strong> Incrementa la cantidad de un producto.</li>
 *   <li><strong>restar:</strong> Decrementa la cantidad de un producto, con un mínimo de 1.</li>
 * </ul>
 * 
 * @author agp00
 */
@WebServlet(name = "AjaxCantidad", urlPatterns = {"/AjaxCantidad"})
public class AjaxCantidad extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String idProductoStr = request.getParameter("idProducto");
        String accion = request.getParameter("accion");

        DAOFactory daof = new DAOFactory();
        IPedidoDAO daoPedido = daof.getPedidoDAO();
        ILineaPedidoDAO daoLineaPedido = daof.getLineaPedidoDAO();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Pedido pedido = Utils.obtenerPedidoDeSesion(session, daof);

        try {
            Short idProducto = Short.valueOf(idProductoStr);
            double importeTotal;
            Byte cantidad = 1;
            LineaPedido lineaPedido = null;
            Producto producto = null;

            if (usuario == null) {
                // Manejo de carrito para usuario anónimo
                Cookie[] cookies = request.getCookies();
                Map<Short, Integer> carrito = UtilsCookie.cargarCarritoDesdeCookie(cookies);

                if ("aumentar".equals(accion)) {
                    carrito.put(idProducto, carrito.getOrDefault(idProducto, 0) + 1);
                } else if ("restar".equals(accion)) {
                    int cant = carrito.getOrDefault(idProducto, 1);
                    carrito.put(idProducto, Math.max(1, cant - 1));
                }

                Utils.sincronizarPedidoConCarrito(pedido, carrito);
                pedido.setImporte(Utils.calcularImporteTotal(pedido.getLineasPedidos()));
                pedido.setIva(pedido.getImporte() * 0.21);

                UtilsCookie.actualizarCookie(response, carrito);

                // Buscar la línea de pedido para obtener cantidad y producto
                lineaPedido = null;
                for (LineaPedido linea : pedido.getLineasPedidos()) {
                    if (linea.getProducto().getIdProducto().equals(idProducto)) {
                        lineaPedido = linea;
                        break;
                    }
                }

                if (lineaPedido != null) {
                    cantidad = lineaPedido.getCantidad();
                    producto = lineaPedido.getProducto();
                }

                importeTotal = pedido.getImporte();

            } else {
                // Manejo de carrito para usuario registrado
                lineaPedido = daoLineaPedido.obtenerLineaPedido(pedido.getIdPedido(), idProducto);

                if (lineaPedido == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Producto no encontrado en el carrito");
                    return;
                }

                cantidad = lineaPedido.getCantidad();
                if ("aumentar".equals(accion)) {
                    cantidad++;
                } else if ("restar".equals(accion)) {
                    cantidad = (byte) Math.max(1, cantidad - 1);
                }

                lineaPedido.setCantidad(cantidad);
                daoLineaPedido.actualizarLineaPedido(lineaPedido);

                List<LineaPedido> lineas = daoLineaPedido.getLineasPedidoByIdPedido(pedido.getIdPedido());
                pedido.setLineasPedidos(lineas);

                importeTotal = Utils.calcularImporteTotal(lineas);
                pedido.setImporte(importeTotal);
                pedido.setIva(pedido.getImporte() * 0.21);
                daoPedido.actualizarPedido(pedido);

                producto = lineaPedido.getProducto();
            }

            session.setAttribute("pedido", pedido);

            // Preparar respuesta JSON
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("baseImpo", String.format("%.2f", importeTotal));
            jsonResponse.addProperty("iva", String.format("%.2f", pedido.getIva()));
            jsonResponse.addProperty("totalPagar", String.format("%.2f", importeTotal + pedido.getIva()));
            jsonResponse.addProperty("cantidad", cantidad);
            jsonResponse.addProperty("importe", String.format("%.2f", producto.getPrecio() * cantidad));

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse.toString());
            
            

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error procesando solicitud: " + e.getMessage());
        }
    }

}
