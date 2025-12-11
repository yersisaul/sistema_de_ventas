const API_URL = "/api/admin/clientes";

// =====================================================
// FUNCIONES DE CARGA Y CRUD
// =====================================================

// 1️⃣ Cargar Lista de Clientes (SOLUCIONA el problema de 'undefined')
async function cargarClientes() {
    console.log("Cargando clientes desde el backend...");
    try {
        const response = await fetch(API_URL);
        
        if (!response.ok) {
            throw new Error(`Error al obtener la lista: ${response.status}`);
        }
        
        const clientes = await response.json();
        console.log("Clientes recibidos:", clientes);

        const tbody = document.querySelector("tbody");
        tbody.innerHTML = ""; // Limpia la tabla antes de llenarla

        clientes.forEach(cliente => {
            // **IMPORTANTE:** Usar nombres de propiedades exactos del DTO de Java
            const fila = `
                <tr data-id="${cliente.id}"> 
                    
                    <td>${cliente.tipoIdentificacion}</td> <td>${cliente.nIdentificacion}</td>    <td>${cliente.nombres}</td>          <td>${cliente.apellidos}</td>        <td>${cliente.telefono}</td>
                    <td>${cliente.correo}</td>
                    <td>${cliente.direccion}</td>

                    <td>
                        <a href="#" class="text-dark fw-semibold text-decoration-none me-3"
                           onclick="editarCliente(${cliente.id})">
                            <i class="bi bi-pencil-square me-1"></i>Editar
                        </a>
                        <a href="#" class="text-danger fw-semibold text-decoration-none"
                           onclick="eliminarCliente(${cliente.id})">
                            <i class="bi bi-trash3 me-1"></i>Eliminar
                        </a>
                    </td>
                </tr>
            `;
            tbody.innerHTML += fila;
        });

    } catch (error) {
        console.error("❌ Error en cargarClientes:", error);
        alert("Error al cargar la lista de clientes. Revise la consola.");
    }
}

// 2️⃣ Registrar Cliente
document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("formCliente");

    if (form) {
        form.addEventListener("submit", async function (e) {
            e.preventDefault();

            // Concatenamos apellidos
            let apellidos = document.getElementById("apellidoPaterno").value + " " + 
                            (document.getElementById("apellidoMaterno") ? document.getElementById("apellidoMaterno").value : "");
            
            // **NOTA:** Aquí debes usar los campos del UsuarioCreateDTO que tu backend espera
            const nuevoCliente = {
                tipoIdentificacion: document.getElementById("tipoIdentificacion").value,
                nIdentificacion: document.getElementById("nIdentificacion").value,
                nombres: document.getElementById("nombres").value,
                apellidos: apellidos.trim(), // Enviamos el apellido completo
                telefono: document.getElementById("telefono").value,
                correo: document.getElementById("correo").value,
                direccion: document.getElementById("direccion").value,
                contrasena: document.getElementById("contrasena").value
            };

            try {
                const response = await fetch(API_URL + "/create", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(nuevoCliente)
                });

                if (!response.ok) {
                    const errorData = await response.json();
                    throw new Error(errorData.mensaje || "Error al guardar el cliente.");
                }

                alert("✔ Cliente registrado con éxito");
                // Cerrar modal y recargar lista
                const modalElement = document.getElementById("modalProducto");
                if (modalElement) {
                    const modalInstance = bootstrap.Modal.getInstance(modalElement);
                    if (modalInstance) {
                        modalInstance.hide();
                    }
                }
                form.reset();
                cargarClientes();

            } catch (error) {
                console.error("❌ Error registrando:", error);
                alert("Error al registrar: " + error.message);
            }
        });
    }
});


