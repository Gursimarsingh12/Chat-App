package com.chat.app.chats.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chat.app.R
import com.chat.app.chats.domain.models.Message
import com.chat.app.chats.presentation.viewmodels.ChatViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = koinViewModel(),
    senderEmail: String?,
    receiverEmail: String,
    navigateToHomeScreen: () -> Unit,
    receiverName: String
) {
    val messages by viewModel.messages.collectAsState()
    val listState = rememberLazyListState()
    var text by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    DisposableEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
        onDispose { }
    }

    LaunchedEffect (key1 = senderEmail) {
        if (senderEmail != null) {
            viewModel.getAllPreviousMessages(senderEmail, receiverEmail)
        }
    }

    LaunchedEffect(receiverEmail) {
        viewModel.getMessages(receiverEmail)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chat with $receiverName",
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
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigateToHomeScreen()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    reverseLayout = true
                ) {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    items(messages.asReversed()) { message ->
                        val isOwnMessage = message.senderId == senderEmail
                        Box(
                            contentAlignment = if (isOwnMessage) {
                                Alignment.CenterEnd
                            } else Alignment.CenterStart,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .width(200.dp)
                                    .drawBehind {
                                        val cornerRadius = 10.dp.toPx()
                                        val triangleHeight = 20.dp.toPx()
                                        val triangleWidth = 25.dp.toPx()
                                        val trianglePath = Path().apply {
                                            if (isOwnMessage) {
                                                moveTo(size.width, size.height - cornerRadius)
                                                lineTo(size.width, size.height + triangleHeight)
                                                lineTo(
                                                    size.width - triangleWidth,
                                                    size.height - cornerRadius
                                                )
                                                close()
                                            } else {
                                                moveTo(0f, size.height - cornerRadius)
                                                lineTo(0f, size.height + triangleHeight)
                                                lineTo(triangleWidth, size.height - cornerRadius)
                                                close()
                                            }
                                        }
                                        drawPath(
                                            path = trianglePath,
                                            color = if (isOwnMessage) Color(0xFF093605) else Color.DarkGray
                                        )
                                    }
                                    .background(
                                        color = if (isOwnMessage) Color(0xFF093605) else Color.DarkGray,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = if (isOwnMessage) "You" else receiverName,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = message.text,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = message.formattedTime,
                                    color = Color.White,
                                    modifier = Modifier.align(Alignment.End),
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = {
                            Text(text = "Enter a message")
                        },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            if (text.isNotEmpty()) {
                                if (senderEmail != null) {
                                    viewModel.sendMessage(senderEmail, receiverEmail, text)
                                }
                                text = ""
                                if (messages.isNotEmpty()) {
                                    scope.launch {
                                        listState.animateScrollToItem(messages.size - 1)
                                    }
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send"
                        )
                    }
                }
            }
        }
    }
}


