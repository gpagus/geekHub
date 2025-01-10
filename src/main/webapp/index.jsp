<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contexto" value="${pageContext.request.contextPath}" scope="application" />
<c:set var="estilo" value="${contexto}/CSS/estilo.css" scope="application" />
<c:set var="formulario" value="${contexto}/CSS/formulario.css" scope="application" />

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <jsp:include page="/INC/metas.inc"/>
        <meta http-equiv="refresh" content="2;URL=${contexto}/FrontController">
        <link rel="stylesheet" href="${estilo}"/>
        <title>Página de Inicio</title>
    </head>
    <body>
        <header class="header">
            <img src="./IMG/logo.png" alt="logo"/>
            <div>
                <h1>GeekHub</h1>
                <h3><em>Tu destino para componentes de PC al mejor precio</em></h3>
            </div>

        </header>
    </body>
</html>
