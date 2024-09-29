package com.example.audioskinsduoc

sealed class AuthState {
    data class Authenticated(val nombre: String) : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}