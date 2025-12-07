document.addEventListener("DOMContentLoaded", () => {
  // Formulario de registro de vendedor

  // Toggle password
  const togglePassword = document.getElementById("togglePassword")
  if (togglePassword) {
    togglePassword.addEventListener("click", function () {
      const passwordInput = document.getElementById("contrasena")
      const type = passwordInput.type === "password" ? "text" : "password"
      passwordInput.type = type
      this.classList.toggle("bi-eye")
      this.classList.toggle("bi-eye-slash")
    })
  }

  // Buscador
  const buscadores = document.querySelectorAll(".buscador")
  buscadores.forEach((buscador) => {
    buscador.addEventListener("input", () => {
      const filtro = buscador.value.trim().toLowerCase()
      const filas = document.querySelectorAll("tbody tr")
      filas.forEach((fila) => {
        const columnas = fila.querySelectorAll("td")
        const textoFila = Array.from(columnas)
          .map((td) => td.textContent.toLowerCase())
          .join(" ")
        if (textoFila.includes(filtro)) {
          fila.style.display = ""
        } else {
          fila.style.display = "none"
        }
      })
    })
  })
})

// Función para registrar vendedor
/*
async function registrarVendedor() {
  console.log("[v0] Iniciando registro de usuario")

  const submitBtn = document.querySelector('#formCliente button[type="submit"]')
  const originalText = submitBtn.innerHTML

  // Mostrar loading
  submitBtn.innerHTML =
    '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Guardando...'
  submitBtn.disabled = true

  try {
    const usuarioData = {
      tipoIdentificacion: document.getElementById("tipoIdentificacion").value,
      nIdentificacion: document.getElementById("nIdentificacion").value,
      nombres: document.getElementById("nombres").value,
      apellidoPaterno: document.getElementById("apellidoPaterno").value,
      apellidoMaterno: document.getElementById("apellidoMaterno").value,
      telefono: document.getElementById("telefono").value,
      correo: document.getElementById("correo").value,
      direccion: document.getElementById("direccion").value,
      contrasena: document.getElementById("contrasena").value,
      rol: "VENDEDOR",
      estado: true,
    }

    console.log("[v0] Datos a enviar:", usuarioData)

    // IMPORTANTE: Usa la URL correcta del endpoint
    const response = await fetch("/admin/usuarios/registrar", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(usuarioData),
    })

    console.log("[v0] Respuesta recibida. Status:", response.status)

    const resultado = await response.json()
    console.log("[v0] Respuesta del servidor:", resultado)

    if (response.ok) {
      console.log("[v0] ✅ Registro exitoso")
      alert("✅ Vendedor registrado correctamente")

      // Cerrar modal
      const modal = window.bootstrap.Modal.getInstance(document.getElementById("modalProducto"))
      if (modal) {
        modal.hide()
      }

      // Limpiar formulario
      document.getElementById("formCliente").reset()
    } else {
      console.error("[v0] ❌ Error del servidor:", resultado)
      alert("❌ Error: " + (resultado.message || "Error desconocido"))
    }
  } catch (error) {
    console.error("[v0] Error de conexión:", error)
    alert("❌ Error de conexión al registrar el vendedor: " + error.message)
  } finally {
    // Restaurar botón
    submitBtn.innerHTML = originalText
    submitBtn.disabled = false
  }
}

*/

// =====================================================
// CRUD CLIENTES - CONEXIÓN AL BACKEND
// =====================================================

const API_URL = "/api/admin/clientes";

// =========================
// 1️⃣ Cargar Lista de Clientes
// =========================
async function cargarClientes() {
    try {
        const response = await fetch(API_URL);
        const clientes = await response.json();

        const tbody = document.querySelector("tbody");
        tbody.innerHTML = "";

        clientes.forEach(cliente => {
            const fila = `
                <tr>
                    <td>${cliente.tipoDocumento}</td>
                    <td>${cliente.numeroDocumento}</td>
                    <td>${cliente.nombre}</td>
                    <td>${cliente.apellidoPaterno} ${cliente.apellidoMaterno ?? ""}</td>
                    <td>${cliente.telefono}</td>
                    <td>${cliente.correo}</td>
                    <td>${cliente.direccion}</td>

                    <td>
                        <a href="#" class="text-dark fw-semibold text-decoration-none me-3"
                           onclick="editarCliente(${cliente.idUsuario})">
                            <i class="bi bi-pencil-square me-1"></i>Editar
                        </a>

                        <a href="#" class="text-danger fw-semibold text-decoration-none"
                           onclick="eliminarCliente(${cliente.idUsuario})">
                            <i class="bi bi-trash3 me-1"></i>Eliminar
                        </a>
                    </td>
                </tr>
            `;
            tbody.innerHTML += fila;
        });

    } catch (error) {
        console.error("❌ Error cargando clientes:", error);
    }
}

