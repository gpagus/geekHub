<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<header>
    <a href="${contexto}/FrontController">
        <img src="./IMG/logo.png" alt="logo" style="cursor: pointer;">
    </a>

    <c:if test="${not empty sessionScope.usuario}">
        <div class="mensaje-bienvenida">
            <p>¡Bienvenido, ${sessionScope.usuario.nombre} ${sessionScope.usuario.apellidos}!</p>
            <c:if test="${not empty sessionScope.ultimoAccesoFormateado}">
                <p>Tu último acceso fue: ${sessionScope.ultimoAccesoFormateado}</p>
            </c:if>
        </div>
    </c:if>

    <div>
        <form action="FrontController" method="POST">
            <div id="search-bar" class="search-container">
                <input type="text" name="query" id="search-input" maxlength="50" placeholder="Buscar" />
                <button type="submit" name="accion" value="buscar" class="submit-btn" id="search-btn" style="border-radius: 50px">Buscar</button>
            </div>

            <a href="${contexto}/Cesta" id="cesta-btn" title="Carrito de compra" 
               ${empty sessionScope.pedido.lineasPedidos ? "style='pointer-events: none; opacity: 0.5;'" : ""}>
                <img src="./IMG/carrito.png" alt="icono-carrito">
            </a>




            <c:choose>
                <c:when test="${not empty sessionScope.usuario}">
                    <button type="submit" name="accion" value="pedidos">Pedidos</button>
                    <button type="submit" name="accion" value="editar">
                        <c:if test="${not empty sessionScope.usuario.avatar}">
                            <img src="<c:url value='./IMG/avatares/${sessionScope.usuario.avatar}'/>" alt="avatar usuario" title="Perfil" width="50" height="50"/>
                        </c:if>
                    </button>
                    <button type="submit" name="accion" value="cerrarSesion">Cerrar Sesión</button>
                </c:when>

                <c:otherwise>
                    <button type="submit" name="accion" value="registro">Registro</button>
                    <button type="button" id="btnAcceso" onclick="openModal()">Acceso</button>
                </c:otherwise>
            </c:choose>
        </form>
    </div>

    <c:if test="${not empty aviso}">
        <div id="toast" class="toast ${tipoAviso}">${aviso}</div>
    </c:if>

</header>