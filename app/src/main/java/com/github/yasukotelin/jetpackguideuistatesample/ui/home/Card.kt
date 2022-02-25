package com.github.yasukotelin.jetpackguideuistatesample.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.github.yasukotelin.jetpackguideuistatesample.model.CardData

@ExperimentalMaterialApi
@Composable
fun LabelCard(
    modifier: Modifier = Modifier,
    card: CardData,
    onClick: (card: CardData) -> Unit,
) {
    Card(
        modifier = modifier.wrapContentHeight(),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        onClick = { onClick(card) },
    ) {
        Box(Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                CardThumbnail(url = card.url, modifier = Modifier.size(80.dp))

                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(8.dp)
                ) {
                    CardTitle(title = card.title)
                    CardDescription(description = card.description)
                }
            }

            if (!card.enable) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color(0x88FFFFFF))
                )
            }
        }
    }
}

@Composable
private fun CardThumbnail(modifier: Modifier = Modifier, url: String) {
    Image(
        painter = rememberImagePainter(url),
        contentDescription = null,
        modifier = modifier
    )
}

@Composable
private fun CardTitle(title: String) {
    Text(title, fontSize = 22.sp)
}

@Composable
private fun CardDescription(description: String) {
    Text(description, fontSize = 16.sp, softWrap = true)
}