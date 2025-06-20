package es.geekhub.dao;

import es.geekhub.beans.Pedido;
import es.geekhub.beans.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la interfaz {@link IPedidoDAO} para gestionar operaciones
 * relacionadas con los pedidos en la base de datos.
 *
 * <p>
 * Permite realizar operaciones CRUD (crear, leer, actualizar, eliminar) sobre
 * pedidos, así como gestionar su estado (carrito o finalizado).</p>
 *
 * @author agp00
 */
public class PedidoDAO implements IPedidoDAO {

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }

    /**
     * Obtiene el pedido en estado de carrito asociado a un usuario específico.
     *
     * @param usuario El usuario al que pertenece el pedido.
     * @return El pedido en estado de carrito, o null si no existe.
     */
    @Override
    public Pedido obtenerPedidoCarrito(Usuario usuario) {
        Pedido pedido = null;
        String sql = "SELECT p.IdPedido, p.Fecha, p.Estado, p.IdUsuario, p.Importe, p.Iva "
                + "FROM pedidos p "
                + "JOIN usuarios u ON p.IdUsuario = u.IdUsuario "
                + "WHERE u.email = ? AND p.Estado = 'c';";

        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            preparada.setString(1, usuario.getEmail());

            resultado = preparada.executeQuery();

            if (resultado.next()) {
                pedido = new Pedido();
                pedido.setIdPedido(resultado.getShort("IdPedido"));
                pedido.setFecha(resultado.getDate("Fecha"));
                pedido.setEstado(Pedido.Estado.valueOf(resultado.getString("Estado")));
                pedido.setUsuario(usuario);
                pedido.setImporte(resultado.getDouble("Importe"));
                pedido.setIva(resultado.getDouble("Iva"));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            this.closeConnection();
        }
        return pedido;
    }

    /**
     * Crea un nuevo pedido en la base de datos.
     *
     * @param pedido El pedido a registrar.
     * @return El ID del pedido generado, o -1 si ocurre un error.
     */
    @Override
    public int crearPedido(Pedido pedido) {
        String sql = "INSERT INTO pedidos (Fecha, Estado, IdUsuario, Importe, Iva) VALUES (?, ?, ?, ?, ?)";
        Connection connection = null;
        PreparedStatement preparada = null;
        int idPedido = -1;

        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            preparada = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Asignar valores
            preparada.setDate(1, new java.sql.Date(pedido.getFecha().getTime()));
            preparada.setString(2, String.valueOf(pedido.getEstado()));
            preparada.setShort(3, pedido.getUsuario().getIdUsuario());
            preparada.setDouble(4, pedido.getImporte());
            preparada.setDouble(5, pedido.getIva());

            int filas = preparada.executeUpdate();

            if (filas > 0) {
                ResultSet generatedKeys = preparada.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idPedido = generatedKeys.getInt(1); 
                }
                connection.commit();
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
        return idPedido;
    }

    /**
     * Actualiza los detalles de un pedido existente en la base de datos.
     *
     * @param pedido El pedido con los datos actualizados.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    @Override
    public boolean actualizarPedido(Pedido pedido) {
        String sql = "UPDATE pedidos SET Importe = ?, Iva = ? WHERE IdPedido = ?";
        Connection connection = null;
        PreparedStatement preparada = null;
        boolean actualizado = false;

        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            preparada = connection.prepareStatement(sql);

            // Asignar valores
            preparada.setDouble(1, pedido.getImporte());
            preparada.setDouble(2, pedido.getIva());
            preparada.setShort(3, pedido.getIdPedido());

            int filasActualizadas = preparada.executeUpdate();

            if (filasActualizadas > 0) {
                connection.commit();
                actualizado = true;
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
        return actualizado;
    }

    /**
     * Elimina un pedido de la base de datos.
     *
     * @param idPedido El ID del pedido a eliminar.
     */
    @Override
    public void eliminarPedido(Short idPedido) {
        String sql = "DELETE FROM pedidos WHERE IdPedido = ?";
        Connection connection = null;
        PreparedStatement preparada = null;

        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            preparada = connection.prepareStatement(sql);
            preparada.setShort(1, idPedido);

            int filasAfectadas = preparada.executeUpdate();

            if (filasAfectadas > 0) {
                connection.commit();
                System.out.println("Pedido eliminado correctamente.");
            } else {
                connection.rollback();
                System.out.println("No se encontró el pedido para eliminar.");
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
            closeConnection();
        }
    }

    /**
     * Finaliza un pedido cambiando su estado a "finalizado".
     *
     * @param idPedido El ID del pedido a finalizar.
     */
    @Override
    public void finalizarPedido(Short idPedido) {
        String sql = "UPDATE pedidos SET estado = 'f' WHERE IdPedido = ?";
        Connection connection = null;
        PreparedStatement preparada = null;

        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            preparada = connection.prepareStatement(sql);
            preparada.setShort(1, idPedido);

            int filasAfectadas = preparada.executeUpdate();

            if (filasAfectadas > 0) {
                connection.commit();
                System.out.println("Pedido finalizado correctamente.");
            } else {
                connection.rollback();
                System.out.println("No se encontró el pedido para finalizar.");
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
            closeConnection();
        }
    }

    /**
     * Obtiene una lista de pedidos finalizados asociados a un usuario.
     *
     * @param usuario El usuario al que pertenecen los pedidos.
     * @return Una lista de pedidos finalizados ordenados por fecha descendente.
     */
    @Override
    public List<Pedido> getPedidosFinalizadosPorUsuario(Usuario usuario) {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE IdUsuario = ? AND estado = 'f' ORDER BY fecha DESC";
        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            preparada.setShort(1, usuario.getIdUsuario());

            resultado = preparada.executeQuery();

            while (resultado.next()) {
                Pedido pedido = new Pedido();

                pedido.setIdPedido(resultado.getShort("IdPedido"));
                pedido.setFecha(resultado.getDate("Fecha"));
                pedido.setEstado(Pedido.Estado.valueOf(resultado.getString("Estado")));
                pedido.setUsuario(usuario);
                pedido.setImporte(resultado.getDouble("Importe"));
                pedido.setIva(resultado.getDouble("Iva"));

                pedidos.add(pedido);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();

        }

        return pedidos;
    }

}
