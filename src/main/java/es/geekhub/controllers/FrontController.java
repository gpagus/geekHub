package es.geekhub.controllers;

import es.geekhub.beans.Pedido;
import es.geekhub.beans.Producto;
import es.geekhub.beans.Usuario;

import es.geekhub.dao.IProductoDAO;
import es.geekhub.daofactory.DAOFactory;
import es.geekhub.models.Utils;
import es.geekhub.models.UtilsCookie;

import java.io.IOException;
import java.util.HashMap;
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
 * Servlet principal que actúa como un controlador frontal para manejar diversas
 * acciones en la aplicación web.
 *
 * <p>
 * Gestiona la navegación y las operaciones generales, como el manejo del
 * carrito, búsqueda de productos, registro, edición de perfil, visualización de
 * pedidos, cierre de sesión, y detalles de productos.</p>
 *
 * <strong>Acciones soportadas:</strong>
 * <ul>
 * <li><strong>registro:</strong> Redirige a la página de registro.</li>
 * <li><strong>carrito:</strong> Redirige a la página del carrito de
 * compras.</li>
 * <li><strong>editar:</strong> Redirige a la página de edición de perfil.</li>
 * <li><strong>pedidos:</strong> Redirige a la gestión de pedidos.</li>
 * <li><strong>cerrarSesion:</strong> Cierra la sesión del usuario actual.</li>
 * <li><strong>buscar:</strong> Busca productos según la consulta del
 * usuario.</li>
 * <li><strong>verProducto:</strong> Muestra los detalles de un producto
 * seleccionado.</li>
 * </ul>
 *
 * <p>
 * Además, inicializa el carrito de compras y carga productos aleatorios en la
 * sesión si no están disponibles.</p>
 *
 * @author agp00
 */
@WebServlet(name = "FrontController", urlPatterns = {"/FrontController"})
public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = "/JSP/menu.jsp";
        HttpSession session = request.getSession();
        DAOFactory daof = new DAOFactory();
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Producto> productos = (List<Producto>) session.getAttribute("productos");

        if (productos == null || productos.isEmpty()) {

            IProductoDAO daop = daof.getProductoDAO();
            productos = daop.productosAleatorios();
            session.setAttribute("productos", productos);
        }

        Pedido pedido = Utils.obtenerPedidoDeSesion(session, daof);

        if (pedido.getLineasPedidos().isEmpty() && usuario == null) {
            Cookie[] cookies = request.getCookies();
            Map<Short, Integer> carritoCookie;
            carritoCookie = UtilsCookie.cargarCarritoDesdeCookie(cookies);
            if (carritoCookie == null) {
                carritoCookie = new HashMap<>();
            }

            Utils.sincronizarPedidoConCarrito(pedido, carritoCookie);

            pedido.setImporte(Utils.calcularImporteTotal(pedido.getLineasPedidos()));
            pedido.setIva(pedido.getImporte() * 0.21);
            session.setAttribute("pedido", pedido);
        }

        request.getRequestDispatcher(url).forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "/JSP/menu.jsp";
        String accion = request.getParameter("accion");
        HttpSession session = request.getSession();

        if (accion != null) {
            switch (accion) {
                case "registro":
                    url = "/JSP/registro.jsp";
                    break;
                case "carrito":
                    url = "/JSP/carrito.jsp";
                    break;
                case "editar":
                    url = "/JSP/perfil/editar.jsp";
                    break;
                case "pedidos":
                    url = "/Pedidos";
                    break;
                case "cerrarSesion":
                    session.removeAttribute("usuario");
                    session.removeAttribute("ultimoAccesoFormateado");
                    session.removeAttribute("pedido");

                    request.setAttribute("aviso", "Se ha cerrado la sesión");
                    request.setAttribute("tipoAviso", "success");
                    break;
                case "buscar":
                    String query = request.getParameter("query"); // Obtener la consulta ingresada por el usuario

                    DAOFactory daof = new DAOFactory();
                    IProductoDAO daop = daof.getProductoDAO();
                    // Realizar la búsqueda en la base de datos
                    List<Producto> productosEncontrados = daop.buscarProductos(query);
                    // Guardar los productos en el request
                    session.setAttribute("productos", productosEncontrados);
                    break;

                case "verProducto":
                    String idProductoStr = request.getParameter("idProducto");
                    Producto productoSeleccionado = null;

                    if (idProductoStr != null) {
                        Short idProducto = Short.valueOf(idProductoStr);
                        // Obtener lista de productos de la sesión
                        List<Producto> productos = (List<Producto>) session.getAttribute("productos");
                        // Buscar el producto en la lista
                        if (productos != null) {
                            for (Producto p : productos) {
                                if (p.getIdProducto().equals(idProducto)) {
                                    productoSeleccionado = p;
                                    break; // Producto encontrado, salimos del bucle
                                }
                            }
                        }

                    }

                    // Guardar el producto seleccionado en la solicitud
                    request.setAttribute("producto", productoSeleccionado);
                    url = "/JSP/detalles.jsp";
                    break;
            }
        }
        request.getRequestDispatcher(url).forward(request, response);
    }
}
