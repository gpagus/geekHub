<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <jsp:include page="/INC/metas.inc" />
        <link rel="stylesheet" href="${estilo}" />
        <title>Carrito</title>
    </head>

    <body>
        <jsp:include page="/INC/cabecera.inc"/>

        <main class="main-content">
            <h1>Carrito de Compras</h1>

            <table class="cart-table">
                <thead>
                    <tr>
                        <th>Producto</th>
                        <th>Cantidad</th>
                        <th>Precio Unitario</th>
                        <th>Subtotal</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="entry" items="${carrito}">
                        <tr>
                            <td>${productosMap[entry.key].nombre}</td>
                            <td>${entry.value}</td>
                            <td>${productosMap[entry.key].precio}€</td>
                            <td>${productosMap[entry.key].precio * entry.value}€</td>
                            <td>
                                <!-- Botón para aumentar la cantidad -->

                                <!-- Botón para disminuir la cantidad -->

                                <!-- Botón para eliminar el producto -->
                                
                                <form action="${contexto}/Cesta" method="POST">
                                    <input type="hidden" name="accion" value="remove">
                                    <input type="hidden" name="idProducto" value="${entry.key}">
                                    <button type="submit">Eliminar</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="cart-total">
                <p>Total: 
                    <c:set var="total" value="0" />
                    <c:forEach var="entry" items="${carrito}">
                        <c:set var="total" value="${total + (productosMap[entry.key].precio * entry.value)}" />
                    </c:forEach>
                    ${total}€
                </p>
            </div>

            <form action="${contexto}/Cesta" method="POST">
                <button type="submit" name="checkout">Finalizar Compra</button>
            </form>

            <!-- Botón para eliminar el carrito -->
            <form action="FrontController" method="post">
                <button type="submit" name="accion" value="limpiar">Eliminar carrito</button>
            </form>

        </main>
    </body>

</html>
