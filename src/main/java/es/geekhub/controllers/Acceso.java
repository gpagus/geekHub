package es.geekhub.controllers;

import es.geekhub.beans.LineaPedido;
import es.geekhub.beans.Pedido;
import es.geekhub.beans.Usuario;
import es.geekhub.dao.ILineaPedidoDAO;
import es.geekhub.dao.IPedidoDAO;
import es.geekhub.dao.IUsuarioDAO;
import es.geekhub.daofactory.DAOFactory;
import es.geekhub.models.Utils;
import es.geekhub.models.UtilsCookie;
import es.geekhub.models.UtilsImagen;
import java.io.File;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * Servlet que gestiona las operaciones de registro, acceso y edición de perfil
 * de usuarios.
 *
 * <p>
 * Permite a los usuarios realizar las siguientes acciones:</p>
 * <ul>
 * <li><strong>registro:</strong> Registra un nuevo usuario, asigna un avatar, y
 * configura un pedido existente si corresponde.</li>
 * <li><strong>acceso:</strong> Permite el acceso de usuarios registrados con
 * validación de credenciales.</li>
 * <li><strong>editar:</strong> Permite a los usuarios actualizar su perfil,
 * incluyendo la contraseña y el avatar.</li>
 * </ul>
 *
 * <strong>Funciones adicionales:</strong>
 * <ul>
 * <li>Encriptación de contraseñas utilizando MD5.</li>
 * <li>Gestión de avatares con subida de archivos.</li>
 * <li>Actualización y sincronización del pedido del usuario.</li>
 * </ul>
 *
 * @author agp00
 */
@WebServlet(name = "Registro", urlPatterns = {"/Registro"})
@MultipartConfig
public class Acceso extends HttpServlet {

    private static final String UPLOAD_DIRECTORY = "/IMG/avatares";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/JSP/registro.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Usuario usuario = new Usuario();
        String url = "/JSP/menu.jsp";

        try {
            BeanUtils.populate(usuario, request.getParameterMap());
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new ServletException("Error al procesar el formulario", e);
        }

        switch (request.getParameter("accion")) {

            case "registro": {

                Part avatarPart = request.getPart("avatar");
                if (avatarPart != null && avatarPart.getSize() > 0) {
                    String fileName = UtilsImagen.generateUniqueFileName(avatarPart);

                    String uploadPath = getServletContext().getRealPath("") + UPLOAD_DIRECTORY;

                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdir();
                    }

                    String filePath = uploadPath + File.separator + fileName;
                    avatarPart.write(filePath);

                    usuario.setAvatar(fileName);
                } else {
                    usuario.setAvatar("default.jpg");
                }

                // Encriptamos la contraseña mediante md5
                usuario.setPassword(Utils.encriptarMD5(request.getParameter("password")));
                usuario.setNif(usuario.getNif() + request.getParameter("nifLetra"));
                // Obtener la fecha actual
                Timestamp ahora = new Timestamp(new Date().getTime());
                // Configurar el último acceso
                usuario.setUltimoAcceso(ahora);
                DAOFactory daof = new DAOFactory();
                IUsuarioDAO daou = daof.getUsuarioDAO();
                Short idUsuario = daou.registrarUsuario(usuario);

                if (idUsuario != null) { // Si el idUsuario no es nulo, el registro fue exitoso
                    usuario.setIdUsuario(idUsuario); // Asignar el ID generado al objeto usuario
                    session.setAttribute("usuario", usuario);

                    Pedido pedido = Utils.obtenerPedidoDeSesion(session, daof);

                    if (pedido != null && pedido.getLineasPedidos() != null && !pedido.getLineasPedidos().isEmpty()) {
                        pedido.setUsuario(usuario); // Asignar el usuario al pedido
                        

                        // Guardar o actualizar el pedido en la base de datos
                        IPedidoDAO daoPedido = daof.getPedidoDAO();
                        int idPedido = daoPedido.crearPedido(pedido);
                        pedido.setIdPedido((short) idPedido);

                        ILineaPedidoDAO daoLineaPedido = daof.getLineaPedidoDAO();
                        // Guardar o actualizar las líneas de pedido
                        for (LineaPedido linea : pedido.getLineasPedidos()) {
                            if (linea.getIdLinea() == null || linea.getIdLinea() == 0) {
                                linea.setPedido(pedido); // Asegurar la referencia al pedido
                                daoLineaPedido.guardarLineaPedido(linea); // Insertar la línea de pedido
                            } else {
                                daoLineaPedido.actualizarLineaPedido(linea); // Actualizar si ya existe
                            }
                        }

                        // Limpiar el carrito en la cookie
                        Map<Short, Integer> carritoVacio = new HashMap<>();
                        UtilsCookie.actualizarCookie(response, carritoVacio);
                    } else {
                        session.removeAttribute("pedido");
                        pedido = Utils.obtenerPedidoDeSesion(session, daof);
                        
                    }

                    session.setAttribute("pedido", pedido);
                    request.setAttribute("aviso", "Se ha registrado al usuario " + usuario.getNombre() + " correctamente");
                    request.setAttribute("tipoAviso", "success");

                } else { // Si idUsuario es null, hubo un error
                    request.setAttribute("aviso", "Se ha producido un error al intentar registrar al usuario");
                    request.setAttribute("tipoAviso", "error");
                }

                break;
            }

