package org.example

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GestorHistorial {
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val archivoHistorial = File("data/historial_compras.json")
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    data class CompraHistorial(
        val numeroFactura: String,
        val fecha: String,
        val nombreCliente: String,
        val emailCliente: String,
        val productos: List<ProductoComprado>,
        val subtotal: Double,
        val iva: Double,
        val total: Double
    )

    data class ProductoComprado(
        val id: Int,
        val nombre: String,
        val precio: Double,
        val cantidad: Int,
        val subtotal: Double
    )

    init {
        // Crear directorio data si no existe
        archivoHistorial.parentFile?.mkdirs()
        
        // Crear archivo si no existe
        if (!archivoHistorial.exists()) {
            archivoHistorial.writeText("[]")
        }
    }

    fun guardarCompra(factura: GeneradorFactura.Factura): Boolean {
        try {
            // Cargar historial existente
            val historial = cargarHistorial().toMutableList()
            
            // Convertir factura a formato de historial
            val compra = CompraHistorial(
                numeroFactura = factura.numeroFactura,
                fecha = factura.fecha,
                nombreCliente = factura.nombreCliente,
                emailCliente = factura.emailCliente,
                productos = factura.productos.map { 
                    ProductoComprado(
                        id = it.producto.id,
                        nombre = it.producto.nombre,
                        precio = it.producto.precio,
                        cantidad = it.cantidad,
                        subtotal = it.producto.precio * it.cantidad
                    )
                },
                subtotal = factura.subtotal,
                iva = factura.iva,
                total = factura.total
            )
            
            // Agregar nueva compra
            historial.add(compra)
            
            // Guardar historial actualizado
            val json = gson.toJson(historial)
            archivoHistorial.writeText(json)
            
            Logger.info("Compra guardada en historial: ${factura.numeroFactura}")
            return true
            
        } catch (e: Exception) {
            Logger.error("Error al guardar compra en historial", e)
            return false
        }
    }

    fun cargarHistorial(): List<CompraHistorial> {
        try {
            if (!archivoHistorial.exists()) {
                return emptyList()
            }

            val json = archivoHistorial.readText()
            val type = object : TypeToken<List<CompraHistorial>>() {}.type
            val historial: List<CompraHistorial> = gson.fromJson(json, type)
            
            return historial
            
        } catch (e: Exception) {
            Logger.error("Error al cargar historial", e)
            return emptyList()
        }
    }

    fun mostrarHistorial() {
        try {
            val historial = cargarHistorial()
            
            if (historial.isEmpty()) {
                println("No hay compras registradas en el historial.")
                return
            }

            println("═══════════════════════════════════════════════════════")
            println("              HISTORIAL DE COMPRAS                     ")
            println("═══════════════════════════════════════════════════════")
            println("")
            println("Total de compras: ${historial.size}")
            println("")

            historial.forEach { compra ->
                println("───────────────────────────────────────────────────────")
                println("Factura: ${compra.numeroFactura}")
                println("Fecha: ${compra.fecha}")
                println("Cliente: ${compra.nombreCliente}")
                println("Correo: ${compra.emailCliente}")
                println("Total: $${String.format("%.2f", compra.total)}")
                println("Productos:")
                compra.productos.forEach { producto ->
                    println("  - ${producto.nombre} x${producto.cantidad} = $${String.format("%.2f", producto.subtotal)}")
                }
                println("")
            }
            
            println("═══════════════════════════════════════════════════════")
            
        } catch (e: Exception) {
            Logger.error("Error al mostrar historial", e)
            println("❌ Error al mostrar historial: ${e.message}")
        }
    }

    fun obtenerEstadisticas(): Map<String, Any> {
        try {
            val historial = cargarHistorial()
            
            if (historial.isEmpty()) {
                return mapOf(
                    "totalCompras" to 0,
                    "totalVentas" to 0.0,
                    "promedioCompra" to 0.0
                )
            }

            val totalCompras = historial.size
            val totalVentas = historial.sumOf { it.total }
            val promedioCompra = totalVentas / totalCompras

            return mapOf(
                "totalCompras" to totalCompras,
                "totalVentas" to totalVentas,
                "promedioCompra" to promedioCompra
            )
            
        } catch (e: Exception) {
            Logger.error("Error al obtener estadísticas", e)
            return emptyMap()
        }
    }
}
