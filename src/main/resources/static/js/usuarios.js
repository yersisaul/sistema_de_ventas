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

