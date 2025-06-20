<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <%@ include file="/INC/metas.inc" %>
        <link rel="stylesheet" href="${estilo}" />
        <link rel="icon" type="image/png" href="${contexto}/IMG/logo.png">
        <title>Carrito</title>
        <script type="module" src="${contexto}/JS/scriptCabecera.js" defer></script>
        <script type="module" src="${contexto}/JS/patronesValidacion.js" defer></script>

        <script src="${contexto}/JS/sumarRestarCantidad.js" defer></script>
    </head>

    <body>
        <%@ include file="/INC/cabecera.jsp" %>

        <main class="main-content-carrito">
            <h1>Carrito de Compras</h1>

            <table class="cart-table">
                <thead>
                    <tr>
                        <th>Imagen</th>
                        <th>Código</th>
                        <th>Producto</th>
                        <th>Marca</th>
                        <th>Cantidad</th>
                        <th>Precio Unitario</th>
                        <th>Subtotal</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="linea" items="${sessionScope.pedido.lineasPedidos}">
                        <tr>
                            <td>
                                <img src="${contexto}/IMG/productos/${linea.producto.imagen}.jpg" alt="${linea.producto.nombre}" style="width: 50px;">
                            </td>
                            <td>${linea.producto.idProducto}</td>
                            <td>${linea.producto.nombre}</td>
                            <td>${linea.producto.marca}</td>
                            <td id="cantidad-${linea.producto.idProducto}">${linea.cantidad}</td>
                            <td>
                                <fmt:formatNumber value="${linea.producto.precio}" type="currency" currencySymbol="€" groupingUsed="true" maxFractionDigits="2" />
                            </td>
                            <td>
                                <fmt:formatNumber value="${linea.producto.precio * linea.cantidad}" type="currency" currencySymbol="€" groupingUsed="true" maxFractionDigits="2" />
                            </td>
                            <td>
                                <div class="botones-container">
                                    <div>
                                        <!-- Botón para disminuir la cantidad -->
                                        <button class="quantity-btn" data-id="${linea.producto.idProducto}" data-action="restar">-</button>
                                        <!-- Botón para aumentar la cantidad -->
                                        <button class="quantity-btn" data-id="${linea.producto.idProducto}" data-action="aumentar">+</button>
                                    </div> 
                                    <!-- Botón para eliminar producto del carrito -->
                                    <form action="${contexto}/Cesta" method="POST" class="remove-item-form">
                                        <input type="hidden" name="accion" value="remove" />
                                        <input type="hidden" name="idProducto" value="${linea.producto.idProducto}" />
                                        <button type="submit">Eliminar</button>
                                    </form>
                                </div>

                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Cálculos de la factura -->
            <h3>Resumen de la compra</h3>
            <table class="cart-table">
                <tr>
                    <td><strong>Base Imponible:</strong></td>
                    <td id="base-imponible">
                        <fmt:formatNumber value="${sessionScope.pedido.importe}" type="currency" currencySymbol="€" groupingUsed="true" maxFractionDigits="2" />
                    </td>
                </tr>
                <tr>
                    <td><strong>IVA (21%):</strong></td>
                    <td id="iva">
                        <fmt:formatNumber value="${sessionScope.pedido.iva}" type="currency" currencySymbol="€" groupingUsed="true" maxFractionDigits="2" />
                    </td>
                </tr>
                <tr>
                    <td><strong>Total a pagar:</strong></td>
                    <td id="importe-total">
                        <fmt:formatNumber value="${sessionScope.pedido.importe + sessionScope.pedido.iva}" type="currency" currencySymbol="€" groupingUsed="true" maxFractionDigits="2" />
                    </td>
                </tr>
            </table>

            <div style="display: flex; gap: 10px;">
                <form action="${contexto}/Cesta" method="POST">
                    <button type="submit" name="accion" value="finalizar" <c:if test="${empty sessionScope.usuario}">disabled style="opacity: 0.5;"</c:if> >
                            Finalizar Compra
                        </button>
                    </form>

                    <!-- Botón para eliminar el carrito -->
                    <form action="${contexto}/Cesta" method="POST">
                    <button type="submit" name="accion" value="limpiarCarrito">Eliminar carrito</button>
                </form>
            </div>

            <c:if test="${empty sessionScope.usuario}">
                <p>Debes estar registrado para poder comprar</p>
            </c:if>

        </main>
    </body>
    <%@ include file="/INC/acceso.jsp" %>
</html>
