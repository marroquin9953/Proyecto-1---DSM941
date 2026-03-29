package org.example

// Excepción cuando un producto no se encuentra en el inventario
class ProductoNoEncontradoException(mensaje: String) : Exception(mensaje)

// Excepción cuando la cantidad ingresada es inválida
class CantidadInvalidaException(mensaje: String) : Exception(mensaje)

// Excepción cuando se intenta operar con un carrito vacío
class CarritoVacioException(mensaje: String) : Exception(mensaje)

// Excepción cuando falla el envío de correo
class ErrorEnvioCorreoException(mensaje: String, causa: Throwable? = null) : Exception(mensaje, causa)

// Excepción cuando falla la persistencia de datos
class ErrorPersistenciaException(mensaje: String, causa: Throwable? = null) : Exception(mensaje, causa)

// Excepción cuando el email es inválido
class EmailInvalidoException(mensaje: String) : Exception(mensaje)

// Excepción cuando no hay stock suficiente
class StockInsuficienteException(mensaje: String) : Exception(mensaje)
