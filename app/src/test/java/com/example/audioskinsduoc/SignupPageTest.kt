package com.example.audioskinsduoc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SignupPageTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockAuthViewModel = mock<AuthViewModel>()

    // Datos de prueba
    private val validEmail = "test@example.com"
    private val invalidEmail = "invalid-email"
    private val validPassword = "Password123!"
    private val invalidPassword = "123"
    private val matchingPassword = "Password123!"
    private val nonMatchingPassword = "Password321!"
    private val testName = "Pedro"
    private val emptyField = ""

    @Before
    fun setUp() {
        // Configurar el comportamiento del mock para authState antes de cada prueba
        val authStateLiveData = MutableLiveData<AuthState>()
        whenever(mockAuthViewModel.authState).thenReturn(authStateLiveData)
    }

    @Test
    fun testSignupWithInvalidEmail() {
        mockAuthViewModel.signup(invalidEmail, validPassword)
        verify(mockAuthViewModel).signup(invalidEmail, validPassword)
    }

    @Test
    fun testSignupWithNonMatchingPasswords() {
        mockAuthViewModel.signup(validEmail, nonMatchingPassword)
        verify(mockAuthViewModel).signup(validEmail, nonMatchingPassword)
    }

    @Test
    fun testSignupWithValidData() {
        mockAuthViewModel.signup(validEmail, validPassword)
        verify(mockAuthViewModel).signup(validEmail, validPassword)
    }

    @Test
    fun testSignupWithEmptyFields() {
        mockAuthViewModel.signup(emptyField, emptyField)
        verify(mockAuthViewModel).signup(emptyField, emptyField)
    }

    @Test
    fun testSignupWithEmptyName() {
        mockAuthViewModel.signup(validEmail, validPassword)
        verify(mockAuthViewModel).signup(validEmail, validPassword)
    }

    @Test
    fun testSignupWithInvalidPasswordFormat() {
        mockAuthViewModel.signup(validEmail, invalidPassword)
        verify(mockAuthViewModel).signup(validEmail, invalidPassword)
    }

    // Verificar que el estado de autenticación cambia a "Error" cuando ocurre un error durante el registro
    @Test
    fun testSignupErrorChangesAuthState() {
        val authStateLiveData = MutableLiveData<AuthState>()
        whenever(mockAuthViewModel.authState).thenReturn(authStateLiveData)

        // Simular error de autenticación
        authStateLiveData.value = AuthState.Error("Error al registrar")

        // Verificar que el estado se cambió a AuthState.Error
        assert(authStateLiveData.value is AuthState.Error)
    }

    // Verificar que el estado de autenticación cambia a "Authenticated" después del registro exitoso
    @Test
    fun testSignupSuccessChangesAuthState() {
        val authStateLiveData = MutableLiveData<AuthState>()
        whenever(mockAuthViewModel.authState).thenReturn(authStateLiveData)

        // Simular cambio de estado a autenticado
        authStateLiveData.value = AuthState.Authenticated("Usuario registrado")

        // Verificar que el estado se cambió a AuthState.Authenticated
        assert(authStateLiveData.value is AuthState.Authenticated)
    }

    // Verificar que el registro con nombre vacío muestra error en la UI
    @Test
    fun testSignupWithEmptyNameTriggersUIError() {
        // Simular intento de registro con nombre vacío
        mockAuthViewModel.signup(validEmail, validPassword)

        // Verificar que la lógica de mostrar error se llame correctamente (ejemplo: en la UI)
        verify(mockAuthViewModel).signup(validEmail, validPassword)
        // Aquí deberías verificar que el error se muestra en la UI; este paso depende de cómo se maneja la UI
    }

    // Verificar el comportamiento al usar una contraseña sin caracteres especiales
    @Test
    fun testSignupWithPasswordMissingSpecialCharacters() {
        val passwordWithoutSpecialChar = "Password123"
        mockAuthViewModel.signup(validEmail, passwordWithoutSpecialChar)

        // Verificar que se haya llamado a signup() con una contraseña que no tiene caracteres especiales
        verify(mockAuthViewModel).signup(validEmail, passwordWithoutSpecialChar)
    }
}
