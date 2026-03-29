package org.example

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GeneradorFactura {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val fileFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")

    data class Factura(
        val numeroFactura: String,
        val fecha: String,
        val emailCliente: String,
        val nombreCliente: String,
        val productos: List<ProductoCarrito>,
        val subtotal: Double,
        val iva: Double,
        val total: Double
    )

    fun generarNumeroFactura(): String {
        val timestamp = LocalDateTime.now().format(fileFormatter)
        return "FAC-$timestamp"
    }

    fun crearFactura(carrito: Carrito, cliente: Cliente): Factura {
        val fecha = LocalDateTime.now().format(dateFormatter)
        val numeroFactura = generarNumeroFactura()
        val subtotal = carrito.obtenerTotal()
        val iva = subtotal * 0.13
        val total = subtotal + iva

        return Factura(
            numeroFactura = numeroFactura,
            fecha = fecha,
            emailCliente = cliente.email,
            nombreCliente = cliente.nombre,
            productos = carrito.getProductosEnCarrito().toList(),
            subtotal = subtotal,
            iva = iva,
            total = total
        )
    }

    fun generarFacturaTexto(factura: Factura): String {
        val sb = StringBuilder()
        
        sb.appendLine("═══════════════════════════════════════════════════════")
        sb.appendLine("                   WALMART LATAM                       ")
        sb.appendLine("                 FACTURA ELECTRONICA                   ")
        sb.appendLine("═══════════════════════════════════════════════════════")
        sb.appendLine()
        sb.appendLine("Numero de Factura: ${factura.numeroFactura}")
        sb.appendLine("Fecha: ${factura.fecha}")
        sb.appendLine("Cliente: ${factura.nombreCliente}")
        sb.appendLine("Correo: ${factura.emailCliente}")
        sb.appendLine()
        sb.appendLine("───────────────────────────────────────────────────────")
        sb.appendLine("DETALLE DE PRODUCTOS")
        sb.appendLine("───────────────────────────────────────────────────────")
        sb.appendLine()
        
        factura.productos.forEach { producto ->
            val subtotalProducto = producto.producto.precio * producto.cantidad
            sb.appendLine("${producto.producto.nombre}")
            sb.appendLine("  Cantidad: ${producto.cantidad} x $${String.format("%.2f", producto.producto.precio)}")
            sb.appendLine("  Subtotal: $${String.format("%.2f", subtotalProducto)}")
            sb.appendLine()
        }
        
        sb.appendLine("───────────────────────────────────────────────────────")
        sb.appendLine("Subtotal:        $${String.format("%.2f", factura.subtotal)}")
        sb.appendLine("IVA (13%):       $${String.format("%.2f", factura.iva)}")
        sb.appendLine("═══════════════════════════════════════════════════════")
        sb.appendLine("TOTAL:           $${String.format("%.2f", factura.total)}")
        sb.appendLine("═══════════════════════════════════════════════════════")
        sb.appendLine()
        sb.appendLine("Gracias por su compra en Walmart Latam")
        sb.appendLine()
        
        return sb.toString()
    }

    fun generarFacturaHTML(factura: Factura): String {
        val sb = StringBuilder()
        
        sb.appendLine("<!DOCTYPE html>")
        sb.appendLine("<html>")
        sb.appendLine("<head>")
        sb.appendLine("    <meta charset='UTF-8'>")
        sb.appendLine("    <title>Factura ${factura.numeroFactura}</title>")
        sb.appendLine("    <style>")
        sb.appendLine("        body { font-family: Arial, sans-serif; margin: 20px; }")
        sb.appendLine("        .header { text-align: center; background-color: #0071CE; color: white; padding: 20px; }")
        sb.appendLine("        .info { margin: 20px 0; }")
        sb.appendLine("        table { width: 100%; border-collapse: collapse; margin: 20px 0; }")
        sb.appendLine("        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }")
        sb.appendLine("        th { background-color: #0071CE; color: white; }")
        sb.appendLine("        .total { text-align: right; font-size: 18px; font-weight: bold; }")
        sb.appendLine("        .footer { text-align: center; margin-top: 30px; color: #666; }")
        sb.appendLine("    </style>")
        sb.appendLine("</head>")
        sb.appendLine("<body>")
        sb.appendLine("    <div class='header'>")
        sb.appendLine("        <h1>WALMART LATAM</h1>")
        sb.appendLine("        <h2>FACTURA ELECTRONICA</h2>")
        sb.appendLine("    </div>")
        sb.appendLine("    <div class='info'>")
        sb.appendLine("        <p><strong>Numero de Factura:</strong> ${factura.numeroFactura}</p>")
        sb.appendLine("        <p><strong>Fecha:</strong> ${factura.fecha}</p>")
        sb.appendLine("        <p><strong>Cliente:</strong> ${factura.nombreCliente}</p>")
        sb.appendLine("        <p><strong>Correo:</strong> ${factura.emailCliente}</p>")
        sb.appendLine("    </div>")
        sb.appendLine("    <table>")
        sb.appendLine("        <tr>")
        sb.appendLine("            <th>Producto</th>")
        sb.appendLine("            <th>Precio Unitario</th>")
        sb.appendLine("            <th>Cantidad</th>")
        sb.appendLine("            <th>Subtotal</th>")
        sb.appendLine("        </tr>")
        
        factura.productos.forEach { producto ->
            val subtotalProducto = producto.producto.precio * producto.cantidad
            sb.appendLine("        <tr>")
            sb.appendLine("            <td>${producto.producto.nombre}</td>")
            sb.appendLine("            <td>$${String.format("%.2f", producto.producto.precio)}</td>")
            sb.appendLine("            <td>${producto.cantidad}</td>")
            sb.appendLine("            <td>$${String.format("%.2f", subtotalProducto)}</td>")
            sb.appendLine("        </tr>")
        }
        
        sb.appendLine("    </table>")
        sb.appendLine("    <div class='total'>")
        sb.appendLine("        <p>Subtotal: $${String.format("%.2f", factura.subtotal)}</p>")
        sb.appendLine("        <p>IVA (13%): $${String.format("%.2f", factura.iva)}</p>")
        sb.appendLine("        <p style='font-size: 24px; color: #0071CE;'>TOTAL: $${String.format("%.2f", factura.total)}</p>")
        sb.appendLine("    </div>")
        sb.appendLine("    <div class='footer'>")
        sb.appendLine("        <p>Gracias por su compra en Walmart Latam</p>")
        sb.appendLine("    </div>")
        sb.appendLine("</body>")
        sb.appendLine("</html>")
        
        return sb.toString()
    }

    fun guardarFactura(factura: Factura): File {
        try {
            val dirFacturas = File("facturas")
            dirFacturas.mkdirs()
            
            val archivoTexto = File(dirFacturas, "${factura.numeroFactura}.txt")
            archivoTexto.writeText(generarFacturaTexto(factura))
            
            val archivoHTML = File(dirFacturas, "${factura.numeroFactura}.html")
            archivoHTML.writeText(generarFacturaHTML(factura))
            
            Logger.info("Factura guardada: ${factura.numeroFactura}")
            
            // Guardar en historial
            val gestorHistorial = GestorHistorial()
            gestorHistorial.guardarCompra(factura)
            
            return archivoTexto
        } catch (e: Exception) {
            Logger.error("Error al guardar factura", e)
            throw ErrorPersistenciaException("No se pudo guardar la factura", e)
        }
    }
}
