package org.example

import de.vandermeer.asciitable.AsciiTable

fun imprimirBienvenida() {
    println("═══════════════════════════════════════════════════════")
    println("                                                       ")
    println("              BIENVENIDO A WALMART LATAM               ")
    println("                                                       ")
    println("        Sistema de Gestion de Compras en Linea        ")
    println("                                                       ")
    println("═══════════════════════════════════════════════════════")
    println()
}

fun capturarDatosCliente(): Cliente? {
    try {
        println("Para continuar, necesitamos algunos datos:")
        println()
        
        val nombre = Validador.leerStringNoVacio("Ingrese su nombre completo: ")
        
        var emailValido = false
        var email = ""
        
        while (!emailValido) {
            try {
                email = Validador.leerStringNoVacio("Ingrese su correo electronico: ")
                Validador.validarEmail(email)
                emailValido = true
            } catch (e: EmailInvalidoException) {
                println("Formato de correo invalido. Intente nuevamente.")
            }
        }
        
        val cliente = Cliente(nombre, email)
        Logger.info("Cliente registrado: ${cliente.nombre} - ${cliente.email}")
        
        println()
        println("Bienvenido, ${cliente.getNombreFormal()}.")
        println()
        
        return cliente
        
    } catch (e: Exception) {
        Logger.error("Error al capturar datos del cliente", e)
        println("Error al registrar datos. Intente nuevamente.")
        return null
    }
}

fun calcularStockReal(producto: ProductoInventario, carrito: Carrito): Int {
    val cantidadEnCarrito = carrito.getProductoEnCarrito(producto.producto.id)?.cantidad ?: 0
    return producto.cantidadDisponible - cantidadEnCarrito
}

fun imprimirCatalogo(inventario: List<ProductoInventario>, carrito: Carrito) {
    println("═══════════════════════════════════════════════════════")
    println("              CATALOGO DE PRODUCTOS                    ")
    println("═══════════════════════════════════════════════════════")
    println()

    val at = AsciiTable()
    
    at.addRule()
    at.addRow("ID", "Producto", "Precio", "Stock")
    at.addRule()

    inventario.forEachIndexed { index, producto ->
        val stockReal = calcularStockReal(producto, carrito)
        val stockTexto = if (stockReal > 0) {
            "$stockReal"
        } else {
            "AGOTADO"
        }
        
        at.addRow(
            "${producto.producto.id}",
            producto.producto.nombre,
            "$${String.format("%.2f", producto.producto.precio)}",
            stockTexto
        )
        
        // Agregar línea separadora después de cada producto
        at.addRule()
    }

    println(at.render())
}

fun editarCarrito(carrito: Carrito): Boolean {
    try {
        print("\u001b[H\u001b[2J")
        println("═══════════════════════════════════════════════════════")
        println("              EDITAR CARRITO                           ")
        println("═══════════════════════════════════════════════════════")
        println()
        
        if (carrito.getProductosEnCarrito().isEmpty()) {
            println("El carrito esta vacio.")
            println()
            println("Presione Enter para continuar...")
            readlnOrNull()
            return false
        }
        
        carrito.mostrarCarrito()
        
        println()
        println("───────────────────────────────────────────────────────")
        print("Ingrese el ID del producto a eliminar (0 para cancelar): ")
        
        val idProducto = readLine()?.toIntOrNull()
        
        if (idProducto == null || idProducto == 0) {
            return true
        }
        
        val productoEnCarrito = carrito.getProductoEnCarrito(idProducto)
        
        if (productoEnCarrito == null) {
            println()
            println("Producto no encontrado en el carrito.")
            println("Presione Enter para continuar...")
            readlnOrNull()
            return true
        }
        
        println()
        println("Producto: ${productoEnCarrito.producto.nombre}")
        println("Cantidad en carrito: ${productoEnCarrito.cantidad}")
        println()
        
        print("Cantidad a eliminar (max ${productoEnCarrito.cantidad}): ")
        val cantidad = readLine()?.toIntOrNull()
        
        if (cantidad == null || cantidad <= 0) {
            println()
            println("Cantidad invalida.")
            println("Presione Enter para continuar...")
            readlnOrNull()
            return true
        }
        
        if (cantidad > productoEnCarrito.cantidad) {
            println()
            println("Cantidad excede la cantidad en carrito.")
            println("Presione Enter para continuar...")
            readlnOrNull()
            return true
        }
        
        carrito.eliminarProducto(productoEnCarrito.producto, cantidad)
        Logger.info("Producto eliminado del carrito: ${productoEnCarrito.producto.nombre} x$cantidad")
        
        println()
        println("Producto eliminado exitosamente.")
        println()
        
        if (carrito.getProductosEnCarrito().isEmpty()) {
            println("El carrito esta vacio.")
            println("Presione Enter para volver al catalogo...")
            readlnOrNull()
            return false
        } else {
            carrito.mostrarCarrito()
            println()
            println("Presione Enter para continuar...")
            readlnOrNull()
            return true
        }
        
    } catch (e: Exception) {
        Logger.error("Error al editar carrito", e)
        println()
        println("Error al editar carrito.")
        println("Presione Enter para continuar...")
        readlnOrNull()
        return true
    }
}

