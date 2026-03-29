package org.example

data class Cliente(
    val nombre: String,
    val email: String
) {
    init {
        require(nombre.isNotBlank()) { "El nombre del cliente no puede estar vacío" }
        require(email.isNotBlank()) { "El email del cliente no puede estar vacío" }
    }
    
    fun getNombreFormal(): String {
        return nombre.split(" ").firstOrNull()?.replaceFirstChar { it.uppercase() } ?: nombre
    }
}
