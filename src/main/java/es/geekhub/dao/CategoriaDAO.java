package es.geekhub.dao;

import es.geekhub.beans.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * <p>
 * Permite obtener categorías que tienen productos asociados y cierra conexiones
 * con la base de datos de forma segura.</p>
 *
 * @author agp00
 */
public class CategoriaDAO implements ICategoriaDAO {

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }

    /**
     * Obtiene una lista de categorías que tienen al menos un producto asociado.
     *
     * <p>
     * Realiza una consulta que combina las tablas de categorías y productos
     * para devolver solo aquellas categorías que están asociadas a
     * productos.</p>
     *
     * @return Una lista de categorías con productos asociados.
     */
    @Override
    public List<Categoria> obtenerCategoriasConProductos() {
        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT DISTINCT c.idCategoria, c.nombre, c.imagen "
                + "FROM categorias c "
                + "INNER JOIN productos p ON c.idCategoria = p.idCategoria";
        Connection connection = null;
        ResultSet resultado = null;
        PreparedStatement preparada = null;

        try {
            connection = ConnectionFactory.getConnection();
            preparada = connection.prepareStatement(sql);
            resultado = preparada.executeQuery();

            while (resultado.next()) {
                Categoria categoria = new Categoria();

                categoria.setIdCategoria(resultado.getByte("idCategoria"));
                categoria.setNombre(resultado.getString("nombre"));
                categoria.setImagen(resultado.getString("imagen"));

                categorias.add(categoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            closeConnection();
        }
        return categorias;
    }

}
