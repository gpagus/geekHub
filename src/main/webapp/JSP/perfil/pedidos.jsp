<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html lang="es">
    <head>
        <%@ include file="/INC/metas.inc" %>
        <link rel="stylesheet" href="${estilo}" />
        <link rel="icon" type="image/png" href="${contexto}/IMG/logo.png">
        <title>Pedidos</title>
        <script type="module" src="${contexto}/JS/scriptCabecera.js" defer></script>
        <script type="module" src="${contexto}/JS/patronesValidacion.js" defer></script>

        <script>
            function mostrarDetalles(idPedido) {
                const detallesDiv = document.getElementById('detalles-' + idPedido);
                detallesDiv.classList.toggle('oculto');
            }
        </script>
    </head>
    <body>
        <%@ include file="/INC/cabecera.jsp" %>

        <main class="main-content-carrito">
            <h1>Listado de pedidos realizados</h1>

            <c:if test="${empty pedidosFinalizados}">
                <p>No tienes pedidos realizados.</p>
            </c:if>

            <c:forEach var="pedido" items="${pedidosFinalizados}">
                <div class="pedido">
                    <div class="pedido-resumen">
                        <fmt:formatDate value="${pedido.fecha}" pattern="dd 'de' MMMM 'de' yyyy"/>
                        <button class="submit-btn" onclick="mostrarDetalles(${pedido.idPedido})">Detalles</button>
                    </div>

                    <div id="detalles-${pedido.idPedido}" class="oculto">
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
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="linea" items="${pedido.lineasPedidos}">
                                    <tr>
                                        <td>
                                            <img src="${contexto}/IMG/productos/${linea.producto.imagen}.jpg" alt="${linea.producto.nombre}" style="width: 50px;">
                                        </td>
                                        <td>${linea.producto.idProducto}</td>
                                        <td>${linea.producto.nombre}</td>
                                        <td>${linea.producto.marca}</td>
                                        <td>${linea.cantidad}</td>
                                        <td>
                                            <fmt:formatNumber value="${linea.producto.precio}" type="currency" currencySymbol="€" groupingUsed="true" maxFractionDigits="2" />
                                        </td>
                                        <td>
                                            <fmt:formatNumber value="${linea.producto.precio * linea.cantidad}" type="currency" currencySymbol="€" groupingUsed="true" maxFractionDigits="2" />
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td><strong>Base Imponible:</strong></td>
                                    <td colspan="6">
                                        <fmt:formatNumber value="${pedido.importe}" type="currency" currencySymbol="€" groupingUsed="true" maxFractionDigits="2" />
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>IVA (21%):</strong></td>
                                    <td colspan="6">
                                        <fmt:formatNumber value="${pedido.iva}" type="currency" currencySymbol="€" groupingUsed="true" maxFractionDigits="2" />
                                    </td>
                                </tr>
                                <tr>
                                    <td><strong>Total a pagar:</strong></td>
                                    <td colspan="6">
                                        <fmt:formatNumber value="${pedido.importe + pedido.iva}" type="currency" currencySymbol="€" groupingUsed="true" maxFractionDigits="2" />
                                    </td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
            </c:forEach>
        </main> 

    </body>
</html>
