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
 * @author agp00
 */
public class CategoriaDAO implements ICategoriaDAO {
    
    @Override
    public void closeConnection() {
        ConnectionFactory.closeConexion();
    }
    
    @Override
    public List<Categoria> obtenerCategoriasConProductos() {
        List<Categoria> categorias = new ArrayList<>();
        
        String sql = "SELECT DISTINCT c.idCategoria, c.nombre, c.imagen " +
                 "FROM categorias c " +
                 "INNER JOIN productos p ON c.idCategoria = p.idCategoria";
        Connection connection = null;
        ResultSet resultado = null;
        PreparedStatement preparada = null;

        try {
            connection = ConnectionFactory.getConnectionMysql();
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
