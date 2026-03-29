package org.example

import de.vandermeer.asciitable.AsciiTable

class Carrito {
    private val productosEnCarrito = mutableListOf<ProductoCarrito>()

    fun getProductoEnCarrito(id: Int): ProductoCarrito? {
        return productosEnCarrito.find { it.producto.id == id }
    }

    fun getProductosEnCarrito(): List<ProductoCarrito> {
        return productosEnCarrito
    }

    fun agregarProducto(producto: Producto, cantidad: Int = 1) {
        try {
            if (cantidad <= 0) {
                throw CantidadInvalidaException("La cantidad debe ser mayor a 0")
            }
            
            val productoEnCarrito = productosEnCarrito.find { it.producto.id == producto.id }

            if (productoEnCarrito != null) {
                productoEnCarrito.cantidad += cantidad
                Logger.debug("Cantidad actualizada en carrito: ${producto.nombre} - Total: ${productoEnCarrito.cantidad}")
            } else {
                productosEnCarrito.add(ProductoCarrito(producto, cantidad))
                Logger.debug("Producto agregado al carrito: ${producto.nombre} x$cantidad")
            }
        } catch (e: Exception) {
            Logger.error("Error al agregar producto al carrito", e)
            throw e
        }
    }

    fun eliminarProducto(producto: Producto, cantidad: Int = 1) {
        try {
            if (cantidad <= 0) {
                throw CantidadInvalidaException("La cantidad debe ser mayor a 0")
            }
            
            val productoEnCarrito = productosEnCarrito.find { it.producto.id == producto.id }
                ?: throw ProductoNoEncontradoException("El producto ${producto.nombre} no esta en el carrito")

            if (productoEnCarrito.cantidad > cantidad) {
                productoEnCarrito.cantidad -= cantidad
                Logger.debug("Cantidad reducida en carrito: ${producto.nombre} - Restante: ${productoEnCarrito.cantidad}")
            } else {
                productosEnCarrito.remove(productoEnCarrito)
                Logger.debug("Producto eliminado completamente del carrito: ${producto.nombre}")
            }
        } catch (e: Exception) {
            Logger.error("Error al eliminar producto del carrito", e)
            throw e
        }
    }

    fun mostrarCarrito() {
        if (productosEnCarrito.isEmpty()) {
            println("El carrito esta vacio.")
        } else {
            println("Productos en el carrito:")
            println("")

            val at = AsciiTable()
            at.addRule()
            at.addRow("ID - Producto", "Precio unitario", "Cantidad", "Subtotal")
            at.addRule()

            productosEnCarrito.forEach { producto ->
                val subtotal = producto.producto.precio * producto.cantidad
                at.addRow(
                    "${producto.producto.id} - ${producto.producto.nombre}", 
                    "$${String.format("%.2f", producto.producto.precio)}", 
                    "${producto.cantidad}", 
                    "$${String.format("%.2f", subtotal)}"
                )
            }

            at.addRule()
            println(at.render())
            println("")

            val total = obtenerTotal()
            println("Total: $${String.format("%.2f", total)}")
        }
    }

    fun clear(){
        productosEnCarrito.clear()
    }

    fun obtenerTotal(): Double {
        return productosEnCarrito.sumOf { it.producto.precio * it.cantidad }
    }
}
