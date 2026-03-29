package org.example

import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.layout.borders.SolidBorder
import java.io.File

class GeneradorPDF {
    
    fun generarFacturaPDF(factura: GeneradorFactura.Factura, cliente: Cliente): File {
        try {
            val dirFacturas = File("facturas")
            dirFacturas.mkdirs()
            
            val archivoPDF = File(dirFacturas, "${factura.numeroFactura}.pdf")
            
            val writer = PdfWriter(archivoPDF)
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc)
            
            // Encabezado
            document.add(
                Paragraph("WALMART LATAM")
                    .setFontSize(24f)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(5f)
            )
            
            document.add(
                Paragraph("FACTURA ELECTRONICA")
                    .setFontSize(16f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20f)
            )
            
            // Información de factura
            document.add(
                Paragraph("Numero de Factura: ${factura.numeroFactura}")
                    .setFontSize(10f)
            )
            document.add(
                Paragraph("Fecha: ${factura.fecha}")
                    .setFontSize(10f)
            )
            document.add(
                Paragraph("Cliente: ${cliente.nombre}")
                    .setFontSize(10f)
            )
            document.add(
                Paragraph("Correo: ${cliente.email}")
                    .setFontSize(10f)
                    .setMarginBottom(20f)
            )
            
            // Línea separadora
            document.add(
                Paragraph("_".repeat(80))
                    .setFontSize(8f)
                    .setMarginBottom(10f)
            )
            
            // Tabla de productos
            val table = Table(UnitValue.createPercentArray(floatArrayOf(40f, 15f, 15f, 15f, 15f)))
                .useAllAvailableWidth()
            
            // Encabezados de tabla
            table.addHeaderCell(
                Paragraph("Producto")
                    .setBold()
                    .setFontSize(10f)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
            )
            table.addHeaderCell(
                Paragraph("Precio Unit.")
                    .setBold()
                    .setFontSize(10f)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
            )
            table.addHeaderCell(
                Paragraph("Cantidad")
                    .setBold()
                    .setFontSize(10f)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
            )
            table.addHeaderCell(
                Paragraph("Subtotal")
                    .setBold()
                    .setFontSize(10f)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
            )
            table.addHeaderCell(
                Paragraph("Total")
                    .setBold()
                    .setFontSize(10f)
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
            )
            
            // Filas de productos
            factura.productos.forEach { producto ->
                val subtotal = producto.producto.precio * producto.cantidad
                
                table.addCell(Paragraph(producto.producto.nombre).setFontSize(9f))
                table.addCell(Paragraph("$${String.format("%.2f", producto.producto.precio)}").setFontSize(9f))
                table.addCell(Paragraph("${producto.cantidad}").setFontSize(9f))
                table.addCell(Paragraph("$${String.format("%.2f", producto.producto.precio)}").setFontSize(9f))
                table.addCell(Paragraph("$${String.format("%.2f", subtotal)}").setFontSize(9f))
            }
            
            document.add(table)
            document.add(Paragraph("").setMarginBottom(10f))
            
            // Totales
            document.add(
                Paragraph("Subtotal: $${String.format("%.2f", factura.subtotal)}")
                    .setFontSize(11f)
                    .setTextAlignment(TextAlignment.RIGHT)
            )
            document.add(
                Paragraph("IVA (13%): $${String.format("%.2f", factura.iva)}")
                    .setFontSize(11f)
                    .setTextAlignment(TextAlignment.RIGHT)
            )
            document.add(
                Paragraph("TOTAL: $${String.format("%.2f", factura.total)}")
                    .setFontSize(14f)
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setMarginTop(10f)
            )
            
            // Pie de página
            document.add(
                Paragraph("_".repeat(80))
                    .setFontSize(8f)
                    .setMarginTop(20f)
            )
            document.add(
                Paragraph("Gracias por su compra en Walmart Latam")
                    .setFontSize(10f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10f)
            )
            
            document.close()
            
            Logger.info("Factura PDF generada: ${archivoPDF.absolutePath}")
            return archivoPDF
            
        } catch (e: Exception) {
            Logger.error("Error al generar PDF", e)
            throw ErrorPersistenciaException("No se pudo generar la factura en PDF", e)
        }
    }
}
