package org.example

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Logger {
    private val logFile = File("logs/app.log")
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    init {
        // Crear directorio logs si no existe
        logFile.parentFile?.mkdirs()
        if (!logFile.exists()) {
            logFile.createNewFile()
        }
    }

    private fun escribirLog(nivel: String, mensaje: String) {
        try {
            val timestamp = LocalDateTime.now().format(dateFormatter)
            val logEntry = "[$timestamp] [$nivel] - $mensaje\n"
            logFile.appendText(logEntry)
        } catch (e: Exception) {
            System.err.println("Error al escribir en log: ${e.message}")
        }
    }

    fun info(mensaje: String) {
        escribirLog("INFO", mensaje)
        println("[INFO] $mensaje")
    }

    fun warning(mensaje: String) {
        escribirLog("WARNING", mensaje)
        println("[ADVERTENCIA] $mensaje")
    }

    fun error(mensaje: String, excepcion: Exception? = null) {
        val mensajeCompleto = if (excepcion != null) {
            "$mensaje - ${excepcion.javaClass.simpleName}: ${excepcion.message}\n${excepcion.stackTraceToString()}"
        } else {
            mensaje
        }
        escribirLog("ERROR", mensajeCompleto)
        println("[ERROR] $mensaje")
    }

    fun debug(mensaje: String) {
        escribirLog("DEBUG", mensaje)
    }
}
