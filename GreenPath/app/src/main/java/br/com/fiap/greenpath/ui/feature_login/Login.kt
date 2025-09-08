package br.com.fiap.greenpath.ui.feature_login

import UserPreferencesRepository
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.greenpath.R
import br.com.fiap.greenpath.database.repository.UsuarioRepository
import br.com.fiap.greenpath.navigation.Routes
import br.com.fiap.greenpath.ui.components.Logo
import br.com.fiap.greenpath.ui.theme.MontserratFamily
import br.com.fiap.greenpath.ui.theme.corBotoes
import br.com.fiap.greenpath.ui.theme.corFinal
import br.com.fiap.greenpath.ui.theme.corVerdeTopo
import kotlinx.coroutines.launch

@Composable
fun Login(modifier: Modifier = Modifier, navController: NavController) {
    val brushDegradeVertical = Brush.verticalGradient(
        colors = listOf(corVerdeTopo, corFinal)
    )

    val context = LocalContext.current
    val usuarioRepository = UsuarioRepository(context)
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    var usuario by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = brushDegradeVertical)
    ){
        Column {
            Logo(
                texto = stringResource(R.string.gp_login_title),
                modifier = Modifier
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = modifier.padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Usuário
                    OutlinedTextField(
                        value = usuario,
                        onValueChange = { usuario = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.gp_label_username)) },
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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
                            imeAction = ImeAction.Done
                        ),
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(modifier = Modifier.width(150.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = {
                                scope.launch {
                                    try {
                                        val usuarioDoBanco = usuarioRepository.buscarPorNomeUsuario(usuario)
                                        if (usuarioDoBanco.passWord == senha) {
                                            userPreferencesRepository.saveUserId(usuarioDoBanco.id)
                                            navController.navigate(Routes.HOME) {
                                                popUpTo(Routes.INICIAL) { inclusive = true }
                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.gp_error_wrong_password),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            context.getString(R.string.gp_error_login),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = usuario.isNotBlank() && senha.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = corBotoes)
                        ) {
                            Text(stringResource(R.string.gp_btn_sign_in))
                        }
                        Text(
                            text = stringResource(R.string.gp_forgot_password),
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            lineHeight = 24.sp,
                            fontFamily = MontserratFamily,
                            color = corBotoes
                        )
                    }
                }
            }
        }
    }
}
