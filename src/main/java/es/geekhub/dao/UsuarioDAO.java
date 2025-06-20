package es.geekhub.dao;

import es.geekhub.beans.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementación de la interfaz {@link IUsuarioDAO} para gestionar las
 * operaciones relacionadas con los usuarios en la base de datos.
 *
 * <p>
 * Permite realizar operaciones como registrar usuarios, verificar existencia de
 * correos, autenticación de usuarios y actualización de información de
 * perfil.</p>
 *
 * @author agp00
 */
public class UsuarioDAO implements IUsuarioDAO {

    @Override
    public void closeConnection() {
        ConnectionFactory.closeConnection();
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param usuario Objeto {@link Usuario} con los datos del usuario a
     * registrar.
     * @return El ID del usuario generado, o null si ocurre un error.
     */
    @Override
    public Short registrarUsuario(Usuario usuario) {
        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet generatedKeys = null;

        System.out.println("Avatar: " + usuario.getAvatar());

        String sql = "INSERT INTO usuarios (email, password, nombre, apellidos, nif, telefono, direccion, "
                + "codigoPostal, localidad, provincia, avatar, ultimoAcceso) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Short idUsuario = null;

        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            preparada = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            preparada.setString(1, usuario.getEmail());
            preparada.setString(2, usuario.getPassword());
            preparada.setString(3, usuario.getNombre());
            preparada.setString(4, usuario.getApellidos());
            preparada.setString(5, usuario.getNif());
            preparada.setString(6, usuario.getTelefono());
            preparada.setString(7, usuario.getDireccion());
            preparada.setString(8, usuario.getCodigoPostal());
            preparada.setString(9, usuario.getLocalidad());
            preparada.setString(10, usuario.getProvincia());
            preparada.setString(11, usuario.getAvatar() == null || usuario.getAvatar().isEmpty() ? null : usuario.getAvatar());
            preparada.setTimestamp(12, usuario.getUltimoAcceso());

            int filasInsertadas = preparada.executeUpdate();

            if (filasInsertadas > 0) {
                generatedKeys = preparada.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idUsuario = generatedKeys.getShort(1); 
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

        return idUsuario;
    }

    /**
     * Verifica si un email ya existe en la base de datos.
     *
     * @param email el email a verificar
     * @return true si el email existe, false si no existe
     */
    @Override
    public boolean existeEmail(String email) {
        Connection connection = null;
        PreparedStatement preparada = null;
        ResultSet resultado = null;

        String sql = "SELECT COUNT(*) AS count FROM usuarios WHERE Email = ?";

        boolean existe = false;

        try {
            connection = ConnectionFactory.getConnection();

            preparada = connection.prepareStatement(sql);
            preparada.setString(1, email);

            resultado = preparada.executeQuery();

            if (resultado.next()) {
                existe = resultado.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeConnection();
        }
        return existe;
    }

    /**
     * Realiza la autenticación de un usuario y actualiza su último acceso.
     *
     * <p>
     * Si las credenciales son válidas, devuelve el objeto completo del
     * usuario.</p>
     *
     * @param email El email del usuario.
     * @param password La contraseña del usuario.
     * @return El objeto {@link Usuario} si las credenciales son válidas, null
     * en caso contrario.
     * @throws SQLException Si ocurre un error durante la operación.
     */
    @Override
    public Usuario loginConTransaccion(String email, String password) throws SQLException {
        Connection connection = null;
        PreparedStatement validarCredencialesStmt = null;
        PreparedStatement obtenerUsuarioStmt = null;
        PreparedStatement actualizarUltimoAccesoStmt = null;
        ResultSet resultado = null;

        Usuario usuario = null;

        String sqlValidarCredenciales = "SELECT COUNT(*) AS count FROM usuarios WHERE email = ? AND password = ?";
        String sqlObtenerUsuario = "SELECT * FROM usuarios WHERE email = ?";
        String sqlActualizarUltimoAcceso = "UPDATE usuarios SET ultimoAcceso = CURRENT_TIMESTAMP WHERE email = ?";

        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            validarCredencialesStmt = connection.prepareStatement(sqlValidarCredenciales);
            validarCredencialesStmt.setString(1, email);
            validarCredencialesStmt.setString(2, password);
            resultado = validarCredencialesStmt.executeQuery();

            boolean credencialesValidas = false;
            if (resultado.next()) {
                credencialesValidas = resultado.getInt("count") > 0;
            }

            if (credencialesValidas) {
                obtenerUsuarioStmt = connection.prepareStatement(sqlObtenerUsuario);
                obtenerUsuarioStmt.setString(1, email);
                resultado = obtenerUsuarioStmt.executeQuery();

                if (resultado.next()) {
                    usuario = new Usuario();
                    usuario.setIdUsuario(resultado.getShort("idUsuario"));
                    usuario.setNombre(resultado.getString("Nombre"));
                    usuario.setApellidos(resultado.getString("Apellidos"));
                    usuario.setEmail(resultado.getString("email"));
                    usuario.setPassword(resultado.getString("password"));
                    usuario.setNif(resultado.getString("nif"));
                    usuario.setTelefono(resultado.getString("telefono"));
                    usuario.setDireccion(resultado.getString("direccion"));
                    usuario.setCodigoPostal(resultado.getString("codigopostal"));
                    usuario.setLocalidad(resultado.getString("localidad"));
                    usuario.setProvincia(resultado.getString("provincia"));
                    usuario.setUltimoAcceso(resultado.getTimestamp("ultimoAcceso"));
                    usuario.setAvatar(resultado.getString("avatar"));

                }

                actualizarUltimoAccesoStmt = connection.prepareStatement(sqlActualizarUltimoAcceso);
                actualizarUltimoAccesoStmt.setString(1, email);
                actualizarUltimoAccesoStmt.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            this.closeConnection();
        }
        return usuario;
    }

    /**
     * Actualiza la información de un usuario en la base de datos.
     *
     * @param usuario El objeto {@link Usuario} con los datos actualizados.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        Connection connection = null;
        PreparedStatement preparada = null;

        String sql = "UPDATE usuarios SET password = ?, nombre = ?, apellidos = ?, telefono = ?, direccion = ?, "
                + "codigoPostal = ?, localidad = ?, provincia = ?, avatar = ? WHERE idUsuario = ?";

        boolean actualizado = false;

        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            preparada = connection.prepareStatement(sql);

            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                preparada.setString(1, usuario.getPassword());
            } else {
                preparada.setNull(1, java.sql.Types.VARCHAR);
            }

            preparada.setString(2, usuario.getNombre());
            preparada.setString(3, usuario.getApellidos());
            preparada.setString(4, usuario.getTelefono());
            preparada.setString(5, usuario.getDireccion());
            preparada.setString(6, usuario.getCodigoPostal());
            preparada.setString(7, usuario.getLocalidad());
            preparada.setString(8, usuario.getProvincia());

            if (usuario.getAvatar() == null || usuario.getAvatar().isEmpty()) {
                preparada.setNull(9, java.sql.Types.VARCHAR);
            } else {
                preparada.setString(9, usuario.getAvatar());
            }

            preparada.setShort(10, usuario.getIdUsuario());

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

}
