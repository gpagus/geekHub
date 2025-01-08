package es.geekhub.controllers;

import es.geekhub.beans.Producto;
import es.geekhub.dao.IProductoDAO;
import es.geekhub.daofactory.DAOFactory;
import es.geekhub.models.Utils;
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
        IProductoDAO daop = daof.getProductoDAO();
        List<Producto> productos = daop.productosAleatorios();
        session.setAttribute("productos", productos);

        Cookie[] cookies = request.getCookies();
        Map<Short, Integer> carrito = null;

        // intentamos cargar el carrito desde la cookie
        if (carrito == null) {
            carrito = Utils.cargarCarritoDesdeCookie(cookies);
        }

        // Si no existe el carrito en la cookie, inicializarlo vacío
        if (carrito == null) {
            carrito = new HashMap();
        }

        // Guardar el carrito en la sesión
        session.setAttribute("carrito", carrito);

        Utils.actualizarCookie(response, carrito);

        request.getRequestDispatcher(url).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url = "/JSP/menu.jsp";
        String accion = request.getParameter("accion");
        HttpSession session = request.getSession();
        // List<Producto> productos = (List<Producto>) request.getSession().getAttribute("productos");

        if (accion != null) {
            switch (accion) {
                case "login":
                    url = "/JSP/acceso/login.jsp";
                    break;
                case "registro":
                    url = "/JSP/acceso/registro.jsp";
                    break;
                case "carrito":
                    url = "/JSP/carrito.jsp";
                    break;
                case "cuenta":
                    break;
                case "limpiar":

                    Map<Short, Integer> carritoVacio = new HashMap();

                    session.setAttribute("carrito", carritoVacio);

                    Utils.actualizarCookie(response, carritoVacio);
                    break;

            }
        }

        request.getRequestDispatcher(url).forward(request, response);
    }

}
