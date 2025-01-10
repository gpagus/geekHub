package es.geekhub.eventos;

import es.geekhub.beans.Categoria;
import es.geekhub.dao.ICategoriaDAO;
import es.geekhub.dao.IProductoDAO;
import es.geekhub.daofactory.DAOFactory;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

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
