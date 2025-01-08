package es.geekhub.models;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class Utils {

    // Convierte el carrito en una cadena para la cookie (codificada)
    public static String convertirCarritoACookie(Map<Short, Integer> carrito) {
    StringBuilder sb = new StringBuilder();

    for (Map.Entry<Short, Integer> entry : carrito.entrySet()) {
        sb.append(entry.getKey()).append(":")
          .append(entry.getValue()).append(";");
    }

    try {
        return URLEncoder.encode(sb.toString(), "UTF-8"); // Codificar la cadena
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
        return ""; 
    }
}


    // Procesa la cookie y convierte su valor en un ArrayList de productos (decodificada)
    public static Map<Short, Integer> procesarCookie(String cookieValue) {
    Map<Short, Integer> carrito = new HashMap<>();

    try {
        String decodedValue = URLDecoder.decode(cookieValue, "UTF-8"); // Decodificar la cookie
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

    // Actualiza la cookie "carrito" con el contenido del Map de productos y cantidades.
    public static void actualizarCookie(HttpServletResponse response, Map<Short, Integer> carrito) {
    String carritoStr = convertirCarritoACookie(carrito); // Convierte el carrito a string
    Cookie cookieCarrito = new Cookie("carrito", carritoStr);
    cookieCarrito.setMaxAge(60 * 60 * 24 * 2); // 2 días
    cookieCarrito.setPath("/"); // Disponible en toda la aplicación
    response.addCookie(cookieCarrito); // Establece la cookie
}


    // Valida si la cantidad es un número válido.
    public static boolean validarCantidad(String cantidadStr) {
        try {
            int cantidad = Integer.parseInt(cantidadStr);
            return cantidad > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Carga el carrito desde la cookie "carrito".
    public static Map<Short, Integer> cargarCarritoDesdeCookie(Cookie[] cookies) {
    Map<Short, Integer> carrito = new HashMap<>();

    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("carrito".equals(cookie.getName())) {
                // Cargar carrito desde la cookie
                carrito = Utils.procesarCookie(cookie.getValue());
                break;
            }
        }
    }

    return carrito; // Asegura que siempre retorne un mapa, nunca null
}


}
