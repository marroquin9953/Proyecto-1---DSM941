package org.example

// Clase Menu mantenida por compatibilidad pero ya no se usa en el flujo principal
// El nuevo flujo es guiado y no depende de menú tradicional

class Menu {
    val menuOpt = mutableMapOf<Int, (MutableList<ProductoInventario>, Carrito) -> Unit>()
    val menuList = listOf(
        "1. Agregar producto al carrito",
        "2. Eliminar producto del carrito",
        "3. Ver carrito",
        "4. Finalizar compra y generar factura",
        "5. Salir"
    )

    init {
        // Mantenido por compatibilidad
    }
}
