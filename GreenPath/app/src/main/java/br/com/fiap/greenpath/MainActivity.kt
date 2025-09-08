package br.com.fiap.greenpath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.fiap.greenpath.navigation.AppNav
import br.com.fiap.greenpath.ui.theme.GreenPathTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreenPathTheme {
                AppNav()
            }
        }
    }
}
