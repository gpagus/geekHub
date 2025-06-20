<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html lang="es">
    <head>
        <%@ include file="/INC/metas.inc" %>
        <link rel="stylesheet" href="${estilo}" />
        <link rel="stylesheet" href="${contexto}/CSS/detalles.css" />
        <link rel="icon" type="image/png" href="${contexto}/IMG/logo.png">
        <title>${producto.nombre}</title>
        <script type="module" src="${contexto}/JS/scriptCabecera.js" defer></script>
        <script type="module" src="${contexto}/JS/patronesValidacion.js" defer></script>

    </head>
    <body>
        <%@ include file="/INC/cabecera.jsp" %>
        <main class="main-content">
            <!-- Información del producto -->
            <div class="product-header">
                <img src="${contexto}/IMG/productos/${producto.imagen}.jpg" alt="${producto.nombre}" class="product-image">
                <div class="product-details">
                    <h1>${producto.nombre}</h1>
                    <p><strong>Categoría:</strong> ${producto.categoria.nombre}</p>
                    <p><strong>Marca:</strong> ${producto.marca}</p>
                    <p><strong>Precio:</strong> <fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="€" groupingUsed="true" maxFractionDigits="2" /></p>
                    <p><strong>Descripción:</strong> ${producto.descripcion}</p>
                </div>
            </div>

            <!-- Botón para añadir al carrito -->
            <div class="add-to-cart">
                <form action="Cesta" method="POST">
                    <input type="hidden" name="idProducto" value="${producto.idProducto}" />
                    <button type="submit" name="accion" value="add">Añadir al carrito</button>
                </form>
            </div>
        </main>

    </body>
    <%@ include file="/INC/acceso.jsp" %>
</html>
