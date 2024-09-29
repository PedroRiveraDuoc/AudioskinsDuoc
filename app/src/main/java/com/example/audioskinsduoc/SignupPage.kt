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
import androidx.navigation.NavHostController
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@Composable
fun SignupPage(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    // Estado para los campos de texto
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var reingresarPassword by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidoPaterno by remember { mutableStateOf("") }
    var apellidoMaterno by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    // Estados de error
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var reingresarPasswordError by remember { mutableStateOf(false) }
    var nombreError by remember { mutableStateOf(false) }
    var passwordErrorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Expresión regular para las validaciones de la contraseña
    val passwordPattern = Pattern.compile(
        "^" +
                "(?=.*[0-9])" +         // Al menos un dígito
                "(?=.*[A-Z])" +         // Al menos una letra mayúscula
                "(?=.*[a-z])" +         // Al menos una letra minúscula
                "(?=.*[@#\$%^&+=!.,])" +  // Al menos un carácter especial
                ".{8,}" +               // Al menos 8 caracteres
                "$"
    )

    fun isPasswordValid(password: String): Boolean {
        return passwordPattern.matcher(password).matches()
    }

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                isLoading = false
                navController.navigate(route = "home")
            }
            is AuthState.Error -> {
                isLoading = false
                Toast.makeText(
                    context,
                    (authState.value as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
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
                    text = "Registrarse",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Campo de correo
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
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

                // Contraseña
                TextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = !isPasswordValid(password)
                        passwordErrorMessage = when {
                            password.length < 8 -> "Debe tener al menos 8 caracteres"
                            !password.any { it.isUpperCase() } -> "Debe tener al menos una mayúscula"
                            !password.any { it.isDigit() } -> "Debe tener al menos un número"
                            !password.any { "!@#\$%^&*()_+.,=".contains(it) } -> "Debe tener al menos un carácter especial"
                            else -> ""
                        }
                    },
                    label = { Text(text = "Contraseña") },
                    isError = passwordError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
                if (passwordError) {
                    Text(
                        text = passwordErrorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Reingresar Contraseña
                TextField(
                    value = reingresarPassword,
                    onValueChange = {
                        reingresarPassword = it
                        reingresarPasswordError = reingresarPassword != password
                    },
                    label = { Text(text = "Reingresar Contraseña") },
                    isError = reingresarPasswordError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
                if (reingresarPasswordError) {
                    Text(
                        text = "Las contraseñas no coinciden",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nombre
                TextField(
                    value = nombre,
                    onValueChange = {
                        nombre = it
                        nombreError = nombre.isEmpty()
                    },
                    label = { Text(text = "Nombre") },
                    isError = nombreError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (nombreError) {
                    Text(
                        text = "El nombre es obligatorio",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Apellido Paterno
                TextField(
                    value = apellidoPaterno,
                    onValueChange = { apellidoPaterno = it },
                    label = { Text(text = "Apellido Paterno") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Apellido Materno
                TextField(
                    value = apellidoMaterno,
                    onValueChange = { apellidoMaterno = it },
                    label = { Text(text = "Apellido Materno") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Teléfono
                TextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text(text = "Teléfono") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dirección
                TextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text(text = "Dirección") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de registro
                Button(
                    onClick = {
                        if (!emailError && !passwordError && !reingresarPasswordError && !nombreError &&
                            email.isNotEmpty() && nombre.isNotEmpty() && apellidoPaterno.isNotEmpty() &&
                            apellidoMaterno.isNotEmpty() && telefono.isNotEmpty() && direccion.isNotEmpty() &&
                            password.isNotEmpty() && reingresarPassword.isNotEmpty()
                        ) {
                            isLoading = true
                            val database = FirebaseDatabase.getInstance()
                            val ref = database.getReference("usuarios")

                            // Generar un ID único para el usuario
                            val userId = ref.push().key ?: ""

                            val usuario = Usuarios(
                                id = userId, // Asignar el ID generado al usuario
                                email = email,
                                nombre = nombre,
                                apellidoPaterno = apellidoPaterno,
                                apellidoMaterno = apellidoMaterno,
                                telefono = telefono,
                                direccion = direccion,
                            )

                            // Guardar el usuario en la base de datos con el ID generado
                            ref.child(userId).setValue(usuario).addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    authViewModel.signup(email, password)
                                } else {
                                    Toast.makeText(context, "Error al ingresar el usuario", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Por favor, complete todos los campos correctamente",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Crear Cuenta", color = Color.White)
                }


                // Texto para redirigir a la pantalla de inicio de sesión
                Text(
                    text = "¿Ya tienes cuenta? Inicia sesión",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {
                            navController.navigate("login")
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
