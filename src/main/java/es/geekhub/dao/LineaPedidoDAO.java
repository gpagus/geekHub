package es.geekhub.dao;

import es.geekhub.beans.LineaPedido;
import es.geekhub.beans.Pedido;
import es.geekhub.beans.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz {@link ILineaPedidoDAO} para gestionar
 * operaciones relacionadas con las líneas de pedido en la base de datos.
 *
 * <p>
 * Permite realizar operaciones CRUD (crear, leer, actualizar, eliminar) sobre
 * las líneas de pedido, además de obtener todas las líneas asociadas a un
 * pedido específico.</p>
 *
 * @author agp00
 */
public class LineaPedidoDAO implements ILineaPedidoDAO {

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }

    /**
     * Obtiene una línea de pedido específica basada en el ID del pedido y el ID
     * del producto.
     *
     * @param idPedido ID del pedido al que pertenece la línea.
     * @param idProducto ID del producto asociado a la línea.
     * @return La línea de pedido encontrada, o null si no existe.
     */
    @Override
    public LineaPedido obtenerLineaPedido(Short idPedido, Short idProducto) {
        LineaPedido linea = null;
        String sql = "SELECT lp.IdLinea, lp.IdPedido, lp.IdProducto, lp.Cantidad, "
                + "p.Nombre AS ProductoNombre, p.Precio AS ProductoPrecio, "
                + "p.Marca AS ProductoMarca, p.Imagen AS ProductoImagen "
                + "FROM lineaspedidos lp "
                + "JOIN productos p ON lp.IdProducto = p.IdProducto "
                + "WHERE lp.IdPedido = ? AND lp.IdProducto = ?";
        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);

            preparada.setShort(1, idPedido);
            preparada.setShort(2, idProducto);

            resultado = preparada.executeQuery();

            if (resultado.next()) {
                // Construir la línea de pedido
                linea = new LineaPedido();
                linea.setIdLinea(resultado.getShort("IdLinea"));
                linea.setCantidad(resultado.getByte("Cantidad"));

                // Construir el pedido asociado
                Pedido pedido = new Pedido();
                pedido.setIdPedido(resultado.getShort("IdPedido"));
                linea.setPedido(pedido);

                // Construir el producto asociado
                Producto producto = new Producto();
                producto.setIdProducto(resultado.getShort("IdProducto"));
                producto.setNombre(resultado.getString("ProductoNombre"));
                producto.setPrecio(resultado.getDouble("ProductoPrecio"));
                producto.setMarca(resultado.getString("ProductoMarca"));
                producto.setImagen(resultado.getString("ProductoImagen"));
                linea.setProducto(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return linea;
    }

    /**
     * Guarda una nueva línea de pedido en la base de datos.
     *
     * @param lineaPedido La línea de pedido a guardar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    @Override
    public boolean guardarLineaPedido(LineaPedido lineaPedido) {
        String sql = "INSERT INTO lineaspedidos (IdPedido, IdProducto, Cantidad) VALUES (?, ?, ?)";
        Connection connection = null;
        PreparedStatement preparada = null;
        boolean resultado = false;

        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            preparada = connection.prepareStatement(sql);

            preparada.setShort(1, lineaPedido.getPedido().getIdPedido());
            preparada.setShort(2, lineaPedido.getProducto().getIdProducto());
            preparada.setByte(3, lineaPedido.getCantidad());

            int filasAfectadas = preparada.executeUpdate();

            if (filasAfectadas > 0) {
                connection.commit();
                resultado = true;
            } else {
                connection.rollback();
            }

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return resultado;
    }

    /**
     * Actualiza una línea de pedido existente en la base de datos.
     *
     * @param lineaPedido La línea de pedido con los nuevos valores.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    @Override
    public boolean actualizarLineaPedido(LineaPedido lineaPedido) {
        String sql = "UPDATE lineaspedidos SET Cantidad = ? WHERE IdPedido = ? AND IdProducto = ?";
        Connection connection = null;
        PreparedStatement preparada = null;
        boolean cambio = false;

        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            preparada = connection.prepareStatement(sql);

            preparada.setShort(1, lineaPedido.getCantidad());
            preparada.setShort(2, lineaPedido.getPedido().getIdPedido());
            preparada.setShort(3, lineaPedido.getProducto().getIdProducto());

            int filasActualizadas = preparada.executeUpdate();

            if (filasActualizadas > 0) {
                connection.commit();
                System.out.println("La línea de pedido se actualizó correctamente.");
                cambio = true;
            } else {
                connection.rollback();
                System.out.println("No se encontró la línea de pedido para actualizar.");
            }

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback(); // Deshacer la transacción en caso de error
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return cambio;
    }

    /**
     * Obtiene todas las líneas de pedido asociadas a un pedido específico.
     *
     * @param idPedido ID del pedido.
     * @return Lista de líneas de pedido asociadas al pedido.
     */
    @Override
    public List<LineaPedido> getLineasPedidoByIdPedido(Short idPedido) {
        List<LineaPedido> lineasPedido = new ArrayList<>();
        String sql = "SELECT lp.IdLinea, lp.IdPedido, lp.IdProducto, lp.Cantidad, "
                + "p.Nombre AS ProductoNombre, p.Precio AS ProductoPrecio, "
                + "p.Marca AS ProductoMarca, p.Imagen AS ProductoImagen "
                + "FROM lineaspedidos lp "
                + "JOIN productos p ON lp.IdProducto = p.IdProducto "
                + "WHERE lp.IdPedido = ?";
        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            preparada.setShort(1, idPedido);

            resultado = preparada.executeQuery();

            while (resultado.next()) {
                LineaPedido linea = new LineaPedido();

                // Datos de la línea de pedido
                linea.setIdLinea(resultado.getShort("IdLinea"));
                linea.setCantidad(resultado.getByte("Cantidad"));

                // Datos del pedido asociado
                Pedido pedido = new Pedido();
                pedido.setIdPedido(resultado.getShort("IdPedido"));
                linea.setPedido(pedido);

                // Datos del producto asociado
                Producto producto = new Producto();
                producto.setIdProducto(resultado.getShort("IdProducto"));
                producto.setNombre(resultado.getString("ProductoNombre"));
                producto.setPrecio(resultado.getDouble("ProductoPrecio"));
                producto.setMarca(resultado.getString("ProductoMarca"));
                producto.setImagen(resultado.getString("ProductoImagen"));
                linea.setProducto(producto);

                lineasPedido.add(linea);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return lineasPedido;
    }

    /**
     * Elimina una línea de pedido específica basada en el ID del pedido y el ID
     * del producto.
     *
     * @param idPedido ID del pedido al que pertenece la línea.
     * @param idProducto ID del producto asociado a la línea.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    @Override
    public boolean eliminarLineaPedido(Short idPedido, Short idProducto) {
        String sql = "DELETE FROM lineaspedidos WHERE IdPedido = ? AND IdProducto = ?";
        Connection connection = null;
        PreparedStatement preparada = null;
        boolean resultado = false;

        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            preparada = connection.prepareStatement(sql);
            preparada.setShort(1, idPedido);
            preparada.setShort(2, idProducto);

            int filasAfectadas = preparada.executeUpdate();

            if (filasAfectadas > 0) {
                connection.commit();
                resultado = true;
            } else {
                connection.rollback();
            }

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return resultado;
    }

}
