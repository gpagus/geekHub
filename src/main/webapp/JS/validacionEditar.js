import patronesValidacion from './patronesValidacion.js';

// Estado del formulario de edición
const formValid = {
    nombre: true,           // Inicialmente true porque los campos vienen con datos
    apellidos: true,
    telefono: true,
    direccion: true,
    localidad: true,
    provincia: true,
    codigoPostal: true,
    currentPassword: true,  
    newPassword: true,      
    confirmPassword: true  
};

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('editarPerfilForm');

    // Funciones helper
    function showError(elementId, message) {
        const errorElement = document.getElementById(elementId);
        errorElement.textContent = message;
        errorElement.classList.add('error');
    }

    function clearError(elementId) {
        const errorElement = document.getElementById(elementId);
        errorElement.textContent = '';
        errorElement.classList.remove('error');
    }

    // Validación del nombre
    document.getElementById('nombre').addEventListener('blur', function () {
        const nombre = this.value.trim();
        if (!nombre) {
            showError('nombreError', 'El nombre es obligatorio');
            formValid.nombre = false;
       } else if (!patronesValidacion.isValid('name', nombre)) {
            showError('nombreError', patronesValidacion.errorMessages.name);
            formValid.nombre = false;
        } else {
            clearError('nombreError');
            formValid.nombre = true;
        }
    });

    // Validación de apellidos
    document.getElementById('apellidos').addEventListener('blur', function () {
        const apellidos = this.value.trim();
        if (!apellidos) {
            showError('apellidosError', 'Los apellidos son obligatorios');
            formValid.apellidos = false;
        } else if (!patronesValidacion.isValid('name', apellidos)) {
            showError('apellidosError', patronesValidacion.errorMessages.name);
            formValid.apellidos = false;
        } else {
            clearError('apellidosError');
            formValid.apellidos = true;
        }
    });

    // Validación del teléfono (opcional)
    document.getElementById('telefono').addEventListener('blur', function () {
        const telefono = this.value.trim();
        if (telefono && !patronesValidacion.isValid('phone', telefono)) {
            showError('telefonoError', patronesValidacion.errorMessages.phone);
            formValid.telefono = false;
        } else {
            clearError('telefonoError');
            formValid.telefono = true;
        }
    });

    // Validación de contraseña actual
    document.getElementById('currentPassword').addEventListener('blur', function () {
        const currentPassword = this.value;
        const newPassword = document.getElementById('newPassword').value;
        
        // Solo validar si se está intentando cambiar la contraseña
        if (newPassword && !currentPassword) {
            showError('currentPasswordError', 'Debes introducir tu contraseña actual para cambiarla');
            formValid.currentPassword = false;
        } else if (currentPassword && !patronesValidacion.isValid('password', currentPassword)) {
            showError('currentPasswordError', patronesValidacion.errorMessages.password);
            formValid.currentPassword = false;
        } else {
            clearError('currentPasswordError');
            formValid.currentPassword = true;
        }
    });

    // Validación de nueva contraseña
    document.getElementById('newPassword').addEventListener('blur', function () {
        const newPassword = this.value;
        const currentPassword = document.getElementById('currentPassword').value;
        
        if (currentPassword && !newPassword) {
            showError('newPasswordError', 'Debes introducir la nueva contraseña');
            formValid.newPassword = false;
        } else if (newPassword && !patronesValidacion.isValid('password', newPassword)) {
            showError('newPasswordError', patronesValidacion.errorMessages.password);
            formValid.newPassword = false;
        } else {
            clearError('newPasswordError');
            formValid.newPassword = true;
        }

        // Validar confirmación si existe
        const confirmPassword = document.getElementById('confirmPassword');
        if (confirmPassword.value) {
            confirmPassword.dispatchEvent(new Event('blur'));
        }
    });

    // Validación de confirmación de nueva contraseña
    document.getElementById('confirmPassword').addEventListener('blur', function () {
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = this.value;
        
        if (newPassword && !confirmPassword) {
            showError('confirmPasswordError', 'Debes confirmar la nueva contraseña');
            formValid.confirmPassword = false;
        } else if (newPassword && newPassword !== confirmPassword) {
            showError('confirmPasswordError', 'Las contraseñas no coinciden');
            formValid.confirmPassword = false;
        } else {
            clearError('confirmPasswordError');
            formValid.confirmPassword = true;
        }
    });

    // Validación de la dirección
    document.getElementById('direccion').addEventListener('blur', function () {
        const direccion = this.value.trim();
        if (!direccion) {
            showError('direccionError', 'La dirección es obligatoria');
            formValid.direccion = false;
        } else if (!patronesValidacion.isValid('address', direccion)) {
            showError('direccionError', patronesValidacion.errorMessages.address);
            formValid.direccion = false;
        } else {
            clearError('direccionError');
            formValid.direccion = true;
        }
    });

    // Validación de la localidad
    document.getElementById('localidad').addEventListener('blur', function () {
        const localidad = this.value.trim();
        if (!localidad) {
            showError('localidadError', 'La localidad es obligatoria');
            formValid.localidad = false;
        } else if (!patronesValidacion.isValid('text', localidad)) {
            showError('localidadError', patronesValidacion.errorMessages.text);
            formValid.localidad = false;
        } else {
            clearError('localidadError');
            formValid.localidad = true;
        }
    });

    // Validación de la provincia
    document.getElementById('provincia').addEventListener('blur', function () {
        const provincia = this.value.trim();
        if (!provincia) {
            showError('provinciaError', 'La provincia es obligatoria');
            formValid.provincia = false;
        } else if (!patronesValidacion.isValid('text', provincia)) {
            showError('provinciaError', patronesValidacion.errorMessages.text);
            formValid.provincia = false;
        } else {
            clearError('provinciaError');
            formValid.provincia = true;
        }
    });

    // Validación del código postal
    document.getElementById('codigoPostal').addEventListener('blur', function () {
        const cp = this.value.trim();
        if (!cp) {
            showError('codigoPostalError', 'El código postal es obligatorio');
            formValid.codigoPostal = false;
        } else if (!patronesValidacion.isValid('cp', cp)) {
            showError('codigoPostalError', patronesValidacion.errorMessages.cp);
            formValid.codigoPostal = false;
        } else {
            clearError('codigoPostalError');
            formValid.codigoPostal = true;
        }
    });

    // Validación del archivo de avatar
    document.getElementById('avatar').addEventListener('change', function() {
        const file = this.files[0];
        if (file) {
            if (!file.type.startsWith('image/')) {
                alert('Por favor, selecciona un archivo de imagen válido');
                this.value = '';
            } else if (file.size > 5 * 1024 * 1024) { // 5MB límite
                alert('La imagen no puede superar los 5MB');
                this.value = '';
            }
        }
    });

    // Validación del formulario completo
    form.addEventListener('submit', function (e) {
        e.preventDefault();

        // Disparar validaciones de todos los campos obligatorios
        const camposObligatorios = [
            'nombre', 'apellidos', 'direccion', 
            'localidad', 'provincia', 'codigoPostal'
        ];

        camposObligatorios.forEach(campo => {
            const elemento = document.getElementById(campo);
            elemento.dispatchEvent(new Event('blur'));
        });

        // Validar campos de contraseña solo si se está intentando cambiar
        const currentPassword = document.getElementById('currentPassword').value;
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        if (currentPassword || newPassword || confirmPassword) {
            ['currentPassword', 'newPassword', 'confirmPassword'].forEach(campo => {
                document.getElementById(campo).dispatchEvent(new Event('blur'));
            });
        }

        // Verificar el estado de las validaciones
        const isValid = Object.values(formValid).every(valid => valid);
        console.log('Estado de validación:', formValid);

        if (isValid) {
            console.log('Formulario válido, enviando...');
            this.submit();
        } else {
            console.error('Algunos campos tienen errores');
            // Desplazarse al primer error
            const firstError = document.querySelector('.error:not(:empty)');
            if (firstError) {
                firstError.scrollIntoView({behavior: 'smooth', block: 'center'});
            }
        }
    });
});