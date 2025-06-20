document.addEventListener('DOMContentLoaded', () => {

    // Desactivar los botones de restar si la cantidad es 1 al cargar la página
    document.querySelectorAll('.quantity-btn[data-action="restar"]').forEach(boton => {
        const productoId = boton.getAttribute('data-id'); // Obtenemos el ID del producto
        const cantidadElemento = document.getElementById(`cantidad-${productoId}`); // Buscamos el elemento que tiene la cantidad
        const cantidad = cantidadElemento ? parseInt(cantidadElemento.textContent) : 0; // Leemos la cantidad y la convertimos a entero

        if (cantidad === 1) {
            boton.disabled = true; // Desactiva el botón si la cantidad es 1
            boton.style.opacity = 0.5;
        }
    });

    document.querySelectorAll('.quantity-btn').forEach(boton => {
        boton.addEventListener('click', async (event) => {

            // Verificar si el botón está bloqueado
            if (boton.dataset.bloqueado === "true") {
                console.log("El botón está bloqueado temporalmente.");
                return;
            }

            // Bloquear el botón para evitar múltiples clics
            boton.dataset.bloqueado = "true";
            boton.style.pointerEvents = "none"; // Deshabilita interacciones visuales

            const productoId = event.target.getAttribute('data-id');
            const accion = event.target.getAttribute('data-action');

            if (!productoId || !accion) {
                console.error('No se pudo obtener el ID o la acción del botón.');
                return;
            }

            const params = new URLSearchParams();
            params.append('idProducto', productoId);
            params.append('accion', accion);

            try {
                const response = await fetch('AjaxCantidad', {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                        "Accept-Charset": "utf-8"
                    },
                    body: params.toString()
                });

                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                const responseData = await response.json();
                console.log('Respuesta del servidor:', responseData);

                // Update DOM elements
                document.getElementById('base-imponible').textContent = responseData.baseImpo + " €";
                document.getElementById('iva').textContent = responseData.iva + " €";
                document.getElementById('importe-total').textContent = responseData.totalPagar + " €";
                document.getElementById(`cantidad-${productoId}`).textContent = responseData.cantidad;
                document.querySelector(`tr:has(button[data-id="${productoId}"]) td:nth-child(7)`).textContent = responseData.importe + " €";


                const botonRestar = document.querySelector(`button[data-id="${productoId}"][data-action="restar"]`);
                if (responseData.cantidad == "1") {
                    botonRestar.disabled = true;
                    botonRestar.style.opacity = 0.5;
                } else {
                    botonRestar.disabled = false;
                    botonRestar.style.opacity = 1;
                }

            } catch (error) {
                console.error("Error al obtener los resultados:", error);

            } finally {
                // Desbloquea el botón después del tiempo establecido
                setTimeout(() => {
                    boton.dataset.bloqueado = "false";
                    boton.style.pointerEvents = "auto"; // Habilita interacciones visuales
                }, 500);
            }
        });
    });
});