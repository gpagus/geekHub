import patronesValidacion from './patronesValidacion.js';

// NAV BAR
const searchInput = document.getElementById('search-input');
const searchButton = document.getElementById('search-btn');

// MODAL LOGIN
const emailInput = document.getElementById('emailLogin');
const passwordInput = document.getElementById('passwordLogin');
const correoStatus = document.getElementById('correoStatus');
const passwordError = document.getElementById('passwordError');
const form = document.querySelector('form[action="Registro"]');


searchInput.addEventListener('input', () => {
    if (searchInput.value.length >= 3) {
        searchButton.classList.add('visible'); // Mostrar el botón
    } else {
        searchButton.classList.remove('visible'); // Ocultarlo
    }
});

// Prevenir la acción de Enter si no hay suficientes caracteres
searchInput.addEventListener('keydown', (event) => {
    if (event.key === 'Enter' && searchInput.value.length < 3) {
        event.preventDefault(); // Prevenir la acción de búsqueda
    }
});

function openModal() {
    document.getElementById('loginModal').style.display = 'block';
    document.body.style.overflow = 'hidden';
}

function closeModal() {
    document.getElementById('loginModal').style.display = 'none';
    document.body.style.overflow = 'auto';
}

window.openModal = openModal;
window.closeModal = closeModal;

// Cerrar el modal si se hace clic fuera de él
window.onclick = function (event) {
    const modal = document.getElementById('loginModal');
    if (event.target === modal) {
        closeModal();
    }
};

// Cerrar el modal con la tecla ESC
document.addEventListener('keydown', function (event) {
    if (event.key === 'Escape') {
        closeModal();
    }
});

window.onload = function () {
    // Verificar si el bocadillo existe en la página
    const toast = document.getElementById('toast');

    if (toast) {
        // Mostrar el bocadillo
        toast.classList.add('show');

        // Después de 3 segundos, ocultarlo
        setTimeout(() => {
            toast.classList.remove('show');
        }, 2000);
    }
};


// Validación del avatar
const MAX_FILE_SIZE = 100 * 1024; // 100 KB
const avatarInput = document.getElementById('avatar');
const avatarErrorSpan = document.getElementById('avatarError');
const previewContainer = document.getElementById('avatar-preview'); // Referencia al contenedor de la vista previa

if (avatarInput !== null) {
    avatarInput.addEventListener('change', () => {
        // Limpiar mensajes de error y vista previa
        avatarErrorSpan.textContent = '';
        previewContainer.innerHTML = '';

        const file = avatarInput.files[0];
        if (!file.type.startsWith('image/')) {
            avatarErrorSpan.textContent = 'Por favor, selecciona una imagen.';
            avatarInput.value = ''; // Limpiar el input file
            return;
        } else if (file.size > MAX_FILE_SIZE) {
            avatarErrorSpan.textContent = 'El tamaño de la imagen no puede exceder los 100 KB.';
            avatarInput.value = ''; // Limpiar el input file
            return;
        } else {
            const reader = new FileReader();

            reader.onload = function (e) {
                const previewImage = document.createElement('img');
                previewImage.src = e.target.result;
                // Mostrar la vista previa
                previewContainer.innerHTML = '';
                previewContainer.appendChild(previewImage);
            };

            if (file) {
                reader.readAsDataURL(file);
            }
        }
    });
}

// Validar campo de correo electrónico
emailInput.addEventListener('input', () => {
    const emailValue = emailInput.value.trim();
    if (!emailValue) {
        correoStatus.textContent = patronesValidacion.errorMessages.required;
        correoStatus.className = 'error';
    } else if (!patronesValidacion.isValid('email', emailValue)) {
        correoStatus.textContent = 'Introduce un correo electrónico válido';
        correoStatus.className = 'error';
    } else {
        correoStatus.textContent = 'Correo válido';
        correoStatus.className = 'success';
    }
});

// Validar campo de contraseña
passwordInput.addEventListener('input', () => {
    const passwordValue = passwordInput.value.trim();
    if (!passwordValue) {
        passwordError.textContent = patronesValidacion.errorMessages.required;
        passwordError.className = 'error';
    } else if (!patronesValidacion.isValid('password', passwordValue)) {
        passwordError.textContent = patronesValidacion.errorMessages.password;
        passwordError.className = 'error';
    } else {
        passwordError.textContent = '';
        passwordError.className = '';
    }
});

// Validación al enviar el formulario
form.addEventListener('submit', (event) => {
    let valid = true;

    // Validar correo
    if (!patronesValidacion.isValid('email', emailInput.value.trim())) {
        correoStatus.textContent = 'Introduce un correo electrónico válido';
        correoStatus.className = 'error';
        valid = false;
    }

    // Validar contraseña
    if (!patronesValidacion.isValid('password', passwordInput.value.trim())) {
        passwordError.textContent = patronesValidacion.errorMessages.password;
        passwordError.className = 'error';
        valid = false;
    }

    // Si no es válido, prevenir el envío
    if (!valid) {
        event.preventDefault();
    }
});

