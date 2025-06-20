package es.geekhub.models;

import javax.servlet.http.Part;

/**
 * Utilidad para la gestión de nombres de archivo en la carga de imágenes.
 *
 * <p>
 * Proporciona métodos para generar nombres únicos para los archivos subidos y
 * para obtener el nombre original del archivo desde un objeto {@link Part}.</p>
 *
 * @author agp00
 */
public class UtilsImagen {

    public static String generateUniqueFileName(Part part) {
        String originalFileName = getFileName(part);
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return System.currentTimeMillis() + extension;
    }

    public static String getFileName(Part part) {
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "";
    }

}
