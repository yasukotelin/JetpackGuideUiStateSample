package com.github.yasukotelin.jetpackguideuistatesample.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(text: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth(),
    ) {
        Text(text, fontSize = 22.sp)
    }
}