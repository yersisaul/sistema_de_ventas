// Botón para abrir el selector de archivos
document.getElementById("btn-subir-imagen").addEventListener("click", () => {
    document.getElementById("input-imagen").click();
});

// Cuando el usuario selecciona la imagen
document.getElementById("input-imagen").addEventListener("change", function (e) {
    const archivo = e.target.files[0];
    if (!archivo) return;

    // Previsualizar la imagen
    const reader = new FileReader();
    reader.onload = function (event) {
        const img = document.getElementById("imagen-cliente");
        img.src = event.target.result;
        img.style.display = "block";

        document.getElementById("placeholder-imagen").style.display = "none";
    };
    reader.readAsDataURL(archivo);

    // Guardamos el archivo para luego enviarlo al backend con el producto
    window.imagenCliente = archivo;
});
