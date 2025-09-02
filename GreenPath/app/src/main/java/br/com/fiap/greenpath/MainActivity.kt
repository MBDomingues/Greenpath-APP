package br.com.fiap.greenpath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.fiap.greenpath.model.components.TelaComLogoNoTopoCentralizada
import br.com.fiap.greenpath.ui.theme.GreenPathTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreenPathTheme{
                TelaComLogoNoTopoCentralizada(
                    modifier = Modifier,
                    texto = "Sua Jornada começa aqui",
                )
            }
        }
    }
}
