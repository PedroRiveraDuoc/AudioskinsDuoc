package com.example.audioskinsduoc

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.google.firebase.database.*

@Composable
fun UserManagement(navController: NavHostController) {
    val context = LocalContext.current
    var userList by remember { mutableStateOf(listOf<Usuarios>()) }
    var selectedUser by remember { mutableStateOf<Usuarios?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) } // Nuevo estado para la barra de progreso

    // Conexión con Firebase
    val database = FirebaseDatabase.getInstance()
    val ref = database.getReference("usuarios")

    // Cargar la lista de usuarios
    LaunchedEffect(Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<Usuarios>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(Usuarios::class.java)
                    if (user != null) users.add(user)
                }
                userList = users
                isLoading = false
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error al cargar usuarios", Toast.LENGTH_SHORT).show()
                isLoading = false
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Título
        Text(
            text = "Administración de Usuarios", // Texto fijo
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        if (isLoading) {
            // Mostrar barra de progreso mientras se cargan los datos
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (isEditing) {
            // Formulario de edición
            UserEditForm(
                user = selectedUser,
                onSave = { user ->
                    if (user != null) {
                        ref.child(user.id).setValue(user)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                                isEditing = false
                                selectedUser = null
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Error al actualizar el usuario", Toast.LENGTH_SHORT).show()
                            }
                    }
                },
                onCancel = {
                    isEditing = false
                    selectedUser = null
                }
            )
        } else {
            // Mostrar lista de usuarios
            LazyColumn {
                items(userList) { user ->
                    UserCard(
                        user = user,
                        onEdit = {
                            selectedUser = it
                            isEditing = true
                        },
                        onDelete = {
                            ref.child(it.id).removeValue()
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Error al eliminar el usuario", Toast.LENGTH_SHORT).show()
                                }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UserCard(user: Usuarios, onEdit: (Usuarios) -> Unit, onDelete: (Usuarios) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "${user.nombre} ${user.apellidoPaterno}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Correo: ${user.email}", fontSize = 14.sp)
            Text(text = "Teléfono: ${user.telefono}", fontSize = 14.sp)
            Text(text = "Dirección: ${user.direccion}", fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { onEdit(user) }) {
                    Text("Editar", color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = { onDelete(user) }) {
                    Text("Eliminar", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun UserEditForm(
    user: Usuarios?,
    onSave: (Usuarios?) -> Unit,
    onCancel: () -> Unit
) {
    var nombre by remember { mutableStateOf(user?.nombre ?: "") }
    var apellidoPaterno by remember { mutableStateOf(user?.apellidoPaterno ?: "") }
    var apellidoMaterno by remember { mutableStateOf(user?.apellidoMaterno ?: "") }
    var telefono by remember { mutableStateOf(user?.telefono ?: "") }
    var direccion by remember { mutableStateOf(user?.direccion ?: "") }

    // Usamos Box para centrar la Card verticalmente
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center // Centrar el contenido
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Margen alrededor de la Card
            elevation = CardDefaults.cardElevation(8.dp) // Sombra para la Card
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp) // Espacio interno de la Card
            ) {
                // Título dentro de la tarjeta
                Text(
                    text = "Editar Usuario",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                )

                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = apellidoPaterno,
                    onValueChange = { apellidoPaterno = it },
                    label = { Text("Apellido Paterno") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = apellidoMaterno,
                    onValueChange = { apellidoMaterno = it },
                    label = { Text("Apellido Materno") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = { onSave(user?.copy(nombre = nombre, apellidoPaterno = apellidoPaterno, apellidoMaterno = apellidoMaterno, telefono = telefono, direccion = direccion)) }) {
                        Text("Guardar")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(onClick = { onCancel() }) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}



