<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <jsp:include page="/INC/metas.inc" />
        <link rel="stylesheet" href="${estilo}" />
        <link rel="icon" type="image/png" href="${contexto}/IMG/logo.png">
        <title>Menú</title>
        <script type="module" src="${contexto}/JS/scriptCabecera.js" defer></script>
        <script type="module" src="${contexto}/JS/patronesValidacion.js" defer></script>

    </head>
    <body>
        <%@ include file="/INC/cabecera.jsp" %>
        <main class="main-content">
            <aside class="filters">
                <form action="${contexto}/Filtro" method="POST" class="filter-form">
                    <!-- Filtro de Categorías -->
                    <details class="filter-section">
                        <summary><h3>Categorías</h3></summary>
                        <div class="filter-content">
                            <c:forEach var="categoria" items="${categorias}">
                                <div>
                                    <label>
                                        <input type="checkbox"
                                               name="categorias"
                                               value="${categoria.idCategoria}"
                                               <c:if test="${filtrosSeleccionados != null && filtrosSeleccionados.categorias.contains(categoria.idCategoria)}">checked</c:if>/>
                                        ${categoria.nombre}
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </details>

                    <!-- Filtro de Marcas -->
                    <details class="filter-section">
                        <summary><h3>Marcas</h3></summary>
                        <div class="filter-content">
                            <c:forEach var="marca" items="${marcas}">
                                <div>
                                    <label>
                                        <input type="checkbox"
                                               name="marcas"
                                               value="${marca}"
                                               <c:if test="${filtrosSeleccionados != null && filtrosSeleccionados.marcas.contains(marca)}">checked</c:if>/>
                                        ${marca}
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                    </details>

                    <!-- Filtro por precio -->
                    <details class="filter-section">
                        <summary><h3>Precio</h3></summary>
                        <div class="filter-content">
                            <div>
                                <label>
                                    <input type="radio"
                                           name="price"
                                           value="0-50"
                                           <c:if test="${filtrosSeleccionados != null && filtrosSeleccionados.priceRange == '0-50'}">checked</c:if> />
                                           0€ - 50€
                                    </label>
                                </div>
                                <div>
                                    <label>
                                        <input type="radio"
                                               name="price"
                                               value="51-100"
                                        <c:if test="${filtrosSeleccionados != null && filtrosSeleccionados.priceRange == '51-100'}">checked</c:if> />
                                        51€ - 100€
                                    </label>
                                </div>
                                <div>
                                    <label>
                                        <input type="radio"
                                               name="price"
                                               value="101-500"
                                        <c:if test="${filtrosSeleccionados != null && filtrosSeleccionados.priceRange == '101-500'}">checked</c:if> />
                                        101€ - 500€
                                    </label>
                                </div>
                                <div>
                                    <label>
                                        <input type="radio"
                                               name="price"
                                               value="501-1000"
                                        <c:if test="${filtrosSeleccionados != null && filtrosSeleccionados.priceRange == '501-1000'}">checked</c:if> />
                                        501€ - 1000€
                                    </label>
                                </div>
                                <div>
                                    <label>
                                        <input type="radio"
                                               name="price"
                                               value="1001-9999"
                                        <c:if test="${filtrosSeleccionados != null && filtrosSeleccionados.priceRange == '1001-9999'}">checked</c:if> />
                                        Más de 1000€
                                    </label>
                                </div>
                            </div>

                        </details>

                        <!-- Botones para aplicar/borrar filtros -->
                        <div class="filter-buttons">
                            <button type="submit" class="submit-btn">Filtrar</button>
                            <a href="${contexto}/FrontController" class="clear-filters">Borrar Filtros</a>
                    </div>
                </form>
            </aside>

            <section class="products">
                <c:choose>
                    <c:when test="${empty productos}">
                        <div class="empty-products">
                            <p>No hay productos disponibles en este momento</p>
                            <img src="${contexto}/IMG/no-results.jpg" alt="imagen de: sin resultados"/>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="producto" items="${sessionScope.productos}">
                            <div class="product-card">

                                <!-- Formulario para ir a los detalles del producto -->
                                <form action="${contexto}/FrontController" method="POST" class="product-card-form">
                                    <input type="hidden" name="accion" value="verProducto">
                                    <input type="hidden" name="idProducto" value="${producto.idProducto}">
                                    <div class="product-details" onclick="this.closest('form').submit()">
                                        <img src="${contexto}/IMG/productos/${producto.imagen}.jpg" alt="${producto.nombre}" class="product-image">
                                        <h3 class="product-name">${producto.nombre}</h3>
                                        <p class="price">
                                            <fmt:formatNumber value="${producto.precio}" type="currency" currencySymbol="€" groupingUsed="true" maxFractionDigits="2" />
                                        </p>
                                    </div>
                                </form>

                                <!-- Formulario para añadir al carrito -->
                                <form action="${contexto}/Cesta" method="POST" class="add-to-cart-form">
                                    <input type="hidden" name="accion" value="add">
                                    <input type="hidden" name="idProducto" value="${producto.idProducto}">
                                    <button type="submit" class="add-to-cart-button">Añadir al carrito</button>
                                </form>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </section>
        </main>
    </body>
    <%@ include file="/INC/acceso.jsp" %>
</html>

