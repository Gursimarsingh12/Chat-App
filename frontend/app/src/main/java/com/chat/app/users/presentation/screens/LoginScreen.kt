package com.chat.app.users.presentation.screens

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.chat.app.R
import com.chat.app.core.ui.theme.hintColor
import com.chat.app.core.utils.Resource
import com.chat.app.users.presentation.components.AuthButtonComponent
import com.chat.app.users.presentation.components.AuthClickableTextComponent
import com.chat.app.users.presentation.components.AuthEmailTextFieldComponent
import com.chat.app.users.presentation.components.AuthHeadingTextComponent
import com.chat.app.users.presentation.components.AuthTextFieldTextComponent
import com.chat.app.users.presentation.viewmodel.UsersViewModel

@Composable
fun LoginScreen(
    viewModel: UsersViewModel = koinViewModel(),
    navigateToHomeScreen: (String, String) -> Unit,
    navigateToSignUpScreen: () -> Unit
) {
    val userState by viewModel.userState.collectAsState()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        enabled = true,
                        state = scrollState,
                        reverseScrolling = true
                    )
                    .padding(paddingValues)
            ) {
                AuthHeadingTextComponent(
                    text = "Log in",
                    modifier = Modifier
                        .padding(start = 20.dp, top = 120.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                AuthClickableTextComponent(
                    unClickableText = "Don't have and account ",
                    clickableText = "Sign up",
                    onClick = navigateToSignUpScreen
                )
                Spacer(modifier = Modifier.height(20.dp))
                AuthTextFieldTextComponent(
                    text = "Name",
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp)
                )
                OutlinedTextField(
                    value = userState.name,
                    onValueChange = {
                        viewModel.onNameChanged(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(
                            minHeight = 56.dp
                        )
                        .padding(
                            top = 2.dp,
                            start = 20.dp,
                            end = 20.dp
                        ),
                    enabled = true,
                    isError = userState.nameError.isNotEmpty(),
                    placeholder = {
                        Text(
                            text = "John Doe",
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
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(top = 2.dp)
                        )
                    },
                    supportingText = {
                        Text(
                            text = userState.nameError,
                            color = Color(0xFFCC3300),
                            fontFamily = FontFamily(
                                Font(R.font.sf_pro_rounded)
                            )
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFD8DADC),
                        focusedTextColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedBorderColor = MaterialTheme.colorScheme.primaryContainer,
                        cursorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                AuthTextFieldTextComponent(
                    text = "Email",
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp)
                )
                AuthEmailTextFieldComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(
                            minHeight = 56.dp
                        )
                        .padding(
                            top = 2.dp,
                            start = 20.dp,
                            end = 20.dp
                        )
                )
                Spacer(modifier = Modifier.height(20.dp))
                AuthButtonComponent(
                    onClick = {
                        viewModel.login()
                    },
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 25.dp
                        )
                        .defaultMinSize(
                            minHeight = 56.dp
                        )
                        .fillMaxWidth(),
                    btnText = "Log in"
                )
            }
        }
        when (userState.state) {
            Resource.Idle -> {

            }
            Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .zIndex(1f)
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }
            is Resource.Success -> {
                LaunchedEffect(key1 = userState.state) {
                    snackbarHostState.showSnackbar(
                        message = "Login successful",
                        duration = SnackbarDuration.Short
                    )
                }
                navigateToHomeScreen(userState.name, userState.email)
            }
            is Resource.Error -> {
                LaunchedEffect(key1 = userState.state) {
                    snackbarHostState.showSnackbar(
                        message = (userState.state as Resource.Error).message ?: "An unexpected error occurred!",
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
}
