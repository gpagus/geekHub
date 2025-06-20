package es.geekhub.models;

import com.google.gson.Gson;
import es.geekhub.beans.LineaPedido;
import es.geekhub.beans.Pedido;
import es.geekhub.beans.Producto;
import es.geekhub.beans.Usuario;
import es.geekhub.dao.IPedidoDAO;
import es.geekhub.dao.IProductoDAO;
import es.geekhub.daofactory.DAOFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

/**
 * Clase de utilidades para operaciones comunes en la aplicación.
 *
 * <p>
 * Proporciona métodos para encriptación, gestión de pedidos, sincronización del
 * carrito de compras y cálculo de importes, entre otros.</p>
 *
 * <p>
 * Incluye funcionalidades para la conversión de objetos a JSON y validaciones
 * específicas.</p>
 *
 * @author agp00
 */
public class Utils {

    /**
     * Encripta una cadena utilizando el algoritmo MD5.
     *
     * @param password La cadena a encriptar.
     * @return La cadena encriptada en formato hexadecimal.
     */
    public static String encriptarMD5(String password) {
        try {
            // Crear instancia de MessageDigest con el algoritmo MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // Convertir la contraseña en un array de bytes y aplicar el hash
            byte[] hash = md.digest(password.getBytes());

            // Convertir los bytes del hash a formato hexadecimal
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar el hash MD5", e);
        }
    }

    /**
     * Obtiene el pedido de la sesión del usuario. Si no existe, lo crea y lo
     * inicializa.
     *
     * @param session La sesión del usuario.
     * @param daof La fábrica de DAOs para acceder a la base de datos.
     * @return Un objeto {@link Pedido}.
     */
    public static Pedido obtenerPedidoDeSesion(HttpSession session, DAOFactory daof) {
        Pedido pedido = (Pedido) session.getAttribute("pedido");
        if (pedido == null) {
            Usuario usuario = (Usuario) session.getAttribute("usuario"); // Intento recoger el pedido de la sesión
            IPedidoDAO daoPedido = daof.getPedidoDAO();
            if (usuario != null) {
                pedido = daoPedido.obtenerPedidoCarrito(usuario); // Si no, Intento recoger el pedido de la bbdd
            }

            if (pedido == null) { // Si tampoco hay, creamos uno y lo insertamos en la bbdd
                pedido = new Pedido();
                List<LineaPedido> lineasPedidos = new ArrayList();
                pedido.setEstado(Pedido.Estado.c);
                pedido.setFecha(new Date());
                pedido.setImporte(0.0);
                pedido.setIva(0.0);
                pedido.setLineasPedidos(lineasPedidos);

                if (usuario != null) {
                    pedido.setUsuario(usuario);
                    int idPedido = daoPedido.crearPedido(pedido);
                    pedido.setIdPedido((short) idPedido);
                }

            }
        }
        return pedido;
    }

    /**
     * Sincroniza las líneas de un pedido con el contenido del carrito de
     * compras.
     *
     * @param pedido El pedido a sincronizar.
     * @param carrito Un mapa con los productos y sus cantidades.
     */
    public static void sincronizarPedidoConCarrito(Pedido pedido, Map<Short, Integer> carrito) {
        DAOFactory daof = new DAOFactory();
        IProductoDAO daop = daof.getProductoDAO();

        // Crear un mapa para las líneas de pedido existentes por ID de producto
        Map<Short, LineaPedido> lineasExistentes = new HashMap<>();
        for (LineaPedido linea : pedido.getLineasPedidos()) {
            lineasExistentes.put(linea.getProducto().getIdProducto(), linea);
        }

        for (Map.Entry<Short, Integer> entry : carrito.entrySet()) {
            Short idProducto = entry.getKey();
            Integer cantidadEnCarrito = entry.getValue();

            // Verificar si ya existe una línea de pedido para este producto
            if (lineasExistentes.containsKey(idProducto)) {
                // Actualizar la cantidad de la línea existente
                LineaPedido lineaExistente = lineasExistentes.get(idProducto);
                lineaExistente.setCantidad(cantidadEnCarrito.byteValue());
            } else {
                // Crear una nueva línea de pedido si no existe
                Producto producto = daop.getProductoById(idProducto);

                LineaPedido nuevaLinea = new LineaPedido();
                nuevaLinea.setProducto(producto);
                nuevaLinea.setCantidad(cantidadEnCarrito.byteValue());
                nuevaLinea.setPedido(pedido);

                pedido.getLineasPedidos().add(nuevaLinea);
            }
        }
    }

    /**
     * Calcula el importe total de las líneas de pedido.
     *
     * @param lineasPedidos Lista de líneas de pedido.
     * @return El importe total calculado.
     */
    public static double calcularImporteTotal(List<LineaPedido> lineasPedidos) {
        double importeTotal = 0;
        for (LineaPedido linea : lineasPedidos) {
            importeTotal += linea.getProducto().getPrecio() * linea.getCantidad();
        }
        return importeTotal;
    }

    /**
     * Calcula la letra del DNI basado en los números proporcionados.
     *
     * @param numeros Los números del DNI.
     * @return La letra correspondiente.
     */
    public static String calcularLetraDNI(int numeros) {
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";
        int resto = numeros % 23;
        return String.valueOf(letras.charAt(resto));
    }

    /**
     * Convierte las líneas de un pedido a formato JSON.
     *
     * @param pedido El pedido a convertir.
     * @return Una cadena JSON representando las líneas del pedido.
     */
    public static String convertirPedidoAJSON(Pedido pedido) {
        Gson gson = new Gson(); // Usar la librería Gson para manejar JSON
        return gson.toJson(pedido.getLineasPedidos());
    }

}
