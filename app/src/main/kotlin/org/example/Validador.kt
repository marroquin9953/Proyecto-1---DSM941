package org.example

object Validador {
    
    // Validar que el ID del producto existe en el inventario
    fun validarIdProducto(id: Int, inventario: List<ProductoInventario>): ProductoInventario {
        val producto = inventario.find { it.producto.id == id }
        if (producto == null) {
            Logger.error("ID de producto no encontrado: $id")
            throw ProductoNoEncontradoException("El producto con ID $id no existe en el inventario")
        }
        return producto
    }

    // Validar que la cantidad es válida y no excede el disponible
    fun validarCantidad(cantidad: Int, disponible: Int, nombreProducto: String = "producto") {
        if (cantidad <= 0) {
            Logger.error("Cantidad inválida: $cantidad para $nombreProducto")
            throw CantidadInvalidaException("La cantidad debe ser mayor a 0")
        }
        
        if (cantidad > disponible) {
            Logger.error("Cantidad excede disponible: $cantidad > $disponible para $nombreProducto")
            throw StockInsuficienteException(
                "Cantidad solicitada ($cantidad) excede el stock disponible ($disponible)"
            )
        }
    }

    // Validar formato de email
    fun validarEmail(email: String): Boolean {
        if (email.isBlank()) {
            Logger.error("Email vacío")
            throw EmailInvalidoException("El email no puede estar vacío")
        }
        
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!emailRegex.matches(email)) {
            Logger.error("Formato de email inválido: $email")
            throw EmailInvalidoException("El formato del email '$email' no es válido")
        }
        
        return true
    }

    // Validar opción de menú
    fun validarOpcionMenu(opcion: Int, opcionesValidas: List<Int>): Boolean {
        if (!opcionesValidas.contains(opcion)) {
            Logger.warning("Opción de menú inválida: $opcion")
            return false
        }
        return true
    }

    // Validar que el carrito no esté vacío
    fun validarCarritoNoVacio(carrito: Carrito) {
        if (carrito.obtenerTotal() == 0.0) {
            Logger.error("Intento de operación con carrito vacío")
            throw CarritoVacioException("El carrito está vacío. Agregue productos antes de continuar")
        }
    }

    // Leer entero de forma segura
    fun leerEntero(mensaje: String): Int? {
        print(mensaje)
        val input = readLine()
        val numero = input?.toIntOrNull()
        
        if (numero == null && !input.isNullOrBlank()) {
            Logger.warning("Entrada no numérica: $input")
        }
        
        return numero
    }

    // Leer string no vacío
    fun leerStringNoVacio(mensaje: String): String {
        print(mensaje)
        val input = readLine()?.trim()
        
        if (input.isNullOrBlank()) {
            Logger.warning("Entrada vacía")
            throw IllegalArgumentException("La entrada no puede estar vacía")
        }
        
        return input
    }
}
