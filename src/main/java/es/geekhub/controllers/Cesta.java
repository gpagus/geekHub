package es.geekhub.controllers;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

/**
 * Servlet que gestiona las operaciones relacionadas con el carrito de compras.
 *
 * <p>
 * Permite realizar las siguientes acciones:</p>
 * <ul>
 * <li><strong>add:</strong> Agregar un producto al carrito.</li>
 * <li><strong>remove:</strong> Eliminar un producto del carrito.</li>
 * <li><strong>limpiarCarrito:</strong> Vaciar el carrito de compras.</li>
 * <li><strong>finalizar:</strong> Finalizar y registrar el pedido.</li>
 * </ul>
 *
 * <p>
 * El carrito puede ser manejado para usuarios registrados o usuarios anónimos
 * utilizando cookies para sincronizar la información.</p>
 *
 * @author agp00
 */
@WebServlet(name = "Cesta", urlPatterns = {"/Cesta"})
public class Cesta extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/JSP/carrito.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        DAOFactory daof = new DAOFactory();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Map<Short, Integer> carrito = new HashMap();
        String url = "/JSP/carrito.jsp";
        String accion = request.getParameter("accion");

        if (usuario == null) {
            // Obtener el carrito desde las cookies
            Cookie[] cookies = request.getCookies();
            carrito = UtilsCookie.cargarCarritoDesdeCookie(cookies);
        }

        Pedido pedido = Utils.obtenerPedidoDeSesion(session, daof);

        switch (accion) {

            case "add":
                String idProducto = request.getParameter("idProducto");
                if (idProducto != null) {
                    Short idProductoShort = Short.valueOf(idProducto);

                    try {
                        if (usuario == null) {

                            // Incrementa la cantidad del producto en el carrito o lo añade con cantidad 1
                            carrito.put(idProductoShort, carrito.getOrDefault(idProductoShort, 0) + 1);

                            Utils.sincronizarPedidoConCarrito(pedido, carrito);

                            pedido.setImporte(Utils.calcularImporteTotal(pedido.getLineasPedidos()));
                            pedido.setIva(pedido.getImporte() * 0.21);

                            UtilsCookie.actualizarCookie(response, carrito);

                        } else {

                            IPedidoDAO daoPedido = daof.getPedidoDAO();
                            ILineaPedidoDAO daoLineaPedido = daof.getLineaPedidoDAO();

                            // Agregar o actualizar la línea de pedido
                            LineaPedido lineaPedido = daoLineaPedido.obtenerLineaPedido(pedido.getIdPedido(), idProductoShort);
                            if (lineaPedido == null) {
                                lineaPedido = new LineaPedido();
                                Producto producto = new Producto();
                                producto.setIdProducto(idProductoShort);
                                lineaPedido.setPedido(pedido);
                                lineaPedido.setProducto(producto);
                                Byte cantidadInicial = 1;
                                lineaPedido.setCantidad(cantidadInicial);
                                daoLineaPedido.guardarLineaPedido(lineaPedido);
                            } else {
                                Byte nuevaCantidad = (byte) (lineaPedido.getCantidad() + 1);
                                lineaPedido.setCantidad(nuevaCantidad);
                                daoLineaPedido.actualizarLineaPedido(lineaPedido);
                            }

                            List<LineaPedido> lineas = daoLineaPedido.getLineasPedidoByIdPedido(pedido.getIdPedido());
                            pedido.setLineasPedidos(lineas);

                            // Recalcular importe e IVA
                            pedido.setImporte(Utils.calcularImporteTotal(pedido.getLineasPedidos()));
                            pedido.setIva(pedido.getImporte() * 0.21);
                            daoPedido.actualizarPedido(pedido);
                        }

                        session.setAttribute("pedido", pedido);
                        request.setAttribute("aviso", "Producto añadido al carrito");
                        request.setAttribute("tipoAviso", "success");

                    } catch (NumberFormatException e) {
                        // Manejar el caso donde el ID del producto no es un número válido
                        request.setAttribute("error", "ID de producto inválido.");
                    }
                }

                url = "/JSP/menu.jsp";

                break;

            case "remove":

                url = "/JSP/carrito.jsp";
                String idProductoEliminar = request.getParameter("idProducto");

                if (idProductoEliminar != null) {
                    try {
                        // Convertir el idProducto recibido a short
                        short idProductoShort = Short.parseShort(idProductoEliminar);

                        if (usuario == null) {

                            // Eliminar el producto del carrito
                            carrito.remove(idProductoShort);
                            if (carrito.isEmpty()) {
                                url = "/FrontController";
                            }

                            // Actualizar la cookie del carrito
                            UtilsCookie.actualizarCookie(response, carrito);

                            List<LineaPedido> lineasPedidos = pedido.getLineasPedidos();
                            for (Iterator<LineaPedido> iterator = lineasPedidos.iterator(); iterator.hasNext();) {
                                LineaPedido linea = iterator.next();
                                if (linea.getProducto().getIdProducto() == idProductoShort) {
                                    iterator.remove();
                                }
                            }

                            // Recalcular el importe e IVA
                            pedido.setImporte(Utils.calcularImporteTotal(pedido.getLineasPedidos()));
                            pedido.setIva(pedido.getImporte() * 0.21);

                        } else {

                            IPedidoDAO daoPedido = daof.getPedidoDAO();
                            ILineaPedidoDAO daoLineaPedido = daof.getLineaPedidoDAO();

                            daoLineaPedido.eliminarLineaPedido(pedido.getIdPedido(), idProductoShort);

                            List<LineaPedido> lineas = daoLineaPedido.getLineasPedidoByIdPedido(pedido.getIdPedido());
                            pedido.setLineasPedidos(lineas);

                            pedido.setImporte(Utils.calcularImporteTotal(pedido.getLineasPedidos()));
                            pedido.setIva(pedido.getImporte() * 0.21);
                            daoPedido.actualizarPedido(pedido);

                            if (lineas.isEmpty()) {
                                url = "/JSP/menu.jsp";
                            }

                        }

                        // Actualizar el pedido en la sesión
                        session.setAttribute("pedido", pedido);
                        request.setAttribute("aviso", "Producto eliminado al carrito");
                        request.setAttribute("tipoAviso", "success");

                    } catch (NumberFormatException e) {
                        // Manejar el caso donde el ID del producto no sea válido
                        request.setAttribute("error", "ID de producto inválido.");
                    }
                }
                break;

            case "limpiarCarrito":
                if (usuario == null) {
                    Map<Short, Integer> carritoVacio = new HashMap();
                    UtilsCookie.actualizarCookie(response, carritoVacio);
                } else {
                    IPedidoDAO daoPedido = daof.getPedidoDAO();
                    daoPedido.eliminarPedido(pedido.getIdPedido());
                }
                session.removeAttribute("pedido");
                request.setAttribute("aviso", "Se ha vaciado el carrito");
                request.setAttribute("tipoAviso", "success");
                url = "/JSP/menu.jsp";
                break;

            case "finalizar":
                if (usuario != null) {
                    IPedidoDAO daoPedido = daof.getPedidoDAO();

                    // Finalizar pedido en base de datos
                    daoPedido.finalizarPedido(pedido.getIdPedido());

                    // Limpiar pedido de la sesión
                    session.removeAttribute("pedido");

                    request.setAttribute("aviso", "Compra realizada");
                    request.setAttribute("tipoAviso", "success");

                    url = "/JSP/menu.jsp";
                }
                break;

            // Redirección usuario logueado para sincronizar el pedido de la sesión con la bbdd
            default:

                IPedidoDAO daoPedido = daof.getPedidoDAO();
                ILineaPedidoDAO daoLineaPedido = daof.getLineaPedidoDAO();

                List<LineaPedido> lineas = daoLineaPedido.getLineasPedidoByIdPedido(pedido.getIdPedido());
                pedido.setLineasPedidos(lineas);

                // Recalcular importe e IVA
                pedido.setImporte(Utils.calcularImporteTotal(pedido.getLineasPedidos()));
                pedido.setIva(pedido.getImporte() * 0.21);
                daoPedido.actualizarPedido(pedido);

                session.setAttribute("pedido", pedido);
                url = "/JSP/menu.jsp";

                break;

        }

        request.getRequestDispatcher(url).forward(request, response);

    }

}
