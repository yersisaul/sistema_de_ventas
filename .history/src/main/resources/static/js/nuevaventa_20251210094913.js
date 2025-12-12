// ==========================================
// 🔹 NUEVA VENTA - GESTIÓN COMPLETA
// ==========================================

document.addEventListener("DOMContentLoaded", () => {
  console.log('✅ JavaScript cargado correctamente');
  
  // ==========================================
  // VARIABLES GLOBALES
  // ==========================================
  let productos = [];
  let clienteSeleccionado = null;
  let productosDisponibles = [];
  
  // ==========================================
  // 🔹 FUNCIONES DE FECHA Y COMPROBANTE
  // ==========================================

  // Actualizar fecha y hora en tiempo real
  function actualizarFechaHora() {
    const ahora = new Date();
    
    // Formato para datetime-local: YYYY-MM-DDTHH:MM
    const year = ahora.getFullYear();
    const month = String(ahora.getMonth() + 1).padStart(2, '0');
    const day = String(ahora.getDate()).padStart(2, '0');
    const hours = String(ahora.getHours()).padStart(2, '0');
    const minutes = String(ahora.getMinutes()).padStart(2, '0');
    
    const fechaHoraISO = `${year}-${month}-${day}T${hours}:${minutes}`;
    
    // Actualizar input de fecha
    const inputFecha = document.querySelector('input[type="datetime-local"]');
    if (inputFecha) {
      inputFecha.value = fechaHoraISO;
    }
    
    // Formato para mostrar: DD/MM/YYYY - HH:MM
    const fechaFormateada = `${day}/${month}/${year} - ${hours}:${minutes}`;
    
    // Actualizar texto de fecha en el header
    const smallFecha = document.querySelector('.comprobante-header small');
    if (smallFecha) {
      smallFecha.textContent = `Fecha: ${fechaFormateada}`;
    }
  }

  // Obtener siguiente número de comprobante desde el backend
  async function obtenerSiguienteNumeroComprobante(tipo) {
    try {
      console.log(`🔹 Solicitando número de comprobante para ${tipo}...`);
      
      const response = await fetch(`/admin/ventas/api/siguiente-numero?tipo=${tipo}`);
      
      if (!response.ok) {
        throw new Error(`Error HTTP: ${response.status}`);
      }
      
      const data = await response.json();
      console.log('✅ Número obtenido:', data);
      
      return data;
      
    } catch (error) {
      console.error('❌ Error al obtener número:', error);
      
      // Fallback: generar número temporal
      const serie = tipo === 'BOLETA' ? 'B001' : 'F001';
      const numero = String(Math.floor(Math.random() * 100000)).padStart(8, '0');
      
      return {
        numeroCompleto: `${serie}-${numero}`,
        tipo: tipo,
        serie: serie,
        numero: numero
      };
    }
  }

  // Actualizar número de comprobante en la UI
  async function actualizarNumeroComprobante(tipo) {
    const numeroElement = document.querySelector('.numero-comprobante');
    
    if (!numeroElement) {
      console.warn('⚠️ Elemento .numero-comprobante no encontrado');
      return;
    }
    
    // Mostrar loading
    numeroElement.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Generando...';
    
    try {
      const data = await obtenerSiguienteNumeroComprobante(tipo);
      numeroElement.textContent = data.numeroCompleto;
      console.log(`✅ Número de comprobante actualizado: ${data.numeroCompleto}`);
      
    } catch (error) {
      console.error('❌ Error al actualizar número:', error);
      numeroElement.textContent = tipo === 'BOLETA' ? 'B001-00000000' : 'F001-00000000';
    }
  }
  // ==========================================
  // 🔹 GESTIÓN DE CLIENTES
  // ==========================================
  
  // Inicializar event listeners de clientes al cargar
  function agregarEventListenersClientes() {
    document.querySelectorAll('.seleccionar-cliente').forEach(btn => {
      btn.addEventListener('click', function() {
        const cliente = {
          id: this.dataset.id,
          nombre: this.dataset.nombre,
          documento: this.dataset.documento,
          tipoDoc: this.dataset.tipoDoc,
          email: this.dataset.email,
          telefono: this.dataset.telefono,
          direccion: this.dataset.direccion
        };
        
        clienteSeleccionado = cliente;
        
        document.querySelector('.cliente-seleccionado h6').textContent = cliente.nombre.toUpperCase();
        document.querySelector('.cliente-seleccionado p').textContent = 
          `Documento: ${cliente.tipoDoc} ${cliente.documento} | Email: ${cliente.email}`;
        
        const modalElement = document.getElementById('modalClientes');
        const modalInstance = bootstrap.Modal.getInstance(modalElement);
        if (modalInstance) modalInstance.hide();
      });
    });
  }
  
  // Buscar clientes con AJAX
  function buscarClientesAjax() {
    const busqueda = document.getElementById('inputBuscarCliente').value.trim();
    
    if (busqueda.length === 0) {
      location.reload();
      return;
    }
    
    fetch(`/api/admin/clientes/buscar?q=${encodeURIComponent(busqueda)}`)
      .then(response => {
        if (!response.ok) throw new Error('Error en la búsqueda');
        return response.json();
      })
      .then(clientes => {
        actualizarTablaClientes(clientes);
      })
      .catch(error => {
        console.error('Error al buscar clientes:', error);
        alert('Error al buscar clientes. Intente nuevamente.');
      });
  }
  
  // Actualizar tabla de clientes en el modal
  function actualizarTablaClientes(clientes) {
    const tbody = document.getElementById('tablaClientesBody');
    
    if (clientes.length === 0) {
      tbody.innerHTML = '<tr><td colspan="4" class="text-center text-muted py-4">No se encontraron clientes</td></tr>';
      return;
    }
    
    tbody.innerHTML = '';
    clientes.forEach(cliente => {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td>
          <strong>${cliente.nombreCompleto}</strong><br>
          <small>Cliente</small>
        </td>
        <td>${cliente.documentoFormateado}</td>
        <td>${cliente.correo}</td>
        <td>
          <button class="btn btn-sm btn-dark seleccionar-cliente"
                  data-id="${cliente.id}"
                  data-nombre="${cliente.nombreCompleto}"
                  data-documento="${cliente.nIdentificacion}"
                  data-tipo-doc="${cliente.tipoIdentificacion}"
                  data-email="${cliente.correo}"
                  data-telefono="${cliente.telefono || ''}"
                  data-direccion="${cliente.direccion || ''}">
            Seleccionar
          </button>
        </td>
      `;
      tbody.appendChild(tr);
    });
    
    agregarEventListenersClientes();
  }
  
  // Event listeners para búsqueda de clientes
  const btnBuscarCliente = document.getElementById('btnBuscarCliente');
  const inputBuscarCliente = document.getElementById('inputBuscarCliente');
  
  if (btnBuscarCliente) {
    btnBuscarCliente.addEventListener('click', buscarClientesAjax);
  }
  
  if (inputBuscarCliente) {
    inputBuscarCliente.addEventListener('keypress', function(e) {
      if (e.key === 'Enter') {
        buscarClientesAjax();
      }
    });
  }
  
  // Botón para mostrar formulario de nuevo cliente
  const btnClienteNuevo = document.getElementById('btnClienteNuevo');
  if (btnClienteNuevo) {
    btnClienteNuevo.addEventListener('click', function() {
      document.querySelector('.cliente-seleccionado').style.display = 'none';
      document.getElementById('formCliente').style.display = 'block';
      const modalElement = document.getElementById('modalClientes');
      const modalInstance = bootstrap.Modal.getInstance(modalElement);
      if (modalInstance) modalInstance.hide();
    });
  }
  
  // Botón para quitar cliente seleccionado
  const btnQuitarCliente = document.getElementById('btnQuitarCliente');
  if (btnQuitarCliente) {
    btnQuitarCliente.addEventListener('click', function() {
      clienteSeleccionado = null;
      document.querySelector('.cliente-seleccionado h6').textContent = 'CLIENTE OCASIONAL';
      document.querySelector('.cliente-seleccionado p').textContent = 
        'Seleccione un cliente o ingrese los datos manualmente';
      document.getElementById('formCliente').style.display = 'none';
    });
  }
  
  // ==========================================
  // 🔹 GESTIÓN DE COMPROBANTE
  // ==========================================
  document.getElementById('boleta').addEventListener('change', function() {
    if (this.checked) {
      document.getElementById('datosFactura').style.display = 'none';
      actualizarNumeroComprobante('BOLETA');  // ← Cambio importante: 'BOLETA' no 'B001'
    }
  });

  document.getElementById('factura').addEventListener('change', function() {
    if (this.checked) {
      document.getElementById('datosFactura').style.display = 'block';
      actualizarNumeroComprobante('FACTURA');  // ← Cambio importante: 'FACTURA' no 'F001'
    }
  });
    
  // ==========================================
  // 🔹 CARGAR PRODUCTOS DESDE BASE DE DATOS
  // ==========================================
  
  function cargarProductosDesdeDB() {
    console.log('🔹 Cargando productos desde la BD...');
    const listaModalProductos = document.getElementById('listaModalProductos');
    
    // Mostrar loading
    listaModalProductos.innerHTML = `
      <div class="col-12 text-center py-5">
        <div class="spinner-border text-dark" role="status">
          <span class="visually-hidden">Cargando...</span>
        </div>
        <p class="text-muted mt-3">Cargando productos...</p>
      </div>
    `;
    
    fetch('/api/admin/productos/ventas')
      .then(response => {
        console.log('📡 Respuesta recibida:', response);
        if (!response.ok) throw new Error('Error al cargar productos');
        return response.json();
      })
      .then(data => {
        console.log('📦 Productos recibidos:', data);
        productosDisponibles = data;
        mostrarProductosEnModal(data);
      })
      .catch(error => {
        console.error('❌ Error al cargar productos:', error);
        listaModalProductos.innerHTML = `
          <div class="col-12 text-center py-5">
            <i class="bi bi-exclamation-circle fs-1 text-danger"></i>
            <p class="text-danger mt-3">Error al cargar los productos</p>
            <button class="btn btn-dark btn-sm" onclick="location.reload()">
              <i class="bi bi-arrow-clockwise me-2"></i>Reintentar
            </button>
          </div>
        `;
      });
  }
  
  // Mostrar productos en el modal
  function mostrarProductosEnModal(productosDB) {
    const listaModalProductos = document.getElementById('listaModalProductos');
    
    if (productosDB.length === 0) {
      listaModalProductos.innerHTML = `
        <div class="col-12 text-center py-5">
          <i class="bi bi-inbox fs-1 text-muted"></i>
          <p class="text-muted mt-3">No hay productos disponibles</p>
        </div>
      `;
      return;
    }
    
    listaModalProductos.innerHTML = '';
    
    productosDB.forEach(producto => {
      // Calcular stock total de todas las variantes
      const stockTotal = producto.variantes && producto.variantes.length > 0 ? 
        producto.variantes.reduce((sum, v) => sum + (v.stock || 0), 0) : 0;
      
      const stockClass = stockTotal === 0 ? 'stock-agotado' : 
                        stockTotal < 10 ? 'stock-bajo' : 'stock-bueno';
      
      // Generar botones de tallas
      let tallasHTML = '';
      if (producto.variantes && producto.variantes.length > 0) {
        tallasHTML = '<div class="tallas-container mt-2">';
        producto.variantes.forEach(variante => {
          const tallaDisabled = variante.stock === 0 ? 'disabled' : '';
          const tallaClass = variante.stock === 0 ? 'talla-agotada' : 'talla-disponible';
          tallasHTML += `
            <button class="btn-talla ${tallaClass}" 
                    ${tallaDisabled}
                    data-producto-id="${producto.id}"
                    data-variante-id="${variante.id}"
                    data-talla="${variante.talla}"
                    data-color="${variante.color || ''}"
                    data-precio="${variante.precio}"
                    data-stock="${variante.stock}"
                    data-nombre="${producto.nombre}"
                    data-sku="${producto.sku}"
                    data-url-imagen="${producto.url_imagen || ''}">
              ${variante.talla}
            </button>
          `;
        });
        tallasHTML += '</div>';
      }
      
      const col = document.createElement('div');
      col.className = 'col-md-4 mb-3';
      col.innerHTML = `
        <div class="card-producto" style="padding: 15px; border: 1px solid #dee2e6; border-radius: 8px;">
          <div class="d-flex align-items-start mb-2">
            <div style="width: 60px; height: 60px; background: #f0f0f0; border-radius: 8px; margin-right: 10px; overflow: hidden;">
              ${producto.url_imagen ? 
                `<img src="${producto.url_imagen}" style="width:100%; height:100%; object-fit:cover;" alt="${producto.nombre}">` : 
                '<i class="bi bi-image fs-3 d-flex align-items-center justify-content-center h-100 text-muted"></i>'
              }
            </div>
            <div style="flex: 1;">
              <h6 class="fw-bold mb-1">${producto.nombre}</h6>
              <p class="text-muted mb-1 small">SKU: ${producto.sku || 'N/A'}</p>
              <div class="d-flex justify-content-between align-items-center">
                <strong class="text-dark">S/. ${Number(producto.precioMinimo || 0).toFixed(2)}</strong>
                <span class="stock-badge ${stockClass}">Stock: ${stockTotal}</span>
              </div>
            </div>
          </div>
          ${tallasHTML}
        </div>
      `;
      listaModalProductos.appendChild(col);
    });
    
    console.log(`✅ ${productosDB.length} productos mostrados`);
    agregarEventListenersTallas();
  }
  
  // Event listeners para botones de talla
  function agregarEventListenersTallas() {
    document.querySelectorAll('.btn-talla').forEach(btn => {
      btn.addEventListener('click', function() {
        if (this.disabled) {
          alert('Talla agotada');
          return;
        }
        
        const variante = {
          productoId: parseInt(this.dataset.productoId),
          varianteId: parseInt(this.dataset.varianteId),
          nombre: this.dataset.nombre,
          sku: this.dataset.sku,
          talla: this.dataset.talla,
          precio: parseFloat(this.dataset.precio),
          stock: parseInt(this.dataset.stock),
          url_imagen: this.dataset.urlImagen || null 
        };
        
        console.log('🛒 Agregando variante:', variante);
        agregarVarianteAVenta(variante);
        
        // Efecto visual
        this.style.background = '#4CAF50';
        this.style.color = 'white';
        setTimeout(() => {
          this.style.background = '';
          this.style.color = '';
        }, 300);
      });
    });
  }

  // Agregar variante a la venta
  function agregarVarianteAVenta(variante) {
    if (variante.stock === 0 || isNaN(variante.stock)) {
      alert('Talla sin stock disponible');
      return;
    }
    
    // Buscar si ya existe esta variante específica
    const existe = productos.find(p => p.varianteId === variante.varianteId);
    
    if (existe) {
      if (existe.cantidad < variante.stock) {
        existe.cantidad += 1;
        existe.subtotal = existe.precio * existe.cantidad;
      } else {
        alert(`Stock máximo alcanzado (${variante.stock} unidades) para talla ${variante.talla}`);
        return;
      }
    } else {
      productos.push({
        id: variante.productoId,
        varianteId: variante.varianteId,
        nombre: `${variante.nombre} - Talla ${variante.talla}`,
        sku: variante.sku,
        talla: variante.talla,
        precio: variante.precio,
        stock: variante.stock,
        cantidad: 1,
        subtotal: variante.precio,
        url_imagen: variante.url_imagen || null
      });
    }
    
    console.log('✅ Variante agregada. Total productos:', productos.length);
    actualizarListaProductos();
    calcularTotales();
  }
  
  // ==========================================
  // 🔹 GESTIÓN DE PRODUCTOS EN LA VENTA
  // ==========================================
  
  // Eliminar producto
  document.addEventListener('click', function(e) {
    if (e.target.closest('.btn-eliminar')) {
      const fila = e.target.closest('.producto-item');
      const itemId = parseInt(fila.dataset.id);
      
      // Buscar por varianteId si existe, sino por id
      productos = productos.filter(p => (p.varianteId || p.id) !== itemId);
      actualizarListaProductos();
      calcularTotales();
    }
  });
  
  // Actualizar cantidad de producto
  document.addEventListener('input', function(e) {
    if (e.target.classList.contains('input-cantidad')) {
      const itemId = parseInt(e.target.dataset.id);
      const nuevaCantidad = parseInt(e.target.value) || 1;
      
      const producto = productos.find(p => (p.varianteId || p.id) === itemId);
      if (producto) {
        if (nuevaCantidad > producto.stock) {
          alert(`Stock máximo: ${producto.stock} unidades`);
          e.target.value = producto.stock;
          producto.cantidad = producto.stock;
        } else if (nuevaCantidad < 1) {
          e.target.value = 1;
          producto.cantidad = 1;
        } else {
          producto.cantidad = nuevaCantidad;
        }
        
        producto.subtotal = producto.precio * producto.cantidad;
        actualizarListaProductos();
        calcularTotales();
      }
    }
  });
  
  // Actualizar lista visual de productos
  function actualizarListaProductos() {
    const lista = document.getElementById('listaProductos');
    const sinProductos = document.getElementById('sinProductos');
    
    if (productos.length === 0) {
      sinProductos.style.display = 'block';
      lista.innerHTML = '';
      return;
    }
    
    sinProductos.style.display = 'none';
    
    let html = '';
    productos.forEach(prod => {
      html += `
        <div class="producto-item" data-id="${prod.varianteId || prod.id}">
          <div class="row align-items-center g-3">
            <!-- Columna 1: Info del producto (5 columnas) -->
            <div class="col-md-5">
              <div class="d-flex align-items-start">
                <!-- Imagen del producto -->
                <div class="me-3 flex-shrink-0" 
                    style="width: 60px; height: 60px; background: #f5f5f5; border-radius: 8px; overflow: hidden; display: flex; align-items: center; justify-content: center;">
                  ${prod.url_imagen ? 
                    `<img src="${prod.url_imagen}" alt="${prod.nombre}" style="width: 100%; height: 100%; object-fit: cover;">` :
                    '<i class="bi bi-image text-muted" style="font-size: 24px;"></i>'
                  }
                </div>
                
                <!-- Detalles del producto -->
                <div style="flex: 1; min-width: 0;">
                  <h6 class="mb-2">${prod.nombre}</h6>
                  <div class="d-flex flex-wrap gap-2">
                    <span class="badge-sku">SKU: ${prod.sku}</span>
                    ${prod.talla ? `<span class="badge-sku">Talla: ${prod.talla}</span>` : ''}
                    ${prod.color ? `<span class="badge-sku">Color: ${prod.color}</span>` : ''}
                  </div>
                </div>
              </div>
            </div>
            
            <!-- Columna 2: Cantidad (2 columnas) -->
            <div class="col-md-2 text-center">
              <input type="number" 
                    class="form-control input-cantidad mx-auto" 
                    value="${prod.cantidad}" 
                    min="1" 
                    max="${prod.stock}"
                    data-id="${prod.varianteId || prod.id}">
              <small class="text-muted d-block mt-1" style="font-size: 11px;">Máx: ${prod.stock}</small>
            </div>
            
            <!-- Columna 3: Precio unitario (2 columnas) -->
            <div class="col-md-2 text-center">
              <div class="precio-unitario">S/. ${prod.precio.toFixed(2)}</div>
              <small class="text-label-small">Precio unitario</small>
            </div>
            
            <!-- Columna 4: Subtotal (2 columnas) -->
            <div class="col-md-2 text-center">
              <div class="precio-subtotal">S/. ${prod.subtotal.toFixed(2)}</div>
              <small class="text-label-small">Subtotal</small>
            </div>
            
            <!-- Columna 5: Eliminar (1 columna) -->
            <div class="col-md-1 text-center">
              <button class="btn-eliminar mx-auto" title="Eliminar producto">
                <i class="bi bi-trash"></i>
              </button>
            </div>
          </div>
        </div>
      `;
    });
    
    lista.innerHTML = html;
    console.log(`✅ Lista actualizada: ${productos.length} productos`);
  }
  
  // ==========================================
  // 🔹 CÁLCULOS Y TOTALES
  // ==========================================
  
  function calcularTotales() {
  const subtotal = productos.reduce((sum, prod) => sum + prod.subtotal, 0);
  const descuentoInput = document.getElementById('descuento');
  const tipoDescuento = document.getElementById('tipoDescuento').value;
  let descuento = parseFloat(descuentoInput.value) || 0;
  
  if (tipoDescuento === 'porcentaje') {
    descuento = subtotal * (descuento / 100);
  }

  const subtotalConDescuento = subtotal - descuento;
  const baseImponible = subtotalConDescuento / 1.18;
  const igv = baseImponible * 0.18;
  const total = subtotalConDescuento;
  
  // Actualizar en pantalla
  document.getElementById('subtotal').textContent = `S/. ${baseImponible.toFixed(2)}`;
  document.getElementById('igv').textContent = `S/. ${igv.toFixed(2)}`;
  document.getElementById('total').textContent = `S/. ${total.toFixed(2)}`;
  
  // Actualizar vuelto si es efectivo
  const metodoPago = document.getElementById('metodoPago').value;
  if (metodoPago === 'efectivo') {
    const campoVuelto = document.querySelector('#campoVuelto input');
    if (campoVuelto) {
      const recibido = parseFloat(campoVuelto.value) || 0;
      const vuelto = recibido - total;
      
      const montoRecibidoEl = document.getElementById('montoRecibido');
      const vueltoEl = document.getElementById('vuelto');
      
      if (montoRecibidoEl) {
        montoRecibidoEl.textContent = `S/. ${recibido.toFixed(2)}`;
      }
      
      if (vueltoEl) {
        if (vuelto >= 0) {
          vueltoEl.textContent = `S/. ${vuelto.toFixed(2)}`;
          vueltoEl.className = 'text-success';
        } else {
          vueltoEl.textContent = `Faltan S/. ${Math.abs(vuelto).toFixed(2)}`;
          vueltoEl.className = 'text-danger';
        }
      }
    }
  }
  return {
    subtotal: baseImponible,
    impuestos: igv,
    total: total,
    descuento: descuento
  };
}
  
  // ==========================================
  // 🔹 MÉTODO DE PAGO
  // ==========================================
  
  document.getElementById('metodoPago').addEventListener('change', function() {
    const campoVuelto = document.getElementById('campoVuelto');
    const infoVuelto = document.getElementById('infoVuelto');
    const metodoTexto = document.getElementById('metodoPagoTexto');
    
    if (this.value === 'efectivo') {
      campoVuelto.style.display = 'block';
      infoVuelto.style.display = 'block';
      metodoTexto.textContent = 'Efectivo';
    } else {
      campoVuelto.style.display = 'none';
      infoVuelto.style.display = 'none';
      metodoTexto.textContent = this.options[this.selectedIndex].text;
    }
  });
  
  // Calcular vuelto al escribir monto recibido
  const inputMontoRecibido = document.querySelector('#campoVuelto input');
  if (inputMontoRecibido) {
    inputMontoRecibido.addEventListener('input', calcularTotales);
  }
  
  // ==========================================
  // 🔹 DESCUENTO
  // ==========================================
  
  document.getElementById('descuento').addEventListener('input', calcularTotales);
  document.getElementById('tipoDescuento').addEventListener('change', calcularTotales);
  
  // ==========================================
  // 🔹 GUARDAR VENTA
  // ==========================================

  async function guardarVenta(imprimir = false) {
    // Validaciones
    if (productos.length === 0) {
      alert('❌ Debe agregar al menos un producto');
      return;
    }
    
    let clienteId = clienteSeleccionado ? clienteSeleccionado.id : null;
    
    if (!clienteId) {
      alert('⚠️ Debe seleccionar un cliente');
      return;
    }
    
    const tipoComprobante = document.getElementById('boleta').checked ? 'BOLETA' : 'FACTURA';
    const totales = calcularTotales();
    console.log('📊 Totales calculados:');
    console.log('  - Subtotal (sin IGV):', totales.subtotal);
    console.log('  - Impuestos (IGV):', totales.impuestos);
    console.log('  - Total (con IGV):', totales.total);
    console.log('  - Descuento:', totales.descuento);
    // Preparar datos
    const pedidoData = {
      clienteId: parseInt(clienteId),
      tipoComprobante: tipoComprobante,
      subtotal: totales.subtotal,
      impuestos: totales.impuestos,
      total: totales.total,
      detalles: productos.map(prod => ({
        varianteProductoId: prod.varianteId,
        cantidad: prod.cantidad
      })),
      costoEnvio: 0,
      direccionEnvio: clienteSeleccionado?.direccion || 'Recojo en tienda',
      telefonoContacto: clienteSeleccionado?.telefono || '000000000',
      notas: document.getElementById('observaciones')?.value || ''
    };
    
    console.log('💾 Enviando datos:', pedidoData);
    
    // Mostrar loading
    const btnGuardar = document.getElementById('btnGuardar');
    const btnImprimir = document.getElementById('btnImprimir');
    const textoOriginal = btnGuardar.innerHTML;
    
    btnGuardar.disabled = true;
    btnImprimir.disabled = true;
    btnGuardar.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Guardando...';
    
    try {
      const response = await fetch('/admin/ventas/api/guardar', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(pedidoData)
      });
      
      const data = await response.json();
      
      if (response.ok && data.success) {
        console.log('✅ Venta guardada:', data);
        
        // Mostrar notificación
        mostrarNotificacion(
          '✅ Venta Registrada Exitosamente',
          `Comprobante: ${data.numeroComprobante}<br>Total: S/. ${data.total.toFixed(2)}`,
          'success'
        );
        
        // Si se debe imprimir, abrir modal
        if (imprimir) {
          setTimeout(() => {
            abrirModalImpresion(data.pedidoId);
          }, 1000);
        }
        
        // Resetear formulario después de 2 segundos
        setTimeout(() => {
          resetearFormulario();
        }, 2000);
        
      } else {
        throw new Error(data.mensaje || 'Error al guardar la venta');
      }
      
    } catch (error) {
      console.error('❌ Error:', error);
      mostrarNotificacion(
        '❌ Error al Guardar',
        error.message,
        'error'
      );
      
      btnGuardar.disabled = false;
      btnImprimir.disabled = false;
      btnGuardar.innerHTML = textoOriginal;
    }
  }

  // Función para mostrar notificaciones
  function mostrarNotificacion(titulo, mensaje, tipo) {
    const notificacion = document.createElement('div');
    notificacion.className = `alert alert-${tipo === 'success' ? 'success' : 'danger'} alert-dismissible fade show`;
    notificacion.style.cssText = `
      position: fixed;
      top: 20px;
      right: 20px;
      z-index: 9999;
      min-width: 300px;
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    `;
    notificacion.innerHTML = `
      <strong>${titulo}</strong><br>
      ${mensaje}
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(notificacion);
    
    setTimeout(() => {
      notificacion.remove();
    }, 5000);
  }

  // Función para resetear el formulario
  function resetearFormulario() {
    console.log('🔄 Reseteando formulario...');
    
    productos = [];
    actualizarListaProductos();
    
    clienteSeleccionado = null;
    document.querySelector('.cliente-seleccionado h6').textContent = 'CLIENTE OCASIONAL';
    document.querySelector('.cliente-seleccionado p').textContent = 
      'Seleccione un cliente o ingrese los datos manualmente';
    document.getElementById('formCliente').style.display = 'none';
    
    document.getElementById('descuento').value = '0.00';
    document.getElementById('tipoDescuento').value = 'fijo';
    
    document.getElementById('metodoPago').value = 'efectivo';
    document.getElementById('campoVuelto').style.display = 'none';
    document.getElementById('infoVuelto').style.display = 'none';
    
    const inputMontoRecibido = document.querySelector('#campoVuelto input');
    if (inputMontoRecibido) {
      inputMontoRecibido.value = '';
    }
    
    const observaciones = document.getElementById('observaciones');
    if (observaciones) {
      observaciones.value = '';
    }
    
    document.getElementById('boleta').checked = true;
    document.getElementById('datosFactura').style.display = 'none';
    
    calcularTotales();
    actualizarNumeroComprobante('BOLETA');
    
    document.getElementById('btnGuardar').disabled = false;
    document.getElementById('btnImprimir').disabled = false;
    document.getElementById('btnGuardar').innerHTML = '<i class="bi bi-save me-2"></i>Guardar Venta';
    
    console.log('✅ Formulario reseteado');
  }

  // Event listeners
  document.getElementById('btnGuardar').addEventListener('click', function() {
    guardarVenta(false);
  });

  document.getElementById('btnImprimir').addEventListener('click', function() {
    guardarVenta(true);
  });

  const btnGenerar = document.getElementById('btnGenerar');
  if (btnGenerar) {
    btnGenerar.addEventListener('click', function() {
      guardarVenta(false);
    });
  }
  // Abrir modal de impresión
  async function abrirModalImpresion(pedidoId) {
    console.log('🖨️ Abriendo modal de impresión para pedido:', pedidoId);
    
    // Abrir modal
    const modal = new bootstrap.Modal(document.getElementById('modalImpresion'));
    modal.show();
    
    // Mostrar loading
    document.getElementById('contenidoImpresion').innerHTML = `
      <div class="text-center py-5">
        <div class="spinner-border text-dark" role="status">
          <span class="visually-hidden">Cargando...</span>
        </div>
        <p class="mt-3">Cargando comprobante...</p>
      </div>
    `;
    
    try {
      // Obtener datos del pedido
      const response = await fetch(`/admin/ventas/api/pedido/${pedidoId}`);
      const data = await response.json();
      
      if (response.ok && data.success) {
        console.log('✅ Datos del pedido obtenidos:', data.pedido);
        renderizarComprobante(data.pedido);
      } else {
        throw new Error(data.mensaje || 'Error al cargar el comprobante');
      }
      
    } catch (error) {
      console.error('❌ Error:', error);
      document.getElementById('contenidoImpresion').innerHTML = `
        <div class="alert alert-danger">
          <i class="bi bi-exclamation-triangle me-2"></i>
          Error al cargar el comprobante: ${error.message}
        </div>
      `;
    }
  }

  // Renderizar comprobante en el modal
  function renderizarComprobante(pedido) {
    const fecha = new Date(pedido.fecha);
    const fechaFormateada = `${String(fecha.getDate()).padStart(2, '0')}/${String(fecha.getMonth() + 1).padStart(2, '0')}/${fecha.getFullYear()}`;
    const horaFormateada = `${String(fecha.getHours()).padStart(2, '0')}:${String(fecha.getMinutes()).padStart(2, '0')}`;
    
    let productosHTML = '';
    pedido.detalles.forEach(detalle => {
      productosHTML += `
        <tr>
          <td>${detalle.productoNombre} - Talla ${detalle.talla}</td>
          <td class="text-center">${detalle.cantidad}</td>
          <td class="text-end">S/. ${parseFloat(detalle.precioUnitario).toFixed(2)}</td>
          <td class="text-end"><strong>S/. ${parseFloat(detalle.subtotal).toFixed(2)}</strong></td>
        </tr>
      `;
    });
    
    const html = `
      <div class="comprobante-impresion">
        <!-- Header -->
        <div class="comprobante-header-print">
          <h3>TIENDA MAMBO</h3>
          <p class="mb-1">RUC: 20123456789</p>
          <p class="mb-1">Av. Principal 123 - Lima, Perú</p>
          <p class="mb-3">Tel: (01) 234-5678</p>
          <div class="numero-comp">${pedido.tipoComprobante}</div>
          <div class="numero-comp">${pedido.numeroComprobante}</div>
        </div>
        
        <!-- Datos del cliente -->
        <div class="comprobante-seccion">
          <h4>DATOS DEL CLIENTE</h4>
          <div class="comprobante-linea">
            <span>Cliente:</span>
            <strong>${pedido.cliente.nombre}</strong>
          </div>
          <div class="comprobante-linea">
            <span>${pedido.cliente.tipoDocumento}:</span>
            <strong>${pedido.cliente.numeroDocumento}</strong>
          </div>
          <div class="comprobante-linea">
            <span>Dirección:</span>
            <strong>${pedido.cliente.direccion}</strong>
          </div>
          <div class="comprobante-linea">
            <span>Teléfono:</span>
            <strong>${pedido.cliente.telefono}</strong>
          </div>
        </div>
        
        <!-- Datos de la venta -->
        <div class="comprobante-seccion">
          <h4>DATOS DE LA VENTA</h4>
          <div class="comprobante-linea">
            <span>Fecha:</span>
            <strong>${fechaFormateada}</strong>
          </div>
          <div class="comprobante-linea">
            <span>Hora:</span>
            <strong>${horaFormateada}</strong>
          </div>
          <div class="comprobante-linea">
            <span>Estado:</span>
            <strong>${pedido.estado}</strong>
          </div>
        </div>
        
        <!-- Productos -->
        <div class="comprobante-seccion">
          <h4>DETALLE DE PRODUCTOS</h4>
          <table class="comprobante-tabla">
            <thead>
              <tr>
                <th>Producto</th>
                <th class="text-center">Cant.</th>
                <th class="text-end">P. Unit.</th>
                <th class="text-end">Subtotal</th>
              </tr>
            </thead>
            <tbody>
              ${productosHTML}
            </tbody>
          </table>
        </div>
        
        <!-- Totales -->
        <div class="comprobante-total">
          <div class="linea">
            <span>Subtotal:</span>
            <strong>S/. ${parseFloat(pedido.subtotal).toFixed(2)}</strong>
          </div>
          <div class="linea">
            <span>IGV (18%):</span>
            <strong>S/. ${parseFloat(pedido.impuestos).toFixed(2)}</strong>
          </div>
          <div class="linea linea-total">
            <span>TOTAL:</span>
            <strong>S/. ${parseFloat(pedido.total).toFixed(2)}</strong>
          </div>
        </div>
        
        <!-- Footer -->
        <div class="comprobante-footer">
          <p class="mb-1">¡Gracias por su compra!</p>
          <p class="mb-0">www.tiendamambo.com | ventas@tiendamambo.com</p>
        </div>
      </div>
    `;
    
    document.getElementById('contenidoImpresion').innerHTML = html;
  }

  // Función para descargar PDF del comprobante
  async function descargarComprobantePDF() {
    const btnDescargar = document.getElementById('btnDescargarPDF');
    const textoOriginal = btnDescargar.innerHTML;
    
    try {
      // Mostrar loading
      btnDescargar.disabled = true;
      btnDescargar.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Generando PDF...';
      
      console.log('📄 Generando PDF del comprobante...');
      
      const contenido = document.getElementById('contenidoImpresion');
      
      // Capturar el contenido como imagen con mejor calidad
      const canvas = await html2canvas(contenido, {
        scale: 2, // Mayor resolución
        useCORS: true,
        logging: false,
        backgroundColor: '#ffffff'
      });
      
      const imgData = canvas.toDataURL('image/png');
      
      // Crear PDF
      const { jsPDF } = window.jspdf;
      const pdf = new jsPDF('p', 'mm', 'a4');
      
      // Calcular dimensiones para centrar
      const imgWidth = 190;
      const imgHeight = (canvas.height * imgWidth) / canvas.width;
      const pageHeight = 297; // A4 height
      
      let heightLeft = imgHeight;
      let position = 10;
      
      // Agregar imagen al PDF
      pdf.addImage(imgData, 'PNG', 10, position, imgWidth, imgHeight);
      heightLeft -= pageHeight;
      
      // Si la imagen es más grande que una página, agregar páginas adicionales
      while (heightLeft > 0) {
        position = heightLeft - imgHeight;
        pdf.addPage();
        pdf.addImage(imgData, 'PNG', 10, position, imgWidth, imgHeight);
        heightLeft -= pageHeight;
      }
      
      // Obtener número de comprobante para el nombre del archivo
      const numeroComprobante = document.querySelector('.numero-comp')?.textContent || 'comprobante';
      const nombreArchivo = `Comprobante_${numeroComprobante.replace(/\s+/g, '_')}.pdf`;
      
      // Descargar PDF
      pdf.save(nombreArchivo);
      
      console.log('✅ PDF descargado:', nombreArchivo);
      
      // Restaurar botón
      btnDescargar.disabled = false;
      btnDescargar.innerHTML = textoOriginal;
      
      // Opcional: cerrar el modal después de descargar
      setTimeout(() => {
        const modal = bootstrap.Modal.getInstance(document.getElementById('modalImpresion'));
        if (modal) {
          modal.hide();
        }
      }, 1000);
      
    } catch (error) {
      console.error('❌ Error al generar PDF:', error);
      alert('Error al generar el PDF. Por favor, intente nuevamente.');
      
      // Restaurar botón
      btnDescargar.disabled = false;
      btnDescargar.innerHTML = textoOriginal;
    }
  }
  // ==========================================
  // 🔹 EVENTO AL ABRIR MODAL DE PRODUCTOS
  // ==========================================
  
  const modalProductos = document.getElementById('modalProductos');
  if (modalProductos) {
    modalProductos.addEventListener('show.bs.modal', function() {
      console.log('🔹 Modal de productos abierto');
      cargarProductosDesdeDB();
    });
  }
  
  // ==========================================
  // 🔹 INICIALIZACIÓN
  // ==========================================
  
  agregarEventListenersClientes();
  calcularTotales();
  
  actualizarFechaHora();
  setInterval(actualizarFechaHora, 60000);
  actualizarNumeroComprobante('BOLETA');

  // Event listener para descargar PDF
  const btnDescargarPDF = document.getElementById('btnDescargarPDF');
  if (btnDescargarPDF) {
    btnDescargarPDF.addEventListener('click', descargarComprobantePDF);
  }
  
  console.log('🎯 Sistema inicializado correctamente');
});