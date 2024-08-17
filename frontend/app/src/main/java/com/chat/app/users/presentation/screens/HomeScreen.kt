package com.chat.app.users.presentation.screens

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.chat.app.R
import com.chat.app.core.utils.Resource
import com.chat.app.navigation.Screens
import com.chat.app.users.presentation.components.AuthHeadingTextComponent
import com.chat.app.users.presentation.components.UserListItem
import com.chat.app.users.presentation.viewmodel.UsersViewModel
import org.koin.androidx.compose.koinViewModel
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    name: String,
    viewModel: UsersViewModel = koinViewModel(),
    currentEmail: String,
    onClickName: (String, String, String) -> Unit
) {
    val userState by viewModel.users.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val messageState by viewModel.message.collectAsState()


    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Home",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(
                                Font(R.font.sf_pro_rounded)
                            )
                        )
                    },
                    colors = TopAppBarColors(
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    AuthHeadingTextComponent(
                        text = "Hey $name",
                        modifier = Modifier
                            .padding(start = 20.dp, top = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(userState.users){ item ->
                            viewModel.getLatestMessages(currentEmail, item.email)
                            UserListItem(
                                userName = item.name,
                                messagePreview = if (messageState.message.senderId == item.email || messageState.message.receiverId == item.email) {
                                    messageState.message.text
                                } else {
                                    ""
                                },
                                messageTime = if (messageState.message.senderId == item.email || messageState.message.receiverId == item.email) {
                                    SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
                                } else {
                                    ""
                                },
                            ) {
                                onClickName(item.name, currentEmail, item.email)
                            }
                        }
                    }
                }
                if (userState.state is Resource.Loading || messageState.state is Resource.Loading) {
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
            }
        }
    }
    DisposableEffect(key1 = currentEmail) {
        viewModel.getAllUsers(currentEmail)

        onDispose {

        }
    }

    LaunchedEffect(key1 = Unit) {
        if (userState.state is Resource.Error) {
            snackbarHostState.showSnackbar("Error fetching users")
        }else if (userState.state is Resource.Success) {
            snackbarHostState.showSnackbar("Users fetched")
        }
    }

}