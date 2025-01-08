package es.geekhub.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionFactory {

    /*
    * Declaramos el pool de conexiones
     */
    static DataSource dataSource = null;
    static Connection conexion = null;
    static final String DATASOURCE_MYSQL = "java:comp/env/jdbc/geekHub";

    public static Connection getConnectionMysql() {

        try {
            /*
            * Para buscar y acceder a un recurso defnido en el Servidor de Aplicaciones
            *   - Creamos el contexto de búsqueda mediante la clase InitialContext.
            *   - Realizamos la búsqueda del recurso haciendo el casting correspondiente con la sentecia lookup
             */
            Context contextoInicial = new InitialContext();
            dataSource = (DataSource) contextoInicial.lookup(DATASOURCE_MYSQL);
            conexion = dataSource.getConnection();
        } catch (NamingException | SQLException ex) {
            /*
            * Existe un error al intentar crear el pool de conexiones. Escribimos el logger y se visualiza error500.jsp
             */
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);

        }
        return conexion;
    }

    public static void closeConexion() {
        try {
            conexion.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
