<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>Datos del usuario</title>  
        <%@ include file="/INC/metas.inc" %>
        <link rel="stylesheet" href="${estilo}" />
        <link rel="stylesheet" href="${formulario}"/>
        <link rel="icon" type="image/png" href="${contexto}/IMG/logo.png">
        <script type="module" src="${contexto}/JS/validacionEditar.js" defer></script>
        <script type="module" src="${contexto}/JS/patronesValidacion.js" defer></script>
        <script type="module" src="${contexto}/JS/scriptCabecera.js" defer></script>
    </head>
    <body>
        <%@ include file="/INC/cabecera.jsp" %>
        <div class="form-container">
            <form id="editarPerfilForm" action="Registro" method="POST" enctype="multipart/form-data">
                <h1>Datos del perfil</h1>
                <!-- Columna Izquierda -->
                <div class="form-column">
                    <!-- Datos Personales -->
                    <label for="nombre">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" value="${sessionScope.usuario.nombre}">
                    <span class="error" id="nombreError"></span>

                    <label for="apellidos">Apellidos:</label>
                    <input type="text" id="apellidos" name="apellidos" value="${sessionScope.usuario.apellidos}">
                    <span class="error" id="apellidosError"></span>

                    <!-- NIF (Deshabilitado) -->
                    <label for="nif">NIF:</label>
                    <input type="text" id="nif" name="nif" value="${sessionScope.usuario.nif}" readonly>
                    <span class="error" id="nifError"></span>

                    <label for="telefono">Teléfono:</label>
                    <input type="tel" id="telefono" name="telefono" value="${sessionScope.usuario.telefono}">
                    <span class="error" id="telefonoError"></span>

                    <!-- Email (Deshabilitado) -->
                    <label for="email">Correo Electrónico:</label>
                    <input type="email" id="email" name="email" value="${sessionScope.usuario.email}" readonly>
                    <span id="correoStatus"></span>

                    <div id="avatar-preview">
                        <img src="<c:url value='./IMG/avatares/${sessionScope.usuario.avatar}'/>" alt="avatar usuario" title="Perfil"/>
                    </div>
                </div>

                <!-- Columna Derecha -->
                <div class="form-column">
                    <!-- Cambiar Contraseña -->
                    <h3>Cambiar Contraseña</h3>
                    <label for="currentPassword">Contraseña Actual:</label>
                    <input type="password" id="currentPassword" name="currentPassword">
                    <span class="error" id="currentPasswordError"></span>

                    <label for="newPassword">Nueva Contraseña:</label>
                    <input type="password" id="newPassword" name="newPassword">
                    <span class="error" id="newPasswordError"></span>

                    <label for="confirmPassword">Confirmar Nueva Contraseña:</label>
                    <input type="password" id="confirmPassword" name="confirmPassword">
                    <span class="error" id="confirmPasswordError"></span>

                    <!-- Dirección -->
                    <label for="direccion">Dirección:</label>
                    <input type="text" id="direccion" name="direccion" value="${sessionScope.usuario.direccion}">
                    <span class="error" id="direccionError"></span>

                    <label for="localidad">Localidad:</label>
                    <input type="text" id="localidad" name="localidad" value="${sessionScope.usuario.localidad}">
                    <span class="error" id="localidadError"></span>

                    <label for="provincia">Provincia:</label>
                    <input type="text" id="provincia" name="provincia" value="${sessionScope.usuario.provincia}">
                    <span class="error" id="provinciaError"></span>

                    <label for="codigoPostal">Código Postal:</label>
                    <input type="text" id="codigoPostal" name="codigoPostal" value="${sessionScope.usuario.codigoPostal}">
                    <span class="error" id="codigoPostalError"></span>
                </div>

                <!-- Foto de Avatar -->
                <div class="form-full">
                    <label for="avatar">Cambiar de Avatar:</label>
                    <input type="file" id="avatar" name="avatar" accept="image/*">
                    <span class="error" id="avatarError"></span>

                </div>

                <!-- Botón de Actualizar -->
                <div>
                    <input type="hidden" name="accion" value="editar">
                    <button type="submit" class="form-button">Aplicar cambios</button>
                </div>

            </form>
        </div>

    </body>
    <%@ include file="/INC/acceso.jsp"%>
</html>
