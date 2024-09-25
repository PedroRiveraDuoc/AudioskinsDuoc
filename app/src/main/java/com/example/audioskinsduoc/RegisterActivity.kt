package com.example.audioskinsduoc

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.util.PatternsCompat
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegistroUsuarioScreen()
        }
    }
}

@Composable
fun RegistroUsuarioScreen() {
    val context = LocalContext.current
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

    Box(
        modifier = Modifier
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

                // Email
                TextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = !PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
                    },
                    label = { Text(text = "Email") },
                    isError = emailError,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                if (emailError) {
                    Text(
                        text = "Por favor ingrese un email válido",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Contraseña
                TextField(
                    value = password,
                    onValueChange = { it ->
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

                // Botón de registrarse
                Button(
                    onClick = {
                        // Validar que no haya errores en los campos y que estén completos
                        if (!emailError && !passwordError && !reingresarPasswordError && !nombreError &&
                            email.isNotEmpty() && nombre.isNotEmpty() && apellidoPaterno.isNotEmpty() &&
                            apellidoMaterno.isNotEmpty() && telefono.isNotEmpty() && direccion.isNotEmpty() &&
                            password.isNotEmpty() && reingresarPassword.isNotEmpty()) {

                            isLoading = true

                            val database = FirebaseDatabase.getInstance()
                            val ref = database.getReference("usuarios")

                            val usuario = Usuarios(
                                email = email,
                                nombre = nombre,
                                apellidoPaterno = apellidoPaterno,
                                apellidoMaterno = apellidoMaterno,
                                telefono = telefono,
                                direccion = direccion,
                            )

                            ref.push().setValue(usuario).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Data Ingresada", Toast.LENGTH_LONG).show()
                                    isLoading = false // Oculta la barra de progreso
                                } else {
                                    Toast.makeText(context, "Error al ingresar el usuario", Toast.LENGTH_LONG).show()
                                    isLoading = false // Oculta la barra de progreso
                                }
                            }

                        } else {
                            // Opcional: Mostrar un mensaje de error si los campos no están completos
                            Toast.makeText(context, "Por favor, completa todos los campos correctamente.", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = "Registrarse", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Barra de progreso (solo visible si isLoading es true)
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

@Preview(showBackground = true)
@Composable
fun PreviewRegistroUsuarioScreen() {
    RegistroUsuarioScreen()
}
