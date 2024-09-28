package com.example.audioskinsduoc

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.PatternsCompat
import androidx.navigation.NavHostController

@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onLoginSuccess: () -> Unit,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val authState by authViewModel.authState.observeAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Estados de error
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Observando el estado de autenticación
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                isLoading = false
                onLoginSuccess()  // Llamar al callback onLoginSuccess
                Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_LONG).show()
            }
            is AuthState.Error -> {
                isLoading = false
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "Iniciar Sesión",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Campo de correo
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
                    },
                    label = { Text(text = "Correo Electrónico") },
                    isError = emailError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                if (emailError) {
                    Text(
                        text = "Por favor, ingrese un correo electrónico válido",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de contraseña
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(text = "Contraseña") },
                    isError = passwordError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
                if (passwordError) {
                    Text(
                        text = "La contraseña no puede estar vacía",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de inicio de sesión
                Button(
                    onClick = {
                        if (!emailError && password.isNotEmpty()) {
                            isLoading = true
                            authViewModel.login(email, password)
                        } else {
                            passwordError = password.isEmpty()
                            Toast.makeText(context, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Iniciar Sesión", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Texto para redirigir a la pantalla de registro
                Text(
                    text = "¿No tienes una cuenta? Regístrate aquí",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("signup")
                        }
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Barra de progreso (visible solo si isLoading es true)
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}
