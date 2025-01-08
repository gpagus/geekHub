package es.geekhub.controllers;

import es.geekhub.models.Utils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

/**
 *
 * @author agp00
 */
@WebServlet(name = "Cesta", urlPatterns = {"/Cesta"})
public class Cesta extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Cookie[] cookies = request.getCookies();
        String url = "/JSP/carrito.jsp";
        String accion = request.getParameter("accion");
        String idProducto;

        // Obtener el carrito desde la sesión o desde la cookie si no está en sesión
        Map<Short, Integer> carrito = (Map<Short, Integer>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = Utils.cargarCarritoDesdeCookie(cookies);
        }

        if (carrito == null) {
            carrito = new HashMap<>();
        }

        switch (accion) {

            case "add":
                idProducto = request.getParameter("idProducto");

                if (idProducto != null) {
                    try {
                        short idProductoShort = Short.parseShort(idProducto);

                        // Incrementar la cantidad del producto en el carrito o añadirlo con cantidad 1
                        carrito.put(idProductoShort, carrito.getOrDefault(idProductoShort, 0) + 1);

                    } catch (NumberFormatException e) {
                        // Manejar el caso donde el ID del producto no es un número válido
                        request.setAttribute("error", "ID de producto inválido.");
                    }
                }

                url = "/FrontController";

                break;

            case "remove":
                idProducto = request.getParameter("idProducto");

                if (idProducto != null) {
                    try {
                        short idProductoShort = Short.parseShort(idProducto);

                        // Eliminar el producto del carrito si existe
                        if (carrito.containsKey(idProductoShort)) {
                            carrito.remove(idProductoShort);
                            if (carrito.isEmpty()) url = "/FrontController";
                        } else {
                            // Manejar el caso donde el producto no está en el carrito
                            request.setAttribute("error", "El producto no existe en el carrito.");
                        }

                    } catch (NumberFormatException e) {
                        // Manejar el caso donde el ID del producto no es un número válido
                        request.setAttribute("error", "ID de producto inválido.");
                    }
                }
                break;

            case "checkout":

                break;

        }

        // Actualizar el carrito en la sesión
        session.setAttribute("carrito", carrito);

        // Actualizar la cookie del carrito
        Utils.actualizarCookie(response, carrito);

        // Redirigir internamente al FrontController
        request.getRequestDispatcher(url).forward(request, response);

    }

}
