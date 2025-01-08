<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Registro</title>
        <jsp:include page="/INC/metas.inc"/>
        <link rel="stylesheet" href="${estilo}"/>
        <link rel="stylesheet" href="${formulario}"/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    </head>
    <body>
        <jsp:include page="/INC/cabecera.inc"/>
        <h1 style="text-align: center">Registro de Usuario</h1>
        <div class="form-container">
            <form id="registroForm" action="FrontController" method="POST" enctype="multipart/form-data">

                <!-- Columna Izquierda -->
                <div class="form-column">
                    <!-- Nombre -->
                    <label for="nombre">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" required>
                    <span class="error" id="nombreError"></span>

                    <!-- Correo Electrónico -->
                    <label for="correo">Correo Electrónico:</label>
                    <input type="email" id="correo" name="correo" required>
                    <span id="correoStatus"></span>

                    <!-- Contraseña -->
                    <label for="password">Contraseña:</label>
                    <input type="password" id="password" name="password" required>
                    <span class="error" id="passwordError"></span>
                </div>

                <!-- Columna Derecha -->
                <div class="form-column">
                    <!-- Confirmar Contraseña -->
                    <label for="confirmPassword">Confirmar Contraseña:</label>
                    <input type="password" id="confirmPassword" required>
                    <span class="error" id="confirmPasswordError"></span>

                    <!-- NIF -->
                    <label for="nif">NIF:</label>
                    <div class="nif-container">
                        <input type="text" id="nif" name="nif" maxlength="8" required>
                        <input type="text" id="nifLetra" name="nifLetra" readonly>
                        <span class="error" id="nifError"></span>
                    </div>

                    <!-- Código Postal -->
                    <label for="codigoPostal">Código Postal:</label>
                    <input type="text" id="codigoPostal" name="codigoPostal" pattern="\\d{5}" required>
                    <span class="error" id="codigoPostalError"></span>
                </div>

                <!-- Foto de Avatar -->
                <div class="form-full">
                    <label for="avatar">Foto de Avatar:</label>
                    <input type="file" id="avatar" name="avatar" accept="image/*">
                </div>

                <!-- Botón de Registro -->
                <button type="submit" class="form-button">Registrarse</button>
            </form>
        </div>



        <script>
            // Validación de contraseñas en el cliente
            $('#confirmPassword').on('input', function () {
                const password = $('#password').val();
                const confirmPassword = $(this).val();
                if (password !== confirmPassword) {
                    $('#confirmPasswordError').text('Las contraseñas no coinciden.');
                } else {
                    $('#confirmPasswordError').text('');
                }
            });

            // Comprobación de correo electrónico (Ajax)
            $('#correo').on('blur', function () {
                const correo = $(this).val();
                $.ajax({
                    url: 'FrontController',
                    method: 'POST',
                    data: {accion: 'verificarCorreo', correo: correo},
                    success: function (response) {
                        if (response === 'ocupado') {
                            $('#correoStatus').text('Correo en uso.').addClass('error').removeClass('success');
                        } else {
                            $('#correoStatus').text('Correo disponible.').addClass('success').removeClass('error');
                        }
                    },
                    error: function () {
                        $('#correoStatus').text('Error al verificar el correo.').addClass('error').removeClass('success');
                    }
                });
            });

            // Asignación de la letra del NIF (Ajax)
            $('#nif').on('input', function () {
                const nifNumeros = $(this).val();
                if (nifNumeros.length === 8 && !isNaN(nifNumeros)) {
                    $.ajax({
                        url: 'FrontController',
                        method: 'POST',
                        data: {accion: 'calcularNIF', nif: nifNumeros},
                        success: function (response) {
                            $('#nifLetra').val(response);
                        },
                        error: function () {
                            $('#nifError').text('Error al calcular la letra del NIF.');
                        }
                    });
                } else {
                    $('#nifLetra').val('');
                }
            });
        </script>
    </body>
</html>
