package com.kavyakanaja.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kavyakanaja.app.ui.AppNavigation
import com.kavyakanaja.app.ui.theme.KavyaKanajaTheme
import com.kavyakanaja.app.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KavyaKanajaTheme {
                val viewModel: MainViewModel = viewModel()
                AppNavigation(viewModel = viewModel)
            }
        }
    }
}
