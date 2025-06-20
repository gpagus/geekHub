package es.geekhub.eventos;

import es.geekhub.beans.Categoria;
import es.geekhub.dao.ICategoriaDAO;
import es.geekhub.dao.IProductoDAO;
import es.geekhub.daofactory.DAOFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

/**
 * Clase que inicializa la aplicación web configurando datos globales al inicio
 * y limpieza de recursos al finalizar.
 *
 * <p>
 * Esta clase implementa {@link ServletContextListener} y se encarga de cargar
 * categorías y marcas disponibles en el contexto de la aplicación al
 * arrancar.</p>
 *
 * <p>
 * Los datos se eliminan del contexto al finalizar la aplicación.</p>
 *
 */
@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        DAOFactory daof = new DAOFactory();
        ICategoriaDAO daoc = daof.getCategoriaDAO();
        IProductoDAO productoDAO = daof.getProductoDAO();

        List<Categoria> categorias = daoc.obtenerCategoriasConProductos();
        sce.getServletContext().setAttribute("categorias", categorias);

        List<String> marcas = productoDAO.obtenerMarcasDisponibles();
        sce.getServletContext().setAttribute("marcas", marcas);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("categorias");
        sce.getServletContext().removeAttribute("marcas");
    }

}
