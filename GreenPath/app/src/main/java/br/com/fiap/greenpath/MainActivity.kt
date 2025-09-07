package br.com.fiap.greenpath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import br.com.fiap.greenpath.database.repository.UsuarioRepository
import br.com.fiap.greenpath.ui.feature_cadastro.TelaCadastro
import br.com.fiap.greenpath.ui.theme.GreenPathTheme
import kotlinx.coroutines.coroutineScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val usuarioRepository = UsuarioRepository(context)
            GreenPathTheme{

            }
        }
    }
}
