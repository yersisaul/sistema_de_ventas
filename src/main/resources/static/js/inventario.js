
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


