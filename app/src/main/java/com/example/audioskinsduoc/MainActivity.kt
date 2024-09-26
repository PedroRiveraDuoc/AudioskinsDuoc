package com.example.audioskinsduoc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.audioskinsduoc.ui.theme.AudioskinsDuocTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel by viewModels() // Usa el ViewModel

            AudioskinsDuocTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController, // Pasa el navController aquí
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}
