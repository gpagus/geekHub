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
                    <div class="filter-section">
                        <h3>Precio</h3>
                        <input type="range" min="0" max="1000" step="50" />
                        <div>0€ - 1000€</div>
                    </div>

                    <div class="filter-section">
                        <h3>Categoría</h3>
                        <div><input type="checkbox" /> Procesadores</div>
                        <div><input type="checkbox" /> Tarjetas Gráficas</div>
                        <div><input type="checkbox" /> Memoria RAM</div>
                    </div>

                    <div class="filter-section">
                        <h3>Marca</h3>
                        <div><input type="checkbox" /> AMD</div>
                        <div><input type="checkbox" /> Intel</div>
                        <div><input type="checkbox" /> NVIDIA</div>
                    </div>
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
