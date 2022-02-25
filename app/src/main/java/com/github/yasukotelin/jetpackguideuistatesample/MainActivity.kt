package com.github.yasukotelin.jetpackguideuistatesample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import com.github.yasukotelin.jetpackguideuistatesample.ui.home.HomeScreen
import com.github.yasukotelin.jetpackguideuistatesample.ui.theme.JetpackGuideUiStateSampleTheme

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackGuideUiStateSampleTheme {
                HomeScreen()
            }
        }
    }
}