package com.tuguego.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tuguego.app.core.data.local.preferences.AppPreferences
import com.tuguego.app.features.auth.AuthNavigation
import com.tuguego.app.features.home.presentation.map.MapScreen
import com.tuguego.app.features.onboarding.presentation.OnboardingScreen
import com.tuguego.app.ui.theme.TugueGoTheme

@Composable
fun AppNavigation(
    preferences: AppPreferences,
    navController: NavHostController = rememberNavController()
) {
    var darkTheme by remember { mutableStateOf(preferences.isDarkTheme()) }

    TugueGoTheme(
        darkTheme = darkTheme
    ) {
        // TODO:
        //  - Handle if there is already a user logged in, if there is, we will not login again! :>
        val startDestination: Any = if (preferences.isFirstLaunch()) {
            Routes.Onboarding
        } else {
            Routes.Auth
        }

        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable<Routes.Onboarding> {
                OnboardingScreen(
                    onboardingCompleted = {
                        preferences.setFirstLaunchAsDone()
                        navController.navigate(Routes.Auth) {
                            popUpTo<Routes.Onboarding> {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable<Routes.Auth> {
                AuthNavigation(
                    onAuthSuccessful = {
                        navController.navigate(Routes.Home) {
                            popUpTo<Routes.Auth> {
                                inclusive = true
                            }
                        }
                    },
                    darkTheme = darkTheme,
                    toggleDarkTheme = {
                        darkTheme = !darkTheme
                        preferences.toggleDarkTheme()
                    }
                )
            }

            composable<Routes.Home> {
                MapScreen()
            }
        }
    }
}