<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div id="loginModal" class="modal-background">
    <div class="modal">
        <button class="close-button" onclick="closeModal()">&times;</button>
        <div class="modal-header">
            <h2 class="modal-title">Iniciar Sesión</h2>
        </div>
        <form action="Registro" method="POST">
            <div class="form-group">
                <label for="emailLogin">Correo Electrónico: <span class="required">*</span></label>
                <input type="email" class="form-control" id="emailLogin" name="email">
                <span id="correoStatus"></span>
            </div>
            <div class="form-group">
                <label for="passwordLogin">Contraseña: <span class="required">*</span></label>
                <input type="password" class="form-control" id="passwordLogin" name="password">
                <span class="error" id="passwordError"></span>
            </div>
            <button  type="submit" name="accion" value="acceso" class="btn btn-primary">Ingresar</button>
            <div class="footer-links">
                <p>¿No tienes una cuenta? <a href="Registro">Regístrate</a></p>
            </div>
        </form>
    </div>
</div>