package com.chat.app.users.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.chat.app.R

@Composable
fun AuthTextFieldTextComponent(text: String, modifier: Modifier) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight(400),
            color = MaterialTheme.colorScheme.primaryContainer,
            fontFamily = FontFamily(
                Font(R.font.poppins_regular)
            )
        ),
        modifier = modifier
    )
}