            case "acceso": {
                // Encriptamos la contraseña mediante md5
                usuario.setPassword(Utils.encriptarMD5(request.getParameter("password")));

                String email = usuario.getEmail();
                String password = usuario.getPassword();
                DAOFactory daof = new DAOFactory();
                IUsuarioDAO daou = daof.getUsuarioDAO();
                try {
                    // Obtener el usuario completo dentro de una transacción
                    Usuario usuarioCompleto = daou.loginConTransaccion(email, password);

                    if (usuarioCompleto != null) {
                        // Credenciales válidas, guardar el usuario completo en la sesión
                        session.setAttribute("usuario", usuarioCompleto);
                        session.removeAttribute("pedido");
                        request.setAttribute("aviso", "Bienvenido, " + usuarioCompleto.getNombre());
                        request.setAttribute("tipoAviso", "success");

                        SimpleDateFormat sdf = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy, HH:mm", new Locale("es", "ES"));
                        String ultimoAccesoFormateado = sdf.format(usuarioCompleto.getUltimoAcceso());

                        session.setAttribute("ultimoAccesoFormateado", ultimoAccesoFormateado);
                        url = "/Cesta";
                    } else {
                        // Credenciales inválidas, mostrar error
                        request.setAttribute("aviso", "Email o contraseña incorrectos");
                        request.setAttribute("tipoAviso", "error");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("aviso", "Error al intentar acceder: " + e.getMessage());
                    request.setAttribute("tipoAviso", "error");

                }
                break;
            }

            case "editar":
                url = "/JSP/perfil/editar.jsp";
                boolean error = false;
                boolean validarPassword = false;
                boolean passwordCambiada = false; 

                Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");

                usuario.setIdUsuario(usuarioSesion.getIdUsuario());
                usuario.setUltimoAcceso(usuarioSesion.getUltimoAcceso());

                String currentPassword = request.getParameter("currentPassword");
                if (!currentPassword.trim().isEmpty()) {
                    validarPassword = true;
                } else {
                    usuario.setPassword(usuarioSesion.getPassword());
                }

                Part avatarPart = request.getPart("avatar");
                if (avatarPart != null && avatarPart.getSize() > 0) {
                    String fileName = UtilsImagen.generateUniqueFileName(avatarPart);

                    // Definir la ruta de subida
                    String uploadPath = getServletContext().getRealPath("") + UPLOAD_DIRECTORY;

                    // Crear directorio si no existe
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdir();
                    }

                    String filePath = uploadPath + File.separator + fileName;

                    avatarPart.write(filePath);

                    usuario.setAvatar(fileName);
                } else {
                    usuario.setAvatar(usuarioSesion.getAvatar());
                }

                boolean datosCambiados = !usuario.equals(usuarioSesion);

                if (validarPassword) {
                    currentPassword = Utils.encriptarMD5(currentPassword);
                    if (!currentPassword.equals(usuarioSesion.getPassword())) {
                        error = true;
                        request.setAttribute("aviso", "La contraseña actual introducida no es correcta");
                        request.setAttribute("tipoAviso", "error");
                    }
                }

                if (!error) {
                    String nuevaPassword = request.getParameter("newPassword");
                    if (nuevaPassword != null && !nuevaPassword.trim().isEmpty()) {
                        usuario.setPassword(Utils.encriptarMD5(nuevaPassword));
                        passwordCambiada = true; // Marcar que la contraseña fue cambiada
                    } else {
                        usuario.setPassword(usuarioSesion.getPassword());
                    }

                    // Verificar si hubo cambios en los datos o en la contraseña
                    if (datosCambiados || passwordCambiada) {
                        DAOFactory daof = new DAOFactory();
                        IUsuarioDAO daou = daof.getUsuarioDAO();

                        // Actualizamos el usuario en la base de datos
                        if (daou.actualizarUsuario(usuario)) {
                            // Si la actualización es exitosa, actualizamos los datos en la sesión
                            session.setAttribute("usuario", usuario);
                            request.setAttribute("aviso", "Perfil actualizado correctamente");
                            request.setAttribute("tipoAviso", "success");
                        } else {
                            // Si la actualización falla, mostramos un mensaje de error
                            request.setAttribute("aviso", "No se pudo actualizar el perfil");
                            request.setAttribute("tipoAviso", "error");
                        }
                    } else {
                        request.setAttribute("aviso", "No se detectaron cambios");
                        request.setAttribute("tipoAviso", "error");
                    }
                }
                break;

        }

        request.getRequestDispatcher(url).forward(request, response);

    }

}
