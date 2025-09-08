package br.com.fiap.greenpath.ui.feature_cadastro

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.greenpath.R
import br.com.fiap.greenpath.database.model.Usuario
import br.com.fiap.greenpath.database.repository.UsuarioRepository
import br.com.fiap.greenpath.navigation.Routes
import br.com.fiap.greenpath.ui.components.Logo
import br.com.fiap.greenpath.ui.theme.corBotoes
import br.com.fiap.greenpath.ui.theme.corFinal
import br.com.fiap.greenpath.ui.theme.corVerdeTopo
import kotlinx.coroutines.launch

@Composable
fun TelaCadastro(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current
    val usuarioRepository = UsuarioRepository(context)
    val scope = rememberCoroutineScope()

    val brushDegradeVertical = Brush.verticalGradient(colors = listOf(corVerdeTopo, corFinal))

    var email by rememberSaveable { mutableStateOf("") }
    var usuario by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var confirmarSenha by rememberSaveable { mutableStateOf("") }

    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    var confirmarSenhaVisivel by rememberSaveable { mutableStateOf(false) }

    val senhasCoincidem = senha == confirmarSenha && senha.isNotBlank()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = brushDegradeVertical)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Logo(texto = stringResource(R.string.gp_signup_title), modifier = Modifier)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // E-mail
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.gp_label_email)) },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Usuário
                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.gp_label_username)) },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Senha
                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.gp_label_password)) },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Confirmar senha
                OutlinedTextField(
                    value = confirmarSenha,
                    onValueChange = { confirmarSenha = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.gp_label_confirm_password)) },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    visualTransformation = if (confirmarSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                )

                if (confirmarSenha.isNotBlank() && !senhasCoincidem) {
                    Text(
                        text = stringResource(R.string.gp_error_password_mismatch),
                        color = androidx.compose.material3.MaterialTheme.colorScheme.error,
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.width(200.dp)) {
                    Button(
                        onClick = {
                            val usuarioNovo = Usuario(user = usuario, passWord = senha, email = email)
                            scope.launch {
                                try {
                                    val idNovoUsuario = usuarioRepository.cadastrar(usuarioNovo)
                                    if (idNovoUsuario > 0) {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.gp_toast_account_created),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigate(Routes.LOGIN) {
                                            popUpTo(Routes.INICIAL) { inclusive = true }
                                        }
                                    } else {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.gp_toast_account_failed),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        context.getString(
                                            R.string.gp_generic_error,
                                            e.localizedMessage ?: "Ocorreu um problema"
                                        ),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = email.isNotBlank() && usuario.isNotBlank() && senhasCoincidem,
                        colors = ButtonDefaults.buttonColors(containerColor = corBotoes)
                    ) {
                        Text(stringResource(R.string.gp_btn_sign_up))
                    }
                }
            }
        }
    }
}
