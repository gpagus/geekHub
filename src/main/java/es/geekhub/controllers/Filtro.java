package es.geekhub.controllers;

import es.geekhub.beans.Filtros;
import es.geekhub.beans.Producto;
import es.geekhub.dao.IProductoDAO;
import es.geekhub.daofactory.DAOFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet que gestiona el filtrado de productos basado en criterios
 * seleccionados por el usuario.
 *
 * <p>
 * Permite aplicar filtros como rango de precios, categorías y marcas para
 * personalizar la búsqueda de productos en la aplicación.</p>
 *
 * <strong>Funcionamiento:</strong>
 * <ul>
 * <li>Obtiene los parámetros enviados desde la vista (categorías, marcas, y
 * rango de precios).</li>
 * <li>Procesa y valida los parámetros seleccionados.</li>
 * <li>Consulta la base de datos para obtener los productos que cumplen con los
 * filtros.</li>
 * <li>Guarda los resultados en la sesión y los envía de vuelta a la vista.</li>
 * </ul>
 *
 * @author agp00
 */
@WebServlet(name = "Filtro", urlPatterns = {"/Filtro"})
public class Filtro extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String url = "/JSP/menu.jsp";
        Filtros filtros = new Filtros();

        // Obtener parámetros del filtro
        filtros.setPriceRange(request.getParameter("price"));
        String[] categoriasSeleccionadas = request.getParameterValues("categorias");
        String[] marcasSeleccionadas = request.getParameterValues("marcas");

        // Procesar categorías seleccionadas
        if (categoriasSeleccionadas != null) {
            List<Byte> idsCategoria = new ArrayList<>();
            for (String categoriaId : categoriasSeleccionadas) {
                try {
                    Byte categoriaIdByte = Byte.valueOf(categoriaId);
                    idsCategoria.add(categoriaIdByte);
                } catch (NumberFormatException e) {
                }
            }
            filtros.setCategorias(idsCategoria);
        }

        // Procesar marcas seleccionadas
        if (marcasSeleccionadas != null) {
            List<String> marcas = new ArrayList<>();
            marcas.addAll(Arrays.asList(marcasSeleccionadas));
            filtros.setMarcas(marcas);
        }

        // Obtener productos filtrados
        DAOFactory daof = new DAOFactory();
        IProductoDAO daop = daof.getProductoDAO();
        List<Producto> productos = daop.obtenerProductosPorFiltros(filtros);

        // Guardar resultados 
        request.getSession().setAttribute("productos", productos);

        request.setAttribute("filtrosSeleccionados", filtros);

        // Redirigir a la vista
        request.getRequestDispatcher(url).forward(request, response);
    }

}
