package com.chat.app.core

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.chat.app.core.ui.theme.ChatApp
import com.chat.app.navigation.AppNavGraph
import org.koin.androidx.compose.KoinAndroidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
        enableEdgeToEdge()
        setContent {
            ChatApp {
                KoinAndroidContext {
                    AppNavGraph()
                }
            }
        }
    }
}
