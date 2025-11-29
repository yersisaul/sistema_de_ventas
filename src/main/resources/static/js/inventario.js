
document.addEventListener('DOMContentLoaded', ()=>{
const nombreInput = document.getElementById('nombre');
  nombreInput.addEventListener('input', () => {
    nombreInput.value = nombreInput.value.replace(/[^a-zA-Z\s]/g, '');
  })
   const number = document.getElementById('number');
  number.addEventListener('input', () =>{
    number.value = number.value.replace(/[^0-9]/g, '');
  })
});

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