document.addEventListener("DOMContentLoaded", cargarClientes);



// =========================
// 2️⃣ Registrar Cliente
// =========================
document.getElementById("formCliente").addEventListener("submit", async function (e) {
    e.preventDefault();

    const nuevoCliente = {
        tipoDocumento: "DNI",
        numeroDocumento: document.getElementById("nIdentificacion").value,
        nombre: document.getElementById("nombres").value,
        apellidoPaterno: document.getElementById("apellidoPaterno").value,
        apellidoMaterno: document.getElementById("nomm").value,
        telefono: document.getElementById("telefono").value,
        correo: document.getElementById("correo").value,
        direccion: document.getElementById("direccion").value,
        contraseña: document.getElementById("contrasena").value
    };

    try {
        const response = await fetch(API_URL + "/create", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(nuevoCliente)
        });

        if (!response.ok) {
            const err = await response.json();
            throw new Error(err.mensaje || "Error al guardar");
        }

        alert("✔ Cliente registrado con éxito");

        // Cerrar modal
        const modal = bootstrap.Modal.getInstance(document.getElementById("modalProducto"));
        modal.hide();

        document.getElementById("formCliente").reset();
        cargarClientes();

    } catch (error) {
        console.error("❌ Error registrando:", error);
        alert("Error al registrar: " + error.message);
    }
});




// =========================
// 3️⃣ Eliminar Cliente
// =========================
async function eliminarCliente(id) {
    if (!confirm("¿Seguro que deseas eliminar este cliente?")) return;

    try {
        const response = await fetch(`${API_URL}/${id}`, { method: "DELETE" });

        if (!response.ok) throw new Error("No se pudo eliminar");

        alert("✔ Cliente eliminado");
        cargarClientes();

    } catch (error) {
        console.error("❌ Error eliminando:", error);
        alert("Error al eliminar cliente");
    }
}
//editar cliente 
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
    document.getElementById("edit_tipoDocumento").value = cliente.tipoDocumento || "";
    document.getElementById("edit_numeroDocumento").value = cliente.numeroDocumento || "";
    document.getElementById("edit_nombre").value = cliente.nombre || "";
    document.getElementById("edit_apellidoPaterno").value = cliente.apellidoPaterno || "";
    document.getElementById("edit_apellidoMaterno").value = cliente.apellidoMaterno || "";
    document.getElementById("edit_telefono").value = cliente.telefono || "";
    document.getElementById("edit_correo").value = cliente.correo || "";
    document.getElementById("edit_direccion").value = cliente.direccion || "";

    // 4. Abrir modal
    const modal = new bootstrap.Modal(document.getElementById("modalEditarCliente"));
    modal.show();
}


//guardar edicion
async function guardarEdicion() {

    const id = document.getElementById("editId").value;

    const data = {
        tipoDocumento: document.getElementById("edit_tipoDocumento").value,
        numeroDocumento: document.getElementById("edit_numeroDocumento").value,
        nombre: document.getElementById("edit_nombre").value,
        apellidoPaterno: document.getElementById("edit_apellidoPaterno").value,
        apellidoMaterno: document.getElementById("edit_apellidoMaterno").value,
        telefono: document.getElementById("edit_telefono").value,
        correo: document.getElementById("edit_correo").value,
        direccion: document.getElementById("edit_direccion").value
    };

    const response = await fetch(`/api/admin/clientes/${id}/update`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    });

    if (!response.ok) {
        alert("Error al actualizar el cliente");
        return;
    }

    alert("✔ Cliente actualizado correctamente");

    // Cerrar modal
    bootstrap.Modal.getInstance(document.getElementById("modalEditarCliente")).hide();

    cargarClientes();
}




