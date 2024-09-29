package com.example.audioskinsduoc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.FirebaseDatabase.getInstance

class AuthViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val database: FirebaseDatabase = getInstance()
) : ViewModel() {
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    // LiveData para almacenar el nombre del usuario
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    init {
        checkAuthService()
    }

    fun checkAuthService() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            fetchUserName()
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Correo o Contraseña no pueden estar vacíos")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fetchUserName()
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Hubo un error")
                }
            }
    }

    private fun fetchUserName() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userEmail = currentUser.email ?: return

            // Referencia a la base de datos
            val ref: DatabaseReference = database.getReference("usuarios")

            // Buscar el usuario por correo electrónico
            ref.get().addOnSuccessListener { snapshot ->
                for (child in snapshot.children) {
                    val usuario = child.getValue(Usuarios::class.java)
                    if (usuario?.email == userEmail) {
                        val nombre = usuario.nombre ?: "Usuario"
                        _userName.value = nombre
                        _authState.value = AuthState.Authenticated(nombre)
                        return@addOnSuccessListener
                    }
                }
                // Si no encuentra el usuario
                _authState.value = AuthState.Error("Usuario no encontrado")
            }.addOnFailureListener {
                _authState.value = AuthState.Error("Error al obtener los datos del usuario")
            }
        }
    }

    fun signup(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Correo o Contraseña no pueden estar vacíos")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    fetchUserName()
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Hubo un error")
                }
            }
    }

    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
        _userName.value = "" // Limpia el nombre de usuario cuando se cierra sesión
    }
}
