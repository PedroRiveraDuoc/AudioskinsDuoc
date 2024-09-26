package com.example.audioskinsduoc

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable(route = "login") {
            LoginPage(
                modifier = modifier,
                navController = navController,
                onLoginSuccess = { navController.navigate("home") },
                authViewModel = authViewModel
            )
        }
        composable(route = "signup") {
            SignupPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(route = "home") {
            HomePage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }
    }
}
