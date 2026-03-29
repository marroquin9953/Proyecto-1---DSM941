package org.example

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File

class GestorInventario {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val archivoInventario = File("data/inventario.json")

    data class ProductoInventarioData(
        val id: Int,
        val nombre: String,
        val precio: Double,
        val cantidadDisponible: Int
    )

    init {
        // Crear directorio data si no existe
        archivoInventario.parentFile?.mkdirs()
    }

    fun guardarInventario(inventario: List<ProductoInventario>): Boolean {
        try {
            val datos = inventario.map { 
                ProductoInventarioData(
                    id = it.producto.id,
                    nombre = it.producto.nombre,
                    precio = it.producto.precio,
                    cantidadDisponible = it.cantidadDisponible
                )
            }
            
            val json = gson.toJson(datos)
            archivoInventario.writeText(json)
            
            Logger.info("Inventario guardado exitosamente: ${inventario.size} productos")
            return true
            
        } catch (e: Exception) {
            Logger.error("Error al guardar inventario", e)
            throw ErrorPersistenciaException("No se pudo guardar el inventario", e)
        }
    }

    fun cargarInventario(): MutableList<ProductoInventario>? {
        try {
            if (!archivoInventario.exists()) {
                Logger.info("Archivo de inventario no existe. Se creará uno nuevo.")
                return null
            }

            val json = archivoInventario.readText()
            val type = object : TypeToken<List<ProductoInventarioData>>() {}.type
            val datos: List<ProductoInventarioData> = gson.fromJson(json, type)
            
            val inventario = datos.map {
                ProductoInventario(
                    producto = Producto(it.id, it.nombre, it.precio),
                    cantidadDisponible = it.cantidadDisponible
                )
            }.toMutableList()
            
            Logger.info("Inventario cargado exitosamente: ${inventario.size} productos")
            return inventario
            
        } catch (e: Exception) {
            Logger.error("Error al cargar inventario", e)
            println("⚠️  No se pudo cargar el inventario guardado. Se creará uno nuevo.")
            return null
        }
    }

    fun existeInventarioGuardado(): Boolean {
        return archivoInventario.exists()
    }

    fun actualizarStock(inventario: MutableList<ProductoInventario>, idProducto: Int, nuevaCantidad: Int): Boolean {
        try {
            val pos = inventario.indexOfFirst { it.producto.id == idProducto }
            if (pos == -1) {
                Logger.error("Producto no encontrado para actualizar stock: ID $idProducto")
                return false
            }

            val producto = inventario[pos].producto
            inventario[pos] = ProductoInventario(producto, nuevaCantidad)
            
            guardarInventario(inventario)
            Logger.info("Stock actualizado: ${producto.nombre} - Nueva cantidad: $nuevaCantidad")
            
            return true
            
        } catch (e: Exception) {
            Logger.error("Error al actualizar stock", e)
            return false
        }
    }

    fun crearInventarioInicial(): MutableList<ProductoInventario> {
        val inventario = crearInventario()
        guardarInventario(inventario)
        Logger.info("Inventario inicial creado y guardado")
        return inventario
    }
}
