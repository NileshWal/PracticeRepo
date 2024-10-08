package com.nilesh.practiceapps.ui.composeviews

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomCardView(modifier: Modifier = Modifier, text: String = "") {
    Card(modifier = modifier) {
        TextView(modifier = Modifier.padding(4.dp), text = text, textAlign = TextAlign.Start)
    }
}

@Composable
fun TextView(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign = TextAlign.Center,
    textColor: Color = Color.Unspecified
) {
    Text(modifier = modifier, text = text, textAlign = textAlign, color = textColor)
}