fun agregarProductosAlCarrito(inventario: List<ProductoInventario>, carrito: Carrito): Boolean {
    while (true) {
        try {
            println()
            println("───────────────────────────────────────────────────────")
            print("Ingrese el ID del producto (0 para finalizar e ir al resumen): ")
            
            val idProducto = readLine()?.toIntOrNull()
            
            if (idProducto == null) {
                println("Entrada invalida. Intente nuevamente.")
                continue
            }
            
            if (idProducto == 0) {
                return carrito.getProductosEnCarrito().isNotEmpty()
            }

            val productoSeleccionado = inventario.find { it.producto.id == idProducto }
            
            if (productoSeleccionado == null) {
                println("Producto no encontrado. Intente nuevamente.")
                continue
            }

            // Calcular stock real disponible
            val stockReal = calcularStockReal(productoSeleccionado, carrito)

            if (stockReal <= 0) {
                println("Producto sin stock disponible. Seleccione otro.")
                continue
            }

            println()
            println("Producto: ${productoSeleccionado.producto.nombre}")
            println("Precio: $${String.format("%.2f", productoSeleccionado.producto.precio)}")
            println("Stock disponible: $stockReal")
            println()

            print("Cantidad (max $stockReal): ")
            val cantidad = readLine()?.toIntOrNull()

            if (cantidad == null || cantidad <= 0) {
                println("Cantidad invalida.")
                continue
            }

            // Validar contra stock real
            if (cantidad > stockReal) {
                println("Cantidad excede el stock disponible ($stockReal unidades).")
                continue
            }

            carrito.agregarProducto(productoSeleccionado.producto, cantidad)
            Logger.info("Producto agregado: ${productoSeleccionado.producto.nombre} x$cantidad")

            println()
            println("Producto agregado exitosamente.")
            println()
            
            // Mostrar carrito actualizado
            carrito.mostrarCarrito()
            
            println()
            println("═══════════════════════════════════════════════════════")
            println("Que desea realizar a continuacion?")
            println("═══════════════════════════════════════════════════════")
            println("1. Continuar comprando")
            println("2. Editar carrito (eliminar productos)")
            println("3. Finalizar compra")
            println()
            
            print("Seleccione una opcion: ")
            val opcion = readLine()?.toIntOrNull()
            
            when (opcion) {
                1 -> {
                    print("\u001b[H\u001b[2J")
                    imprimirCatalogo(inventario, carrito)
                    continue
                }
                2 -> {
                    // Editar carrito
                    if (editarCarrito(carrito)) {
                        print("\u001b[H\u001b[2J")
                        imprimirCatalogo(inventario, carrito)
                        continue
                    } else {
                        // Si el carrito quedó vacío, volver al catálogo
                        print("\u001b[H\u001b[2J")
                        imprimirCatalogo(inventario, carrito)
                        continue
                    }
                }
                3 -> {
                    return true
                }
                else -> {
                    println("Opcion invalida. Continuando compra...")
                    print("\u001b[H\u001b[2J")
                    imprimirCatalogo(inventario, carrito)
                    continue
                }
            }
            
        } catch (e: Exception) {
            Logger.error("Error al agregar producto", e)
            println("Error al procesar producto. Intente nuevamente.")
        }
    }
}

