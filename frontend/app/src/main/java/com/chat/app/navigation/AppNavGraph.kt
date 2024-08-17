package com.chat.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.chat.app.chats.presentation.screens.ChatScreen
import com.chat.app.users.presentation.screens.HomeScreen
import com.chat.app.users.presentation.screens.LoginScreen
import com.chat.app.users.presentation.screens.SignUpScreen
import com.chat.app.users.presentation.viewmodel.UsersViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavGraph(
    viewModel: UsersViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) {
            Screens.HomeScreen(
                name = viewModel.getStoredUser()?.name ?: "",
                email = viewModel.getStoredUser()?.email ?: ""
            )
        } else {
            Screens.LoginScreen
        }
    ) {
        composable<Screens.LoginScreen> {
            LoginScreen(
                navigateToHomeScreen = { name, email ->
                    navController.navigate(
                        Screens.HomeScreen(name, email)
                    ) {
                        popUpTo(Screens.LoginScreen) { inclusive = true }
                    }
                },
                navigateToSignUpScreen = {
                    navController.navigate(
                        Screens.SignUpScreen
                    )
                }
            )
        }
        composable<Screens.SignUpScreen> {
            SignUpScreen(
                navigateToHomeScreen = { name, email ->
                    navController.navigate(
                        Screens.HomeScreen(name, email)
                    ) {
                        popUpTo(Screens.SignUpScreen) { inclusive = true }
                    }
                },
                navigateToLoginScreen = {
                    navController.navigate(
                        Screens.LoginScreen
                    )
                }
            )
        }
        composable<Screens.HomeScreen> {
            val args = it.toRoute<Screens.HomeScreen>()
            HomeScreen(
                name = args.name,
                currentEmail = args.email,
                onClickName = { receiverName, senderEmail, receiverEmail ->
                    navController.navigate(
                        Screens.ChatScreen(
                            receiverName, senderEmail, receiverEmail
                        )
                    )
                }
            )
        }
        composable<Screens.ChatScreen> {
            val args = it.toRoute<Screens.ChatScreen>()
            ChatScreen(
                receiverEmail = args.receiverEmail,
                senderEmail = args.senderEmail,
                receiverName = args.receiverName,
                navigateToHomeScreen = {
                    navController.popBackStack()
                }
            )
        }
    }
}