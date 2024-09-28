package com.example.audioskinsduoc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.example.audioskinsduoc.ui.theme.AudioskinsDuocTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel by viewModels()

            AudioskinsDuocTheme {
                // Uso de Surface para aplicar el color de fondo del tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Box(modifier = Modifier.fillMaxSize()) {
                        // Imagen de fondo
                        Image(
                            painter = painterResource(id = R.drawable.background_image6), // Reemplazar con el nombre de tu imagen
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Contenido principal de la aplicación
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            containerColor = Color.Transparent // Hacer transparente el fondo del Scaffold
                        ) { innerPadding ->
                            MyAppNavigation(
                                modifier = Modifier.padding(innerPadding),
                                navController = navController,
                                authViewModel = authViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