fun mostrarCheckout(carrito: Carrito, cliente: Cliente): Boolean {
    try {
        print("\u001b[H\u001b[2J")
        println("═══════════════════════════════════════════════════════")
        println("              RESUMEN DE COMPRA                        ")
        println("═══════════════════════════════════════════════════════")
        println()
        
        // Mostrar información del cliente
        println("Cliente: ${cliente.nombre}")
        println("Correo: ${cliente.email}")
        println()
        println("───────────────────────────────────────────────────────")
        println()
        
        // Mostrar carrito
        carrito.mostrarCarrito()
        
        println()
        println("═══════════════════════════════════════════════════════")
        println("Desea confirmar la compra?")
        println("═══════════════════════════════════════════════════════")
        println("1. Confirmar compra")
        println("2. Cancelar y volver al catalogo")
        println()
        
        print("Seleccione una opcion: ")
        val opcion = readLine()?.toIntOrNull()
        
        return when (opcion) {
            1 -> {
                Logger.info("Compra confirmada por el usuario")
                true
            }
            2 -> {
                Logger.info("Compra cancelada por el usuario")
                println()
                println("Compra cancelada.")
                false
            }
            else -> {
                println("Opcion invalida. Cancelando compra...")
                false
            }
        }
        
    } catch (e: Exception) {
        Logger.error("Error en checkout", e)
        println("Error en el proceso de checkout.")
        return false
    }
}

fun procesarCompra(inventario: MutableList<ProductoInventario>, carrito: Carrito, cliente: Cliente, gestorInventario: GestorInventario): Boolean {
    try {
        println()
        println("Procesando compra...")
        println()

        // Actualizar inventario
        carrito.getProductosEnCarrito().forEach { productoCarrito ->
            val pos = inventario.indexOfFirst { it.producto.id == productoCarrito.producto.id }
            if (pos != -1) {
                val productoInventario = inventario[pos]
                val nuevaCantidad = productoInventario.cantidadDisponible - productoCarrito.cantidad
                
                inventario[pos] = ProductoInventario(productoCarrito.producto, nuevaCantidad)
                Logger.info("Inventario actualizado: ${productoCarrito.producto.nombre} - Nuevo stock: $nuevaCantidad")
            }
        }

        // Generar factura
        val generador = GeneradorFactura()
        val factura = generador.crearFactura(carrito, cliente)
        val facturaTexto = generador.generarFacturaTexto(factura)
        
        // Mostrar factura en pantalla
        print("\u001b[H\u001b[2J")
        println(facturaTexto)

        // Guardar factura en archivos
        val archivoFactura = generador.guardarFactura(factura)
        
        // Generar PDF
        try {
            val generadorPDF = GeneradorPDF()
            val archivoPDF = generadorPDF.generarFacturaPDF(factura, cliente)
            println("Factura PDF generada: ${archivoPDF.name}")
            println("Ubicacion: ${archivoPDF.absolutePath}")
        } catch (e: Exception) {
            Logger.error("Error al generar PDF", e)
            println("Advertencia: No se pudo generar el PDF de la factura.")
        }

        // Intentar enviar por correo
        try {
            val servicioCorreo = ServicioCorreo()
            val enviado = servicioCorreo.enviarFactura(cliente.email, factura, archivoFactura)
            
            if (!enviado) {
                println()
                println("Nota: El servicio de correo no esta configurado.")
                println("La factura ha sido guardada localmente en la carpeta 'facturas'.")
            }
        } catch (e: Exception) {
            Logger.error("Error al enviar correo", e)
            println()
            println("Nota: No se pudo enviar la factura por correo.")
            println("La factura ha sido guardada localmente en la carpeta 'facturas'.")
        }

        // Guardar inventario
        gestorInventario.guardarInventario(inventario)

        // Limpiar carrito DESPUÉS de completar la compra
        carrito.clear()
        Logger.info("Compra completada - Factura: ${factura.numeroFactura} - Carrito limpiado")

        println()
        println("═══════════════════════════════════════════════════════")
        println("           COMPRA REALIZADA EXITOSAMENTE               ")
        println("═══════════════════════════════════════════════════════")
        
        return true
        
    } catch (e: Exception) {
        Logger.error("Error al procesar compra", e)
        println()
        println("Error al procesar la compra.")
        return false
    }
}

