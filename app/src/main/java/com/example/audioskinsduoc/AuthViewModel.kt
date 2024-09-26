package com.example.audioskinsduoc

import androidx.core.app.NotificationCompat.MessagingStyle.Message
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow

class AuthViewModel : ViewModel(){
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState

    init {
        checkAuthService()
    }

    fun checkAuthService(){
        if(auth.currentUser==null){
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email : String, password : String){

        if (email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Correo o Contraseña no pueden estar vacios")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Hubo un error")

                }
            }
    }

    fun signup(email : String, password : String){

        if (email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Correo o Contraseña no pueden estar vacios")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Hubo un error")

                }
            }
    }

    fun signOut() {
        // Lógica de cierre de sesión (por ejemplo, Firebase signOut)
        _authState.value = AuthState.Unauthenticated
    }

}



sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}