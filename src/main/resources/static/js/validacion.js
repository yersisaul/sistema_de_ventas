
  //Observar contraseñ
const togglePassword = document.getElementById('togglePassword');
  const passwordInput = document.getElementById('password');

  togglePassword.addEventListener('click', () => {
    const isPassword = passwordInput.type === 'password';
    passwordInput.type = isPassword ? 'text' : 'password';
    togglePassword.classList.toggle('bi-eye');
    togglePassword.classList.toggle('bi-eye-slash');
  });
  
  const nombreInput = document.getElementById('nombres');
  nombreInput.addEventListener('input', () => {
    nombreInput.value = nombreInput.value.replace(/[^a-zA-Z\s]/g, '');
  })
  const apellidoP = document.getElementById('apellidoPaterno');
  apellidoP.addEventListener('input', () => {
    apellidoP.value = apellidoP.value.replace(/[^a-zA-Z\s]/g, '');
  })
  const apellidoM = document.getElementById('apellidoMaterno');
  apellidoM.addEventListener('input', () => {
    apellidoM.value = apellidoM.value.replace(/[^a-zA-Z\s]/g, '');
  })
  const number = document.getElementById('nIdentificacion');
  number.addEventListener('input', () =>{
    number.value = number.value.replace(/[^0-9]/g, '');
  })
  const cel = document.getElementById('telefono');
  cel.addEventListener('input', () =>{
    cel.value = cel.value.replace(/[^0-9]/g, '');
  })

  console.log("✅ validacion.js cargado correctamente");





  
