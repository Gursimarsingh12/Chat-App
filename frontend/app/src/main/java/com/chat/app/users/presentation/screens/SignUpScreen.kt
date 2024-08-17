package com.chat.app.users.presentation.screens

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
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.runtime.Composable
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
import com.chat.app.core.utils.Resource
import com.chat.app.core.ui.theme.hintColor
import com.chat.app.users.domain.models.User
import com.chat.app.users.presentation.components.AuthButtonComponent
import com.chat.app.users.presentation.components.AuthClickableTextComponent
import com.chat.app.users.presentation.components.AuthHeadingTextComponent
import com.chat.app.users.presentation.components.AuthTextFieldTextComponent
import com.chat.app.users.presentation.viewmodel.UsersViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    viewModel: UsersViewModel = koinViewModel(),
    navigateToHomeScreen: (String, String) -> Unit,
    navigateToLoginScreen: () -> Unit
) {
    val userState by viewModel.userState.collectAsState()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(paddingValues)
                ) {
                    AuthHeadingTextComponent(
                        text = "Sign Up",
                        modifier = Modifier
                            .padding(start = 20.dp, top = 120.dp)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    AuthClickableTextComponent(
                        unClickableText = "Already have an account? ",
                        clickableText = "Log in",
                        onClick = navigateToLoginScreen
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
                            .defaultMinSize(minHeight = 56.dp)
                            .padding(top = 2.dp, start = 20.dp, end = 20.dp),
                        isError = userState.nameError.isNotEmpty(),
                        placeholder = {
                            Text(
                                text = "John Doe",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight(400),
                                    color = hintColor,
                                    fontFamily = FontFamily(Font(R.font.sf_pro_rounded))
                                ),
                                textAlign = TextAlign.Center
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        },
                        supportingText = {
                            Text(
                                text = userState.nameError,
                                color = Color(0xFFCC3300),
                                fontFamily = FontFamily(Font(R.font.sf_pro_rounded))
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
                    OutlinedTextField(
                        value = userState.email,
                        onValueChange = {
                            viewModel.onEmailChanged(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 56.dp)
                            .padding(top = 2.dp, start = 20.dp, end = 20.dp),
                        isError = userState.emailError.isNotEmpty(),
                        placeholder = {
                            Text(
                                text = "example@example.com",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight(400),
                                    color = hintColor,
                                    fontFamily = FontFamily(Font(R.font.sf_pro_rounded))
                                ),
                                textAlign = TextAlign.Center
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = null,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        },
                        supportingText = {
                            Text(
                                text = userState.emailError,
                                color = Color(0xFFCC3300),
                                fontFamily = FontFamily(Font(R.font.sf_pro_rounded))
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
                    Spacer(modifier = Modifier.height(20.dp))
                    AuthButtonComponent(
                        onClick = {
                            viewModel.signUp()
                        },
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp, top = 25.dp)
                            .defaultMinSize(minHeight = 56.dp)
                            .fillMaxWidth(),
                        btnText = "Sign up"
                    )
                }
                if (userState.state is Resource.Loading) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(50.dp)
                                .zIndex(1f)
                                .align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                    }
                }
            }
        }
        LaunchedEffect(key1 = userState.state) {
            if (userState.state is Resource.Success) {
                navigateToHomeScreen(
                    (userState.state as Resource.Success<User>).data?.name ?: "",
                    (userState.state as Resource.Success<User>).data?.email ?: ""
                )
            } else if (userState.state is Resource.Error) {
                snackbarHostState.showSnackbar(
                    message = (userState.state as Resource.Error).message ?: "An error occurred",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}

