<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <title>Registro</title>
        <jsp:include page="/INC/metas.inc"/>
        <link rel="stylesheet" href="${estilo}"/>
        <link rel="stylesheet" href="${formulario}"/>
        <link rel="icon" type="image/png" href="${contexto}/IMG/logo.png">
        <script type="module" src="${contexto}/JS/validacionRegistro.js" defer></script>
        <script type="module" src="${contexto}/JS/patronesValidacion.js" defer></script>
        <script type="module" src="${contexto}/JS/scriptCabecera.js" defer></script>
    </head>
    <body>
        <%@ include file="/INC/cabecera.jsp" %>
        <div class="form-container">
            <h1>Formulario de registro</h1>
            <p class="required-field-notice">Los campos marcados con * son obligatorios</p>
            <form id="registroForm" action="Registro" method="POST" enctype="multipart/form-data">
                <!-- Columna Izquierda -->
                <div class="form-column">
                    <!-- Datos Personales -->
                    <label for="nombre">Nombre: <span class="required">*</span></label>
                    <input type="text" id="nombre" name="nombre">
                    <span class="error" id="nombreError"></span>

                    <label for="apellidos">Apellidos: <span class="required">*</span></label>
                    <input type="text" id="apellidos" name="apellidos">
                    <span class="error" id="apellidosError"></span>

                    <label for="nif">NIF: <span class="required">*</span></label>
                    <div class="nif-container">
                        <input type="text" id="nif" name="nif" maxlength="8">
                        <input type="text" id="nifLetra" name="nifLetra" readonly>
                        <span class="error" id="nifError"></span>
                    </div>

                    <label for="telefono">Teléfono: <span class="optional">(Opcional)</span></label>
                    <input type="tel" id="telefono" name="telefono">
                    <span class="error" id="telefonoError"></span>

                    <label for="email">Correo Electrónico: <span class="required">*</span></label>
                    <input type="email" id="email" name="email">
                    <span id="correoStatus"></span>

                    <label for="password">Contraseña: <span class="required">*</span></label>
                    <input type="password" id="password" name="password">
                    <span class="error" id="passwordError"></span>
                </div>

                <!-- Columna Derecha -->
                <div class="form-column">
                    <label for="confirmPassword">Confirmar Contraseña: <span class="required">*</span></label>
                    <input type="password" id="confirmPassword">
                    <span class="error" id="confirmPasswordError"></span>

                    <label for="direccion">Dirección: <span class="required">*</span></label>
                    <input type="text" id="direccion" name="direccion">
                    <span class="error" id="direccionError"></span>

                    <label for="localidad">Localidad: <span class="required">*</span></label>
                    <input type="text" id="localidad" name="localidad">
                    <span class="error" id="localidadError"></span>

                    <label for="provincia">Provincia: <span class="required">*</span></label>
                    <input type="text" id="provincia" name="provincia">
                    <span class="error" id="provinciaError"></span>

                    <label for="codigoPostal">Código Postal: <span class="required">*</span></label>
                    <input type="text" id="codigoPostal" name="codigoPostal">
                    <span class="error" id="codigoPostalError"></span>

                    <div id="avatar-preview"></div>
                </div>

      
                <div class="form-full">
                    <label for="avatar">Foto de Avatar: <span class="optional">(Opcional)</span></label>
                    <input type="file" id="avatar" name="avatar" accept="image/*">
                    <span class="error" id="avatarError"></span>
                </div>

                <!-- Botón de Registro -->
                <div>
                    <input type="hidden" name="accion" value="registro">
                    <button type="submit" class="form-button">Registrarse</button>
                </div>
            </form>
        </div>
    </body>
    <%@ include file="/INC/acceso.jsp"%>
</html>