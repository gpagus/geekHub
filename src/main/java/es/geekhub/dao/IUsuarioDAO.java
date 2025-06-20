package es.geekhub.dao;

import es.geekhub.beans.Usuario;
import java.sql.SQLException;

/**
 *
 * @author agp00
 */
public interface IUsuarioDAO {
    
    public void closeConnection();
    public Short registrarUsuario(Usuario usuario);
    public boolean existeEmail(String email);
    public Usuario loginConTransaccion(String email, String password) throws SQLException;
    public boolean actualizarUsuario(Usuario usuario);
    
}
