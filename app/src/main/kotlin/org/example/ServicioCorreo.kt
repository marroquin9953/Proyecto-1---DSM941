package org.example

import java.io.File
import java.util.Properties
import javax.mail.*
import javax.mail.internet.*

class ServicioCorreo {
    
    data class ConfigCorreo(
        val host: String = "smtp.gmail.com",
        val puerto: String = "587",
        val usuario: String = "",
        val password: String = "",
        val remitente: String = ""
    )

    private fun cargarConfiguracion(): ConfigCorreo {
        val configFile = File("config/mail.properties")
        
        if (!configFile.exists()) {
            Logger.warning("Archivo de configuración de correo no encontrado. Usando configuración por defecto.")
            return ConfigCorreo()
        }

        try {
            val props = Properties()
            configFile.inputStream().use { props.load(it) }
            
            return ConfigCorreo(
                host = props.getProperty("mail.smtp.host", "smtp.gmail.com"),
                puerto = props.getProperty("mail.smtp.port", "587"),
                usuario = props.getProperty("mail.usuario", ""),
                password = props.getProperty("mail.password", ""),
                remitente = props.getProperty("mail.remitente", "")
            )
        } catch (e: Exception) {
            Logger.error("Error al cargar configuración de correo", e)
            return ConfigCorreo()
        }
    }

    fun enviarFactura(emailDestino: String, factura: GeneradorFactura.Factura, archivoFactura: File): Boolean {
        val config = cargarConfiguracion()
        
        // Validar configuración
        if (config.usuario.isBlank() || config.password.isBlank()) {
            Logger.warning("Configuración de correo incompleta. Simulando envío...")
            println("")
            println("⚠️  Configuración de correo no disponible")
            println("📧 Factura guardada localmente en: ${archivoFactura.absolutePath}")
            println("   Para habilitar envío de correo, configure: config/mail.properties")
            return true // Simular éxito para no bloquear el flujo
        }

        try {
            // Configurar propiedades
            val props = Properties().apply {
                put("mail.smtp.auth", "true")
                put("mail.smtp.starttls.enable", "true")
                put("mail.smtp.host", config.host)
                put("mail.smtp.port", config.puerto)
                put("mail.smtp.ssl.trust", config.host)
            }

            // Crear sesión
            val session = Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(config.usuario, config.password)
                }
            })

            // Crear mensaje
            val mensaje = MimeMessage(session).apply {
                setFrom(InternetAddress(config.remitente))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestino))
                subject = "Factura Electrónica - ${factura.numeroFactura}"
                
                // Crear cuerpo del mensaje
                val multipart = MimeMultipart()
                
                // Parte de texto
                val textoParte = MimeBodyPart().apply {
                    setText("""
                        Estimado cliente,
                        
                        Adjuntamos su factura electrónica correspondiente a su compra.
                        
                        Número de Factura: ${factura.numeroFactura}
                        Fecha: ${factura.fecha}
                        Total: $${String.format("%.2f", factura.total)}
                        
                        Gracias por su compra!
                        
                        ---
                        Este es un correo automático, por favor no responder.
                    """.trimIndent())
                }
                multipart.addBodyPart(textoParte)
                
                // Adjuntar archivo
                val adjuntoParte = MimeBodyPart().apply {
                    attachFile(archivoFactura)
                }
                multipart.addBodyPart(adjuntoParte)
                
                setContent(multipart)
            }

            // Enviar
            Transport.send(mensaje)
            
            Logger.info("Factura enviada por correo a: $emailDestino")
            println("✓ Factura enviada exitosamente a: $emailDestino")
            
            return true
            
        } catch (e: MessagingException) {
            Logger.error("Error al enviar correo", e)
            println("❌ Error al enviar correo: ${e.message}")
            println("📧 Factura guardada localmente en: ${archivoFactura.absolutePath}")
            return false
        } catch (e: Exception) {
            Logger.error("Error inesperado al enviar correo", e)
            println("❌ Error inesperado: ${e.message}")
            println("📧 Factura guardada localmente en: ${archivoFactura.absolutePath}")
            return false
        }
    }

    fun crearArchivoConfiguracionEjemplo() {
        val configDir = File("config")
        configDir.mkdirs()
        
        val configFile = File(configDir, "mail.properties")
        if (!configFile.exists()) {
            configFile.writeText("""
                # Configuración de correo SMTP
                # Para Gmail, necesitas crear una "Contraseña de aplicación"
                # https://myaccount.google.com/apppasswords
                
                mail.smtp.host=smtp.gmail.com
                mail.smtp.port=587
                mail.usuario=tu_email@gmail.com
                mail.password=tu_contraseña_de_aplicacion
                mail.remitente=tu_email@gmail.com
            """.trimIndent())
            
            Logger.info("Archivo de configuración de correo creado: config/mail.properties")
        }
    }
}
