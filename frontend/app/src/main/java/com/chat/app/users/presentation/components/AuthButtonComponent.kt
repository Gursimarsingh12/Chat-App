package com.chat.app.users.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.app.R

@Composable
fun AuthButtonComponent(
    onClick: () ->Unit,
    btnText: String,
    modifier: Modifier
){
    ElevatedButton(
        onClick = {
            onClick()
        },
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 20.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        )

    )
    {
        Text(
            text = btnText,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(
                Font(R.font.sf_pro_rounded)
            )
        )
    }
}