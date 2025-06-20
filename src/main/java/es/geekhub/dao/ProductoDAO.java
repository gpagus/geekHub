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

/**
 * Implementación de la interfaz {@link IProductoDAO} para gestionar las
 * operaciones relacionadas con los productos en la base de datos.
 *
 * <p>
 * Permite realizar operaciones como obtener productos aleatorios, buscar
 * productos por filtros, y recuperar detalles específicos de productos.</p>
 *
 * @author agp00
 */
public class ProductoDAO implements IProductoDAO {

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }

    /**
     * Obtiene una lista de productos aleatorios, limitada a 8 resultados.
     *
     * @return Una lista de productos aleatorios.
     */
    @Override
    public List<Producto> productosAleatorios() {
        List<Producto> productos = new ArrayList<>();
        Connection connection = null;
        ResultSet resultado = null;
        PreparedStatement preparada = null;

        // Consulta SQL ajustada para incluir descripción y marca
        String sql = "SELECT p.IdProducto, p.Nombre, p.Descripcion, p.Marca, p.Precio, p.Imagen, "
                + "c.IdCategoria, c.Nombre AS CategoriaNombre, c.Imagen AS CategoriaImagen "
                + "FROM productos p "
                + "JOIN categorias c ON p.IdCategoria = c.IdCategoria "
                + "ORDER BY RAND() LIMIT 8";

        try {
            connection = ConnectionFactory.getConnection();

            preparada = connection.prepareStatement(sql);

            resultado = preparada.executeQuery();

            while (resultado.next()) {
                Producto producto = new Producto();

                producto.setIdProducto(resultado.getShort("IdProducto"));
                producto.setNombre(resultado.getString("Nombre"));
                producto.setDescripcion(resultado.getString("Descripcion"));
                producto.setPrecio(resultado.getDouble("Precio"));
                producto.setImagen(resultado.getString("Imagen"));

                // Datos de la categoría
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(resultado.getByte("IdCategoria"));
                categoria.setNombre(resultado.getString("CategoriaNombre"));
                categoria.setImagen(resultado.getString("CategoriaImagen"));

                producto.setCategoria(categoria);

                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return productos;
    }

    /**
     * Obtiene una lista de marcas disponibles en la base de datos.
     *
     * @return Una lista de marcas únicas.
     */
    @Override
    public List<String> obtenerMarcasDisponibles() {
        List<String> marcas = new ArrayList<>();
        String sql = "SELECT DISTINCT marca FROM productos";
        Connection connection = null;
        ResultSet resultado = null;
        PreparedStatement preparada = null;

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            resultado = preparada.executeQuery();

            while (resultado.next()) {
                marcas.add(resultado.getString("marca"));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            this.closeConnection();
        }

        return marcas;
    }

    /**
     * Obtiene una lista de productos que cumplen con los filtros
     * proporcionados.
     *
     * @param filtros Objeto que contiene las categorías, marcas y rango de
     * precios a filtrar.
     * @return Una lista de productos que coinciden con los filtros.
     */
    @Override
    public List<Producto> obtenerProductosPorFiltros(Filtros filtros) {
        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;
        List<Producto> productos = new ArrayList<>();

        String sql = "SELECT p.*, c.Nombre AS CategoriaNombre, c.Imagen AS CategoriaImagen "
                + "FROM productos p "
                + "JOIN categorias c ON p.idCategoria = c.idCategoria "
                + "WHERE 1=1";
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
            sql += " AND p.idCategoria IN (" + placeholders.toString() + ")";
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
            sql += " AND p.marca IN (" + placeholders.toString() + ")";
            parametros.addAll(filtros.getMarcas());
        }

        // Agregar filtro de rango de precios
        if (filtros.getPrecioMin() != null && filtros.getPrecioMax() != null) {
            sql += " AND p.precio BETWEEN ? AND ?";
            parametros.add(filtros.getPrecioMin());
            parametros.add(filtros.getPrecioMax());
        }

        try {
            connection = ConnectionFactory.getConnection();
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
                categoria.setNombre(resultado.getString("CategoriaNombre"));
                categoria.setImagen(resultado.getString("CategoriaImagen"));
                producto.setCategoria(categoria);

                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return productos;
    }

    /**
     * Busca productos por nombre o descripción que coincidan con el término de
     * búsqueda.
     *
     * @param query El término de búsqueda.
     * @return Una lista de productos que coinciden con el término de búsqueda.
     */
    @Override
    public List<Producto> buscarProductos(String query) {
        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        List<Producto> productos = new ArrayList<>();

        String sql
                = "SELECT "
                + "p.idProducto, p.nombre, p.descripcion, p.precio, p.marca, p.imagen, "
                + "c.idCategoria, c.nombre AS categoriaNombre, c.imagen AS categoriaImagen "
                + "FROM productos p "
                + "JOIN categorias c ON p.idCategoria = c.idCategoria "
                + "WHERE p.nombre LIKE ? OR p.descripcion LIKE ?";

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);

            // Preparar el parámetro con comodines para la búsqueda
            String searchQuery = "%" + query + "%";
            preparada.setString(1, searchQuery);
            preparada.setString(2, searchQuery);

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
                categoria.setNombre(resultado.getString("categoriaNombre"));
                categoria.setImagen(resultado.getString("categoriaImagen"));

                producto.setCategoria(categoria);

                productos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return productos;
    }

    /**
     * Obtiene los detalles de un producto específico por su ID.
     *
     * @param idProducto El ID del producto a buscar.
     * @return Un objeto {@link Producto} con los detalles del producto, o null
     * si no se encuentra.
     */
    @Override
    public Producto getProductoById(Short idProducto) {
        Producto producto = null;
        String sql = "SELECT idProducto, nombre, precio, marca, imagen FROM productos WHERE idProducto = ?";
        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        try {
            connection = ConnectionFactory.getConnection();

            preparada = connection.prepareStatement(sql);
            preparada.setShort(1, idProducto);

            resultado = preparada.executeQuery();

            if (resultado.next()) {
                producto = new Producto();
                producto.setIdProducto(resultado.getShort("idProducto"));
                producto.setNombre(resultado.getString("nombre"));
                producto.setPrecio(resultado.getDouble("precio"));
                producto.setMarca(resultado.getString("marca"));
                producto.setImagen(resultado.getString("imagen"));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }

        return producto;
    }

}