// 3️⃣ Editar Cliente (SOLUCIONA el problema de relleno del modal)
async function editarCliente(id) {

    // 1. Guardar ID en un input oculto
    document.getElementById("editId").value = id;

    // 2. Obtener datos reales del backend
    const response = await fetch(`/api/admin/clientes/${id}`);
    if (!response.ok) {
        alert("No se pudo obtener datos del cliente");
        return;
    }

    const cliente = await response.json();

    // 3. Llenar modal con la información del cliente
    // ASEGÚRATE DE USAR LOS ID'S DEL MODAL DE EDICIÓN
    document.getElementById("edit_tipoDocumento").value = cliente.tipoIdentificacion || ""; // CORREGIDO
    document.getElementById("edit_numeroDocumento").value = cliente.nIdentificacion || ""; // CORREGIDO
    document.getElementById("edit_nombre").value = cliente.nombres || ""; // CORREGIDO
    document.getElementById("edit_apellidos").value = cliente.apellidos || ""; // CORREGIDO
    document.getElementById("edit_telefono").value = cliente.telefono || "";
    document.getElementById("edit_correo").value = cliente.correo || "";
    document.getElementById("edit_direccion").value = cliente.direccion || "";

    // 4. Abrir modal
    const modal = new bootstrap.Modal(document.getElementById("modalEditarCliente"));
    modal.show();
}

// 4️⃣ Guardar Edición
async function guardarEdicion() {

    const id = document.getElementById("editId").value;
    
    // **NOTA:** Tu modal de edición tiene solo un campo 'edit_apellidos'
    const data = {
        tipoIdentificacion: document.getElementById("edit_tipoDocumento").value,
        nIdentificacion: document.getElementById("edit_numeroDocumento").value,
        nombres: document.getElementById("edit_nombre").value,
        apellidos: document.getElementById("edit_apellidos").value,
        telefono: document.getElementById("edit_telefono").value,
        correo: document.getElementById("edit_correo").value,
        direccion: document.getElementById("edit_direccion").value
    };

    try {
        const response = await fetch(`/api/admin/clientes/${id}/update`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        if (!response.ok) {
            throw new Error("Error en la actualización del servidor.");
        }

        alert("✔ Cliente actualizado correctamente");
        
        // Cerrar modal
        const modalInstance = bootstrap.Modal.getInstance(document.getElementById("modalEditarCliente"));
        if (modalInstance) {
             modalInstance.hide();
        }
        
        cargarClientes(); // Recarga la tabla con los datos nuevos
        
    } catch (error) {
        console.error("❌ Error al actualizar:", error);
        alert("Error al actualizar el cliente: " + error.message);
    }
}


// 5️⃣ Eliminar Cliente (SOLUCIONA el problema del botón que no llamaba)
async function eliminarCliente(id) {
    if (!confirm("¿Seguro que deseas eliminar este cliente?")) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });

        if (!response.ok) throw new Error("No se pudo eliminar el cliente.");

        alert("✔ Cliente eliminado");
        cargarClientes(); // Recarga la tabla
        
    } catch (error) {
        console.error("❌ Error eliminando:", error);
        alert("Error al eliminar cliente: " + error.message);
    }
}


// =====================================================
// FUNCIONES DE INICIALIZACIÓN Y BUSCADOR
// =====================================================
document.addEventListener("DOMContentLoaded", () => {
    
    // Inicia la carga de la tabla al cargar la página
    cargarClientes(); 

    // Buscador (SOLUCIONA el problema de la búsqueda si la tabla se carga)
    const buscadores = document.querySelectorAll(".buscador");
    buscadores.forEach((buscador) => {
        buscador.addEventListener("input", () => {
            const filtro = buscador.value.trim().toLowerCase();
            const filas = document.querySelectorAll("tbody tr"); // Busca en las filas que cargarClientes creó
            
            filas.forEach((fila) => {
                const columnas = fila.querySelectorAll("td");
                
                // Excluye la última columna (Acciones) al crear el texto de filtro
                const textoFila = Array.from(columnas)
                    .slice(0, -1) // Ignora la última celda (Acciones)
                    .map((td) => td.textContent.toLowerCase())
                    .join(" ");

                if (textoFila.includes(filtro)) {
                    fila.style.display = "";
                } else {
                    fila.style.display = "none";
                }
            });
        });
    });
    
    // Lógica para toggle password (opcional, si está en este archivo)
    const togglePassword = document.getElementById("togglePassword");
    if (togglePassword) {
        togglePassword.addEventListener("click", function () {
            const passwordInput = document.getElementById("contrasena");
            const type = passwordInput.type === "password" ? "text" : "password";
            passwordInput.type = type;
            this.classList.toggle("bi-eye");
            this.classList.toggle("bi-eye-slash");
        });
    }
    
    // Si tenías código de eliminación de eventos antiguos, asegúrate de que esté eliminado.
});