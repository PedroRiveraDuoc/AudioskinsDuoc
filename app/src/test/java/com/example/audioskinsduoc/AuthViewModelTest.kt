package com.example.audioskinsduoc

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AuthViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var authViewModel: AuthViewModel
    private val mockFirebaseAuth: FirebaseAuth = mock()
    private val mockFirebaseDatabase: FirebaseDatabase = mock()
    private val authObserver: Observer<AuthState> = mock()

    @Before
    fun setup() {
        // Inicializa AuthViewModel con FirebaseAuth y FirebaseDatabase simulados
        authViewModel = AuthViewModel(mockFirebaseAuth, mockFirebaseDatabase)

        // Observa los cambios en AuthState
        authViewModel.authState.observeForever(authObserver)

        // Simula la respuesta de currentUser como null para simular el cierre de sesión
        whenever(mockFirebaseAuth.currentUser).thenReturn(null)
    }

    @Test
    fun testSignOut() {
        // Realizar signOut
        authViewModel.signOut()

        // Verificar que el estado cambie a Unauthenticated
        val authState = authViewModel.authState.value
        assertEquals(AuthState.Unauthenticated, authState)
    }
}
