package com.example.audioskinsduoc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class LoginPageTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockAuthViewModel = mock<AuthViewModel>()

    @Test
    fun testLoginWithEmptyFields() {
        // Configurar el comportamiento del mock para authState
        val authStateLiveData = MutableLiveData<AuthState>()
        whenever(mockAuthViewModel.authState).thenReturn(authStateLiveData)

        // Realizar la llamada al método login con campos vacíos
        mockAuthViewModel.login("", "")

        // Verificar que se haya llamado a login() en el ViewModel
        verify(mockAuthViewModel).login("", "")
    }

    @Test
    fun testLoginWithValidFields() {
        // Configurar el comportamiento del mock para authState
        val authStateLiveData = MutableLiveData<AuthState>()
        whenever(mockAuthViewModel.authState).thenReturn(authStateLiveData)

        // Realizar la llamada al método login con campos válidos
        val testEmail = "test@example.com"
        val testPassword = "password123"
        mockAuthViewModel.login(testEmail, testPassword)

        // Verificar que se haya llamado a login() con los campos correctos
        verify(mockAuthViewModel).login(testEmail, testPassword)
    }

    @Test
    fun testLoginWithInvalidFields() {
        // Configurar el comportamiento del mock para authState
        val authStateLiveData = MutableLiveData<AuthState>()
        whenever(mockAuthViewModel.authState).thenReturn(authStateLiveData)

        // Realizar la llamada al método login con campos inválidos
        val invalidEmail = "invalid-email"
        val invalidPassword = "123"
        mockAuthViewModel.login(invalidEmail, invalidPassword)

        // Verificar que se haya llamado a login() con los campos incorrectos
        verify(mockAuthViewModel).login(invalidEmail, invalidPassword)
    }

    @Test
    fun testAuthStateChangedToAuthenticated() {
        // Configurar el comportamiento del mock para authState
        val authStateLiveData = MutableLiveData<AuthState>()
        whenever(mockAuthViewModel.authState).thenReturn(authStateLiveData)

        // Simular el cambio de estado a autenticado con un nombre de usuario
        val testUserName = "Pedro"
        authStateLiveData.value = AuthState.Authenticated(nombre = testUserName)

        // Verificar que el estado ha cambiado a Authenticated
        assert(authStateLiveData.value is AuthState.Authenticated)
    }
}
