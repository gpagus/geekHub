<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INC/metas.inc" />
        <link rel="icon" type="image/png" href="${contexto}/IMG/logo.png">
        <link rel="stylesheet" href="${contexto}/CSS/error.css" />
        <title>Error 404</title>
    </head>
    <body>
        <div class="error-container">
            <img src="${contexto}/IMG/logo.png" alt="Página no encontrada - Error 404">
            <h1>Error 404 - Página no encontrada</h1>
            <p>La página que buscas no existe o ha sido movida a otra dirección.</p>
            <p>Por favor, revisa la URL o utiliza el botón de abajo para regresar al inicio.</p>

            <form action="FrontController" method="post">
                <button type="submit">Inicio</button>
            </form>
        </div>
    </body>
</html>
