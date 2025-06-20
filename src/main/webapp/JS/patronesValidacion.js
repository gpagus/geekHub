const patronesValidacion = {
    // Patrones de validación
    patterns: {
        nif: /^\d{8}$/,
        email: /^[^\s@]{1,50}@[^\s@]{1,50}\.[^\s@]{2,50}$/,
        cp: /^(?:0[1-9]|[1-4]\d|5[0-2])\d{3}$/,
        password: /^[a-zA-Z\d]{4,100}$/,
        name: /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]{2,20}$/,
        phone: /^[6789]\d{8}$/,
        text: /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]{2,40}$/,
        address: /^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\s,./-]{5,40}$/
    },

    // Funciones de validación
    isValid: function(type, value) {
        return this.patterns[type].test(value);
    },
    
    // Mensajes de error
    errorMessages: {
        nif: 'El NIF debe contener 8 dígitos',
        cp: 'El código postal debe contener 5 dígitos, válidos para España',
        password: 'La contraseña debe tener al menos 4 caracteres',
        name: 'Solo puede contener letras y espacios (2-20 caracteres)',
        phone: 'El número de teléfono debe contener 9 dígitos y comenzar con 6, 7, 8 o 9',
        text: 'Solo puede contener letras y espacios y un máximo de 40 carácteres',
        address: 'Introduce una dirección válida (5-40 carácteres)',
        required: 'Este campo es obligatorio'
    }
};

// Exportar el objeto para su uso en otros archivos
export default patronesValidacion;