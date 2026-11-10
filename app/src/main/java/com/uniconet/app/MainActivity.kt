package com.uniconet.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.uniconet.app.navigation.AppNavigation
import com.uniconet.app.ui.theme.UniconetTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            UniconetTheme {
                AppNavigation()
            }
        }
    }
}