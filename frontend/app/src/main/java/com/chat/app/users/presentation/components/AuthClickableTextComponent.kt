package com.chat.app.users.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.app.R

@Composable
fun AuthClickableTextComponent(
    unClickableText: String,
    clickableText: String,
    onClick: () -> Unit,
) {
    Text(
        text = buildAnnotatedString {
            append(unClickableText)
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    fontStyle = FontStyle.Normal,
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(
                        Font(R.font.poppins_regular)
                    )
                )
            )
            {
                append(clickableText)
            }
        },
        style = TextStyle(
            color = MaterialTheme.colorScheme.primaryContainer,
            fontStyle = FontStyle.Normal,
            fontFamily = FontFamily(
                Font(R.font.poppins_regular)
            ),
            fontSize = 14.sp
        ),
        modifier = Modifier.padding(
            start = 20.dp
        )
            .clickable {
                onClick()
            }
        ,
    )
}