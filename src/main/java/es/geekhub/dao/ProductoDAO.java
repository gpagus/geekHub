package es.geekhub.dao;

import es.geekhub.beans.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author agp00
 */
public class ProductoDAO implements IProductoDAO {

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConexion();
    }

    @Override
    public List<Producto> productosAleatorios() {
        ArrayList<Producto> productos = new ArrayList<>();
        Connection connection = null;
        ResultSet resultado = null;
        PreparedStatement preparada = null;
        String sql = "SELECT p.IdProducto, p.Nombre, p.Precio, p.Imagen "
                + "FROM productos p "
                + "ORDER BY RAND() LIMIT 8";

        try {
            // Obtener la conexión (asume que tienes un método para esto)
            connection = ConnectionFactory.getConnectionMysql();

            // Preparar la consulta
            preparada = connection.prepareStatement(sql);

            // Ejecutar la consulta
            resultado = preparada.executeQuery();

            // Procesar el resultado
            while (resultado.next()) {
                Producto producto = new Producto();
                
                producto.setNombre(resultado.getString("Nombre"));
                producto.setPrecio(resultado.getDouble("Precio"));
                producto.setImagen(resultado.getString("Imagen"));
                producto.setIdProducto(resultado.getShort("IdProducto"));

                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
      
        } finally {
            closeConnection();
        }

        return productos;
    }

}