fun main() {
    try {
        Logger.info("=== Iniciando Walmart Latam - Sistema de Compras ===")

        // Crear configuración de correo
        val servicioCorreo = ServicioCorreo()
        servicioCorreo.crearArchivoConfiguracionEjemplo()

        // Gestor de inventario
        val gestorInventario = GestorInventario()
        
        // Cargar o crear inventario
        val inventario = if (gestorInventario.existeInventarioGuardado()) {
            gestorInventario.cargarInventario() ?: gestorInventario.crearInventarioInicial()
        } else {
            gestorInventario.crearInventarioInicial()
        }

        // Pantalla de bienvenida
        print("\u001b[H\u001b[2J")
        imprimirBienvenida()
        
        // Capturar datos del cliente
        val cliente = capturarDatosCliente()
        if (cliente == null) {
            println("No se pudo completar el registro. Finalizando sistema.")
            return
        }
        
        println("Presione Enter para ver el catalogo...")
        readlnOrNull()

        var continuarComprando = true

        while (continuarComprando) {
            try {
                // Crear nuevo carrito para cada compra (asegura limpieza)
                val carrito = Carrito()
                
                // Mostrar catálogo
                print("\u001b[H\u001b[2J")
                println("═══════════════════════════════════════════════════════")
                println("  WALMART LATAM - Cliente: ${cliente.getNombreFormal()}")
                println("═══════════════════════════════════════════════════════")
                println()
                imprimirCatalogo(inventario, carrito)

                // Agregar productos al carrito
                val hayProductos = agregarProductosAlCarrito(inventario, carrito)
                
                if (!hayProductos) {
                    println()
                    println("No se agregaron productos al carrito.")
                    println()
                    print("Desea realizar otra compra? (S/N): ")
                    val respuesta = readLine()?.trim()?.uppercase()
                    continuarComprando = respuesta == "S"
                    continue
                }

                // Mostrar checkout
                val confirmarCompra = mostrarCheckout(carrito, cliente)
                
                if (!confirmarCompra) {
                    println()
                    print("Desea volver al catalogo? (S/N): ")
                    val respuesta = readLine()?.trim()?.uppercase()
                    if (respuesta == "S") {
                        continue
                    } else {
                        continuarComprando = false
                        continue
                    }
                }

                // Procesar compra
                val compraExitosa = procesarCompra(inventario, carrito, cliente, gestorInventario)
                
                if (compraExitosa) {
                    println()
                    print("Desea realizar otra compra? (S/N): ")
                    val respuesta = readLine()?.trim()?.uppercase()
                    continuarComprando = respuesta == "S"
                } else {
                    println()
                    print("Desea intentar nuevamente? (S/N): ")
                    val respuesta = readLine()?.trim()?.uppercase()
                    continuarComprando = respuesta == "S"
                }
                
            } catch (e: Exception) {
                Logger.error("Error en el flujo de compra", e)
                println()
                println("Error inesperado. Intente nuevamente.")
                println()
                print("Desea continuar? (S/N): ")
                val respuesta = readLine()?.trim()?.uppercase()
                continuarComprando = respuesta == "S"
            }
        }

        // Guardar inventario final
        gestorInventario.guardarInventario(inventario)
        
        println()
        println("═══════════════════════════════════════════════════════")
        println("  Gracias por su visita, ${cliente.getNombreFormal()}")
        println("           Walmart Latam - Siempre contigo            ")
        println("═══════════════════════════════════════════════════════")
        Logger.info("=== Sistema finalizado ===")

    } catch (e: Exception) {
        Logger.error("Error critico en la aplicacion", e)
        println("Error critico. Revise el archivo de log en: logs/app.log")
    }
}
