package es.geekhub.controllers;

import com.google.gson.JsonObject;
import es.geekhub.dao.IUsuarioDAO;
import es.geekhub.daofactory.DAOFactory;
import es.geekhub.models.Utils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet que maneja peticiones AJAX para funcionalidades específicas.
 *
 * <p>
 * Las acciones disponibles son:
 * <ul>
 * <li><strong>verificarCorreo:</strong> Verifica si un correo electrónico ya
 * está registrado en la base de datos.</li>
 * <li><strong>calcularLetraNIF:</strong> Calcula la letra del NIF dado un
 * número de DNI.</li>
 * </ul>
 * </p>
 *
 * @author agp00
 */
@WebServlet(name = "Ajax", urlPatterns = {"/Ajax"})
public class Ajax extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject jsonResponse = new JsonObject();

        String accion = request.getParameter("accion");

        switch (accion) {
            case "verificarCorreo":
                String email = request.getParameter("correo");
                try {
                    DAOFactory daof = new DAOFactory();
                    IUsuarioDAO daou = daof.getUsuarioDAO();
                    boolean emailExiste = daou.existeEmail(email);

                    jsonResponse.addProperty("disponible", !emailExiste);
                } catch (Exception e) {
                    jsonResponse.addProperty("error", "Error al verificar el email");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                break;

            case "calcularLetraNIF":
                String numeros = request.getParameter("numeros");
                try {
                    int numerosDNI = Integer.parseInt(numeros);
                    String letra = Utils.calcularLetraDNI(numerosDNI);
                    jsonResponse.addProperty("letra", letra);
                } catch (NumberFormatException e) {
                    jsonResponse.addProperty("error", true);
                    jsonResponse.addProperty("mensaje", "Número de NIF inválido");
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
                break;
        }

        response.getWriter().write(jsonResponse.toString());
    }
}
