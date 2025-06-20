import patronesValidacion from './patronesValidacion.js';

function calcularLetraDNI(numeros) {
    const letras = "TRWAGMYFPDXBNJZSQVHLCKE";
    const resto = numeros % 23;
    return letras.charAt(resto);
}

// Estado del formulario
const formValid = {
    nombre: false,
    apellidos: false,
    correo: false,
    password: false,
    confirmPassword: false,
    nif: false,
    telefono: true,
    direccion: false,
    localidad: false,
    provincia: false,
    codigoPostal: false
};

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('registroForm');

    // Función helper para mostrar errores
    function showError(elementId, message) {
        const errorElement = document.getElementById(elementId);
        errorElement.textContent = message;
        errorElement.classList.add('error');
    }

    // Función helper para limpiar errores
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

    // Validación del teléfono
    document.getElementById('telefono').addEventListener('blur', function () {
        const telefono = this.value.trim();
        if (!telefono) {
            clearError('telefonoError'); // Limpiamos el error si está vacío porque es opcional
            formValid.telefono = true;   // Es válido si está vacío
        } else if (!patronesValidacion.isValid('phone', telefono)) {
            showError('telefonoError', patronesValidacion.errorMessages.phone);
            formValid.telefono = false;
        } else {
            clearError('telefonoError');
            formValid.telefono = true;
        }
    });

    // Validación de email
    document.getElementById('email').addEventListener('blur', async function () {
        const correo = this.value.trim();
        const correoStatus = document.getElementById('correoStatus');

        if (!correo) {
            correoStatus.textContent = 'El correo es obligatorio';
            correoStatus.className = 'error';
            formValid.correo = false;
            return;
        }

        if (!patronesValidacion.isValid('email', correo)) {
            correoStatus.textContent = 'Formato de correo inválido';
            correoStatus.className = 'error';
            formValid.correo = false;
            return;
        }

        try {
            const data = new URLSearchParams();
            data.append('accion', 'verificarCorreo');
            data.append('correo', correo);

            const response = await fetch('Ajax', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: data.toString()
            });

            const resultado = await response.json();

            if (resultado.disponible === false) {
                correoStatus.textContent = 'Este correo ya está registrado';
                correoStatus.className = 'error';
                formValid.correo = false;
            } else {
                correoStatus.textContent = 'Correo disponible';
                correoStatus.className = 'success';
                formValid.correo = true;
            }
        } catch (error) {
            console.error('Error al verificar el correo:', error);
            correoStatus.textContent = 'Error al verificar el correo';
            correoStatus.className = 'error';
            formValid.correo = false;
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
            showError('provinciaError', 'La provincia solo puede contener letras y espacios');
            formValid.provincia = false;
        } else {
            clearError('provinciaError');
            formValid.provincia = true;
        }
    });

    // Validación de la contraseña
    document.getElementById('password').addEventListener('blur', function () {
        const password = this.value;
        if (!password) {
            showError('passwordError', 'La contraseña es obligatoria');
            formValid.password = false;
        } else if (!patronesValidacion.isValid('password', password)) {
            showError('passwordError', patronesValidacion.errorMessages.password);
            formValid.password = false;
        } else {
            clearError('passwordError');
            formValid.password = true;
        }
        // Validar confirmación si existe
        const confirmPassword = document.getElementById('confirmPassword');
        if (confirmPassword.value) {
            confirmPassword.dispatchEvent(new Event('input'));
        }
    });

    // Validación de confirmación de contraseña
    document.getElementById('confirmPassword').addEventListener('input', function () {
        const password = document.getElementById('password').value;
        const confirmPassword = this.value;
        if (!confirmPassword) {
            showError('confirmPasswordError', 'Debes confirmar la contraseña');
            formValid.confirmPassword = false;
        } else if (password !== confirmPassword) {
            showError('confirmPasswordError', 'Las contraseñas no coinciden');
            formValid.confirmPassword = false;
        } else {
            clearError('confirmPasswordError');
            formValid.confirmPassword = true;
        }
    });

    // Validación del NIF
    document.getElementById('nif').addEventListener('input', async function () {
        const nifNumeros = this.value.trim();
        const nifLetra = document.getElementById('nifLetra');

        if (!nifNumeros) {
            showError('nifError', 'El NIF es obligatorio');
            nifLetra.value = '';
            formValid.nif = false;
            return;
        }

        if (!patronesValidacion.isValid('nif', nifNumeros)) {
            showError('nifError', patronesValidacion.errorMessages.nif);
            nifLetra.value = '';
            formValid.nif = false;
            return;
        }

        try {
            const data = new URLSearchParams();
            data.append('accion', 'calcularLetraNIF');
            data.append('numeros', nifNumeros);

            const response = await fetch('Ajax', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: data.toString()
            });

            const resultado = await response.json();

            if (resultado.error) {
                showError('nifError', resultado.mensaje);
                nifLetra.value = '';
                formValid.nif = false;
            } else {
                clearError('nifError');
                nifLetra.value = resultado.letra;
                formValid.nif = true;
            }
        } catch (error) {
            console.error('Error al calcular la letra del NIF:', error);
            showError('nifError', 'Error al calcular la letra del NIF');
            nifLetra.value = '';
            formValid.nif = false;
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

    // Validación del formulario completo
    form.addEventListener('submit', function (e) {
        e.preventDefault();

        // Dispara validaciones de todos los campos
        const camposAValidar = [
            'nombre', 'apellidos', 'email', 'password', 'confirmPassword',
            'nif', 'telefono', 'direccion', 'localidad', 'provincia', 'codigoPostal'
        ];

        camposAValidar.forEach(campo => {
            const elemento = document.getElementById(campo);
            elemento.dispatchEvent(new Event('blur'));
            if (campo === 'nif') {
                elemento.dispatchEvent(new Event('input'));
            }
        });

        // Verifica el estado de las validaciones
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