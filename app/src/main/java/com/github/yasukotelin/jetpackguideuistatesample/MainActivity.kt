package com.github.yasukotelin.jetpackguideuistatesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.yasukotelin.jetpackguideuistatesample.ui.home.HomeScreen
import com.github.yasukotelin.jetpackguideuistatesample.ui.second.SecondScreen
import com.github.yasukotelin.jetpackguideuistatesample.ui.theme.JetpackGuideUiStateSampleTheme
import com.github.yasukotelin.jetpackguideuistatesample.ui.third.ThirdScreen

class MainActivity : ComponentActivity() {

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackGuideUiStateSampleTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController = navController) }
                    composable("second") { SecondScreen() }
                    composable(
                        "third/{count}",
                        arguments = listOf(navArgument("count") { type = NavType.IntType }),
                    ) {
                        ThirdScreen(count = it.arguments?.getInt("count") ?: 0)
                    }
                }
            }
        }
    }
}