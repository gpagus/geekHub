<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INC/metas.inc" />
        <link rel="icon" type="image/png" href="${contexto}/IMG/logo.png">
        <link rel="stylesheet" href="${contexto}/CSS/error.css" />
        <title>Error 500</title>
    </head>
    <body>
        <div class="error-container">
            <img src="${contexto}/IMG/logo.png" alt="Error del Servidor 500">
            <h1>Error 500 - Error Interno del Servidor</h1>
            <p>Lo sentimos, ha ocurrido un error inesperado en nuestro sistema.</p>
            <p>Estamos trabajando para solucionarlo lo antes posible. Por favor, intenta nuevamente más tarde.</p>

            <form action="FrontController" method="post">
                <button type="submit">Inicio</button>
            </form>
        </div>
    </body>
</html>
