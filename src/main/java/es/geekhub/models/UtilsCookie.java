package es.geekhub.models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Utilidad para la gestión de cookies relacionadas con el carrito de compras.
 *
 * <p>
 * Proporciona métodos para convertir el contenido del carrito a un formato
 * compatible con cookies, procesar cookies existentes para recuperar el
 * carrito, y actualizar o cargar el carrito en las cookies del cliente.</p>
 *
 * @author agp00
 */
public class UtilsCookie {

    /**
     * Convierte el contenido del carrito en una cadena codificada para ser
     * almacenada en una cookie.
     *
     * @param carrito Un mapa con el ID del producto como clave y la cantidad
     * como valor.
     * @return Una cadena codificada con los datos del carrito.
     */
    public static String convertirCarritoACookie(Map<Short, Integer> carrito) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<Short, Integer> entry : carrito.entrySet()) {
            sb.append(entry.getKey()).append(":")
                    .append(entry.getValue()).append(";");
        }

        try {
            return URLEncoder.encode(sb.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Procesa el valor de una cookie y convierte su contenido en un mapa del
     * carrito.
     *
     * @param cookieValue El valor de la cookie codificada.
     * @return Un mapa con el ID del producto como clave y la cantidad como
     * valor.
     */
    public static Map<Short, Integer> procesarCookie(String cookieValue) {
        Map<Short, Integer> carrito = new HashMap<>();

        try {
            String decodedValue = URLDecoder.decode(cookieValue, "UTF-8");
            String[] productos = decodedValue.split(";");

            for (String prod : productos) {
                String[] detalles = prod.split(":");
                if (detalles.length == 2) {
                    short idProducto = Short.parseShort(detalles[0]);
                    int cantidad = Integer.parseInt(detalles[1]);
                    carrito.put(idProducto, cantidad);
                }
            }
        } catch (UnsupportedEncodingException | NumberFormatException e) {
            e.printStackTrace();
        }

        return carrito;
    }

    /**
     * Actualiza la cookie del carrito con el contenido actual.
     *
     * @param response El objeto {@link HttpServletResponse} para agregar la
     * cookie.
     * @param carrito Un mapa con el ID del producto como clave y la cantidad
     * como valor.
     */
    public static void actualizarCookie(HttpServletResponse response, Map<Short, Integer> carrito) {
        String carritoStr = convertirCarritoACookie(carrito); // Convierte el carrito a string
        Cookie cookieCarrito = new Cookie("carrito", carritoStr);
        cookieCarrito.setMaxAge(60 * 60 * 24 * 2); // 2 días
        cookieCarrito.setPath("/"); // Disponible en toda la aplicación
        response.addCookie(cookieCarrito); // Establece la cookie
    }

    /**
     * Carga el carrito desde las cookies del cliente.
     *
     * @param cookies Un array de cookies del cliente.
     * @return Un mapa con el ID del producto como clave y la cantidad como
     * valor.
     */
    public static Map<Short, Integer> cargarCarritoDesdeCookie(Cookie[] cookies) {
        Map<Short, Integer> carrito = new HashMap<>();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("carrito".equals(cookie.getName())) {
                    // Cargar carrito desde la cookie
                    carrito = UtilsCookie.procesarCookie(cookie.getValue());
                    break;
                }
            }
        }

        return carrito; // Asegura que siempre retorne un mapa, nunca null
    }

}
