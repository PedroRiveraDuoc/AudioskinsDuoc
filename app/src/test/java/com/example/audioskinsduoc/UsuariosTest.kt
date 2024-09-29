package com.example.audioskinsduoc

import org.junit.Assert.assertEquals
import org.junit.Test

class UsuariosTest {

    @Test
    fun testUsuarioInitialization() {
        // Crear una instancia de Usuarios con valores iniciales
        val usuario = Usuarios(
            id = "12345",
            email = "test@example.com",
            nombre = "Juan",
            apellidoPaterno = "Pérez",
            apellidoMaterno = "Gómez",
            telefono = "123456789",
            direccion = "Calle Falsa 123"
        )

        // Verificar que los valores se inicializan correctamente
        assertEquals("12345", usuario.id)
        assertEquals("test@example.com", usuario.email)
        assertEquals("Juan", usuario.nombre)
        assertEquals("Pérez", usuario.apellidoPaterno)
        assertEquals("Gómez", usuario.apellidoMaterno)
        assertEquals("123456789", usuario.telefono)
        assertEquals("Calle Falsa 123", usuario.direccion)
    }

    @Test
    fun testUsuarioDefaultValues() {
        // Crear una instancia de Usuarios con valores predeterminados
        val usuario = Usuarios()

        // Verificar que los valores predeterminados estén establecidos correctamente
        assertEquals("", usuario.id)
        assertEquals("", usuario.email)
        assertEquals("", usuario.nombre)
        assertEquals("", usuario.apellidoPaterno)
        assertEquals("", usuario.apellidoMaterno)
        assertEquals("", usuario.telefono)
        assertEquals("", usuario.direccion)
    }
}
