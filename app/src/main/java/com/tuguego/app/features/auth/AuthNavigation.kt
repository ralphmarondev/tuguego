package com.tuguego.app.features.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tuguego.app.features.auth.presentation.location.EnableLocationScreen
import com.tuguego.app.features.auth.presentation.login.LoginScreen
import com.tuguego.app.features.auth.presentation.register.RegisterScreen
import com.tuguego.app.features.auth.presentation.welcome.WelcomeScreen

@Composable
fun AuthNavigation(
    darkTheme: Boolean,
    toggleDarkTheme: () -> Unit,
    onAuthSuccessful: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoutes.Location
    ) {
        composable<AuthRoutes.Location> {
            EnableLocationScreen(
                onNext = {
                    navController.navigate(AuthRoutes.Welcome) {
                        popUpTo<AuthRoutes.Location> {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<AuthRoutes.Welcome> {
            WelcomeScreen(
                login = {
                    navController.navigate(AuthRoutes.Login) {
                        launchSingleTop = true
                    }
                },
                register = {
                    navController.navigate(AuthRoutes.Register) {
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<AuthRoutes.Login> {
            LoginScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                onLoginSuccessful = onAuthSuccessful,
                darkTheme = darkTheme,
                toggleDarkTheme = toggleDarkTheme
            )
        }
        composable<AuthRoutes.Register> {
            RegisterScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                darkTheme = darkTheme,
                toggleDarkTheme = toggleDarkTheme
            )
        }
    }
}