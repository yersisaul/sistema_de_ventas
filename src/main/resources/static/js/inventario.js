

  function editarCategoria(id) {
      const input = document.getElementById('cat-nombre-' + id);
      const btnGuardar = document.getElementById('guardar-' + id);

      input.disabled = false;
      input.classList.remove("border-0");
      btnGuardar.classList.remove("d-none");
  }

  function guardarCategoria(id) {
      const nombre = document.getElementById('cat-nombre-' + id).value;

      fetch(`/admin/inventario/categoria/editar/${id}`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ nombre })
      }).then(() => location.reload());
  }


  // Script para mostrar vista previa de la imagen
  document.addEventListener('DOMContentLoaded', function() {
    const imagenInput = document.getElementById('imagenInput');
    if (imagenInput) {
      imagenInput.addEventListener('change', function(e) {
        const preview = document.getElementById('imagePreview');
        const file = e.target.files[0];
        
        if (file) {
          const reader = new FileReader();
          
          reader.onload = function(e) {
            preview.innerHTML = '';
            const img = document.createElement('img');
            img.src = e.target.result;
            img.style.width = '100%';
            img.style.height = '100%';
            img.style.objectFit = 'contain';
            preview.appendChild(img);
          }
          
          reader.readAsDataURL(file);
        } else {
          preview.innerHTML = '<span class="text-muted">Vista previa</span>';
        }
      });
    }
  });

//editar producto
document.addEventListener("DOMContentLoaded", function () {
  const modalEditar = document.getElementById("modalEditarProducto");

  modalEditar.addEventListener("show.bs.modal", function (event) {
    const button = event.relatedTarget; // botón que abrió el modal
    const form = document.getElementById("formEditarProducto");

    // Obtener datos
    const id = button.getAttribute("data-id");
    const nombre = button.getAttribute("data-nombre");
    const descripcion = button.getAttribute("data-descripcion");
    const categoria = button.getAttribute("data-categoria");
    const imagen = button.getAttribute("data-imagen");

    // Rellenar campos
    document.getElementById("nombreEditar").value = nombre;
    document.getElementById("descripcionEditar").value = descripcion;
    document.getElementById("categoriaSelectEditar").value = categoria;

    const preview = document.getElementById("imagePreviewEditar");
    if (imagen) {
      preview.innerHTML = `<img src="${imagen}" class="w-100 h-100 object-fit-cover">`;
    } else {
      preview.innerHTML = `<span class="text-muted">Vista previa</span>`;
    }

    // Acción del formulario
    form.action = `/admin/inventario/editar/${id}`;
  });
});


//eliminar
document.addEventListener("DOMContentLoaded", () => {
    const modal = document.getElementById("modalEliminarProducto");
    const inputId = document.getElementById("inputIdEliminar");
    const form = document.getElementById("formEliminarProducto");

    modal.addEventListener("show.bs.modal", function (event) {
        const button = event.relatedTarget;
        const id = button.getAttribute("data-id");
        inputId.value = id;
        form.action = `/admin/inventario/eliminar/${id}`;
    });
});


//datos del stock
document.addEventListener("DOMContentLoaded", function () {
  const modalStock = document.getElementById("modalAgregarStock");

  modalStock.addEventListener("show.bs.modal", function (event) {
    const button = event.relatedTarget;

    const id = button.getAttribute("data-id");
    const nombre = button.getAttribute("data-nombre");
    const categoria = button.getAttribute("data-categoria");
    const imagen = button.getAttribute("data-imagen");

    // Rellenar campos
    document.getElementById("productoIdStock").value = id;
    document.getElementById("nombreStock").textContent = nombre;
    document.getElementById("categoriaStock").textContent = `Categoría: ${categoria}`;
    
    const imgPreview = document.getElementById("imagenStockPreview");
    if (imagen) {
      imgPreview.src = imagen;
    } else {
      imgPreview.src = "https://placehold.co/80x80?text=IMG";
    }
  });
});


  // Script para calcular resumen en tiempo real
  document.addEventListener('DOMContentLoaded', function() {
    const stockInputs = document.querySelectorAll('input[name^="stock"]');
    const precioInputs = document.querySelectorAll('input[name^="precio"]');
    
    function calcularResumen() {
      let totalStock = 0;
      let totalPrecio = 0;
      let preciosValidos = 0;
      let tallasConStock = 0;
      
      // Calcular stock total
      stockInputs.forEach(input => {
        const valor = parseInt(input.value) || 0;
        totalStock += valor;
        if (valor > 0) tallasConStock++;
      });
      
      // Calcular precio promedio
      precioInputs.forEach(input => {
        const valor = parseFloat(input.value) || 0;
        if (valor > 0) {
          totalPrecio += valor;
          preciosValidos++;
        }
      });
      
      const precioPromedio = preciosValidos > 0 ? totalPrecio / preciosValidos : 0;
      
      // Actualizar UI
      document.getElementById('totalStock').textContent = totalStock;
      document.getElementById('precioPromedio').textContent = 'S/. ' + precioPromedio.toFixed(2);
      document.getElementById('tallasConStock').textContent = tallasConStock + '/4';
    }
    
    // Agregar event listeners
    stockInputs.forEach(input => {
      input.addEventListener('input', calcularResumen);
    });
    
    precioInputs.forEach(input => {
      input.addEventListener('input', calcularResumen);
    });
    
    // Inicializar cálculo
    calcularResumen();
  });