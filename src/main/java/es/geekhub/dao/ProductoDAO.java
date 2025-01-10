package es.geekhub.dao;

import es.geekhub.beans.Categoria;
import es.geekhub.beans.Filtros;
import es.geekhub.beans.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<String> obtenerMarcasDisponibles() {
        List<String> marcas = new ArrayList<>();
        String sql = "SELECT DISTINCT marca FROM productos";
        Connection connection = null;
        ResultSet resultado = null;
        PreparedStatement preparada = null;

        try {
            connection = ConnectionFactory.getConnectionMysql();
            preparada = connection.prepareStatement(sql);
            resultado = preparada.executeQuery();

            while (resultado.next()) {
                marcas.add(resultado.getString("marca"));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            closeConnection();
        }

        return marcas;
    }

    @Override
    public List<Producto> obtenerProductosPorFiltros(Filtros filtros) {
        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT * FROM productos WHERE 1=1";
        List<Object> parametros = new ArrayList<>();

        // Agregar filtro de categorías
        if (filtros.getCategorias() != null && !filtros.getCategorias().isEmpty()) {
            StringBuilder placeholders = new StringBuilder();
            for (int i = 0; i < filtros.getCategorias().size(); i++) {
                placeholders.append("?");
                if (i < filtros.getCategorias().size() - 1) {
                    placeholders.append(", ");
                }
            }
            sql += " AND idCategoria IN (" + placeholders.toString() + ")";
            parametros.addAll(filtros.getCategorias());
        }

        // Agregar filtro de marcas
        if (filtros.getMarcas() != null && !filtros.getMarcas().isEmpty()) {
            StringBuilder placeholders = new StringBuilder();
            for (int i = 0; i < filtros.getMarcas().size(); i++) {
                placeholders.append("?");
                if (i < filtros.getMarcas().size() - 1) {
                    placeholders.append(", ");
                }
            }
            sql += " AND marca IN (" + placeholders.toString() + ")";
            parametros.addAll(filtros.getMarcas());
        }

        // Agregar filtro de rango de precios
        if (filtros.getPrecioMin() != null && filtros.getPrecioMax() != null) {
            sql += " AND precio BETWEEN ? AND ?";
            parametros.add(filtros.getPrecioMin());
            parametros.add(filtros.getPrecioMax());
        }

        try {
            connection = ConnectionFactory.getConnectionMysql();
            preparada = connection.prepareStatement(sql);

            // Asignar parámetros dinámicamente
            for (int i = 0; i < parametros.size(); i++) {
                preparada.setObject(i + 1, parametros.get(i));
            }

            resultado = preparada.executeQuery();

            while (resultado.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(resultado.getShort("idProducto"));
                producto.setNombre(resultado.getString("nombre"));
                producto.setDescripcion(resultado.getString("descripcion"));
                producto.setPrecio(resultado.getDouble("precio"));
                producto.setMarca(resultado.getString("marca"));
                producto.setImagen(resultado.getString("imagen"));

                Categoria categoria = new Categoria();
                categoria.setIdCategoria(resultado.getByte("idCategoria"));
                producto.setCategoria(categoria);

                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(); // Asegúrate de cerrar la conexión, preparada y el resultSet.
        }

        return productos;
    }

}
