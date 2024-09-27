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
import androidx.navigation.NavHostController
import com.google.firebase.database.*

@Composable
fun UserManagement(navController: NavHostController) {
    val context = LocalContext.current
    var userList by remember { mutableStateOf(listOf<Usuarios>()) } // Lista de usuarios
    var selectedUser by remember { mutableStateOf<Usuarios?>(null) } // Usuario seleccionado para edición
    var isEditing by remember { mutableStateOf(false) } // Estado para saber si estamos en modo edición

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
                    user?.id = userSnapshot.key ?: "" // Asigna el ID del usuario desde Firebase
                    if (user != null) users.add(user)
                }
                userList = users
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error al cargar usuarios", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = if (isEditing) "Editar Usuario" else "Administración de Usuarios",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isEditing) {
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
                    UserRow(
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
fun UserRow(user: Usuarios, onEdit: (Usuarios) -> Unit, onDelete: (Usuarios) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = user.email)
        Row {
            TextButton(onClick = { onEdit(user) }) {
                Text("Editar")
            }
            TextButton(onClick = { onDelete(user) }) {
                Text("Eliminar", color = Color.Red)
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
    var telefono by remember { mutableStateOf(user?.telefono ?: "") }
    var direccion by remember { mutableStateOf(user?.direccion ?: "") }

    Column {
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = telefono,
            onValueChange = { telefono = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = { onSave(user?.copy(nombre = nombre, telefono = telefono, direccion = direccion)) }) {
                Text("Guardar")
            }
            Spacer(modifier = Modifier.width(16.dp))
            TextButton(onClick = { onCancel() }) {
                Text("Cancelar")
            }
        }
    }
}
