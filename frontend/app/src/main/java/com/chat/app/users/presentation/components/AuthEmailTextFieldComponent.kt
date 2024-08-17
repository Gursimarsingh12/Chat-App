package com.chat.app.users.presentation.components

import android.util.Patterns
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.app.R
import com.chat.app.core.ui.theme.hintColor
import com.chat.app.users.presentation.viewmodel.UsersViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthEmailTextFieldComponent(
    modifier: Modifier,
    viewModel: UsersViewModel = koinViewModel()
){
    val userState by viewModel.userState.collectAsState()
    OutlinedTextField(
        value = userState.email,
        onValueChange = {
            viewModel.onEmailChanged(it)
        },
        modifier = modifier,
        enabled = true,
        placeholder = {
            Text(
                text = "example@gmail.com",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = hintColor,
                    fontFamily = FontFamily(
                        Font(R.font.sf_pro_rounded)
                    )
                ),
                textAlign = TextAlign.Center
            )
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.AlternateEmail,
                contentDescription = "",
                modifier = Modifier
                    .padding(top = 2.dp)
            )
        },
        isError = userState.emailError.isNotEmpty(),
        supportingText = {
            Text(
                text = userState.emailError,
                color = Color(0xFFCC3300),
                fontFamily = FontFamily(
                    Font(R.font.sf_pro_rounded)
                )
            )
        },
        shape = RoundedCornerShape(10.dp),
        trailingIcon = {
            when {
                userState.email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(userState.email)
                    .matches() && userState.emailError.isEmpty() -> {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = ""
                    )
                }
                userState.email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(userState.email).matches()
                    .not() -> {
                    Icon(imageVector = Icons.Filled.Cancel, contentDescription = "")
                }

                userState.email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(userState.email).matches()
                    .not() && userState.emailError.isNotEmpty() -> {
                    Icon(imageVector = Icons.Filled.Cancel, contentDescription = "")
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Default
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFD8DADC),
            focusedTextColor = MaterialTheme.colorScheme.primaryContainer,
            focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
            cursorColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}