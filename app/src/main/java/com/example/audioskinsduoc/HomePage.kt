package com.example.audioskinsduoc

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomePage(
    modifier: Modifier = Modifier, // Parámetro para aplicar modificaciones de diseño al componente
    navController: NavController, // Controlador de navegación para navegar entre pantallas
    authViewModel: AuthViewModel // ViewModel para gestionar la autenticación del usuario
) {
    // Observa el estado de autenticación utilizando LiveData en el ViewModel
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current // Obtiene el contexto actual para usarlo dentro del componente

    // `LaunchedEffect` se ejecuta cuando cambia el valor de `authState`
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                // Si el usuario no está autenticado, navega a la pantalla de inicio de sesión ("login")
                navController.navigate(route = "login")
            }
            else -> Unit // No hace nada si el usuario está autenticado
        }
    }

    // Layout en columna para organizar los elementos verticalmente
    Column(
        modifier = modifier
            .fillMaxSize() // Ocupa todo el tamaño disponible en la pantalla
            .padding(16.dp), // Añade un padding de 16dp alrededor del contenido
        verticalArrangement = Arrangement.Center, // Centra el contenido verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido horizontalmente
    ) {
        // Muestra un texto que dice "Home Page"
        Text(text = "Home Page", fontSize = 32.sp)

        // Espacio vertical entre los elementos
        Spacer(modifier = Modifier.height(16.dp))

        // Botón para cerrar sesión
        Button(onClick = {
            authViewModel.signOut() // Llama a la función de cierre de sesión en el ViewModel
        }) {
            Text(text = "Cerrar Sesión") // Texto que se muestra en el botón
        }

        Button(onClick = {
            navController.navigate("user_management")
        }) {
            Text(text = "Administración de Usuarios")
        }

    }
}
