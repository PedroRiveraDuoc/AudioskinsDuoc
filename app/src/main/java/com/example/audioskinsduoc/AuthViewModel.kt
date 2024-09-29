package com.example.audioskinsduoc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

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
            val userId = currentUser.uid
            val ref: DatabaseReference = database.getReference("usuarios").child(userId)

            ref.get().addOnSuccessListener { snapshot ->
                // Accede a la propiedad "nombre" del usuario en Firebase
                val userName = snapshot.child("nombre").getValue(String::class.java) ?: "Usuario"
                _authState.value = AuthState.Authenticated(userName)
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
    }
}
