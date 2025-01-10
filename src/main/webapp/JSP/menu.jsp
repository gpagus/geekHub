<jsp:directive.page contentType="text/html" pageEncoding="UTF-8" />
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <jsp:include page="/INC/metas.inc" />
        <link rel="stylesheet" href="${estilo}" />
        <script src="${contexto}/JS/script.js" defer></script>
        <title>Menú</title>
    </head>
    <body>
        <header>
            <img src="./IMG/logo.png" alt="logo"/>
            <div>
                <div id="search-bar">
                    <input type="text" placeholder="Search" />
                </div>
                <form action="FrontController" method="POST">
                    <button type="submit" name="accion" value="carrito" 
                            <c:if test="${empty sessionScope.carrito}">disabled</c:if>>
                                <img src="./IMG/carrito.png" alt="carrito"/>
                            </button>
                            <button type="submit" name="accion" value="login">Login</button>
                            <button type="submit" name="accion" value="registro">Registro</button> 
                    </form>

                </div>
            </header>

            <main class="main-content">
                <aside class="filters">
                    <form action="${contexto}/FrontController" method="POST" class="filter-form">
                    
                    <!-- Filtro de Categorías -->
                    <div class="filter-section">
                        <h3>Categorías</h3>
                        <c:forEach var="categoria" items="${categorias}">
                            <div>
                                <label>
                                    <input type="checkbox" name="categorias" value="${categoria.idCategoria}" />
                                    ${categoria.nombre}
                                </label>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Filtro de Marcas -->
                    <div class="filter-section">
                        <h3>Marcas</h3>
                        <c:forEach var="marca" items="${marcas}">
                            <div>
                                <label>
                                    <input type="checkbox" name="marcas" value="${marca}" />
                                    ${marca}
                                </label>
                            </div>
                        </c:forEach>
                    </div>
                    
                    <!-- Filtro por precio -->
                    <div class="filter-section">
                        <h3>Precio</h3>
                        <div>
                            <label>
                                <input type="radio" name="price" value="0-50" />
                                0€ - 50€
                            </label>
                        </div>
                        <div>
                            <label>
                                <input type="radio" name="price" value="51-100" />
                                51€ - 100€
                            </label>
                        </div>
                        <div>
                            <label>
                                <input type="radio" name="price" value="101-500" />
                                101€ - 500€
                            </label>
                        </div>
                        <div>
                            <label>
                                <input type="radio" name="price" value="501-1000" />
                                501€ - 1000€
                            </label>
                        </div>
                        <div>
                            <label>
                                <input type="radio" name="price" value="1001-9999" />
                                Más de 1000€
                            </label>
                        </div>
                    </div>

                    <!-- Botones para aplicar/borrar filtros -->
                    <div class="filter-buttons">
                        <input type="hidden" name="accion" value="filtro">
                        <button type="submit" class="filter-submit">Filtrar</button>
                        <a href="${contexto}/FrontController" class="clear-filters">Borrar Filtros</a>
                    </div>
                </form>

            </aside>

            <section class="products">
                <c:forEach var="producto" items="${productos}">
                    <div class="product-card">
                        <img src="${contexto}/IMG/productos/${producto.imagen}.jpg" alt="${producto.nombre}" class="product-image">
                        <h3>${producto.nombre}</h3>
                        <p class="price">${producto.precio}€</p>
                        <form action="${contexto}/Cesta" method="POST">
                            <input type="hidden" name="accion" value="add">
                            <input type="hidden" name="idProducto" value="${producto.idProducto}">
                            <button type="submit">Añadir al carrito</button>
                        </form>
                    </div>
                </c:forEach>
            </section>
        </main>
    </body>
</html>

