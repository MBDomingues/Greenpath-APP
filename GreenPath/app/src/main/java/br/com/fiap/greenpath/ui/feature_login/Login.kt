package br.com.fiap.greenpath.screens

import UserPreferencesRepository
import android.widget.Toast
import androidx.activity.result.launch
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.greenpath.ui.theme.MontserratFamily
import br.com.fiap.greenpath.ui.theme.corBotoes
import br.com.fiap.greenpath.ui.theme.corFinal
import br.com.fiap.greenpath.ui.theme.corVerdeTopo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import br.com.fiap.greenpath.database.repository.UsuarioRepository
import br.com.fiap.greenpath.navigation.Routes
import br.com.fiap.greenpath.ui.components.Logo
import kotlinx.coroutines.launch


@Composable
fun Login(modifier: Modifier = Modifier, navController: NavController) {
    val brushDegradeVertical = Brush.verticalGradient(
        colors = listOf(corVerdeTopo, corFinal)
    )

    var context = LocalContext.current
    var usuarioRepository = UsuarioRepository(context)
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    var usuario by rememberSaveable { mutableStateOf("") }

    var senha by rememberSaveable { mutableStateOf("") }

    var senhaVisivel by rememberSaveable { mutableStateOf(false) }

    val scope = rememberCoroutineScope() // Para lançar coroutines

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = brushDegradeVertical)
    ){
        Column() {
            Logo(
                texto = "Faça seu login",
                modifier = Modifier
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = modifier
                        .padding(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Campo de Usuário
                    OutlinedTextField(
                        value = usuario,
                        onValueChange = { usuario = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nome de usuário") },
                        shape = RoundedCornerShape(20.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de Senha
                    OutlinedTextField(
                        value = senha,
                        onValueChange = { senha = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Senha") },
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
                Row(
                    modifier = Modifier
                        .width(150.dp)
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                scope.launch {
                                    try {
                                        val usuarioDoBanco = usuarioRepository.buscarPorNomeUsuario(usuario)
                                        println(usuarioDoBanco) // Para debug

                                        if (usuarioDoBanco.passWord == senha) {
                                            userPreferencesRepository.saveUserId(usuarioDoBanco.id)
                                            navController.navigate(Routes.HOME) {
                                                popUpTo(Routes.INICIAL) { inclusive = true }
                                            }
                                        } else {
                                            Toast.makeText(context, "Senha incorreta. Tente novamente.", Toast.LENGTH_SHORT).show()
                                            println("Senha incorreta informada ao usuário via Toast")

                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Erro ao fazer Login.", Toast.LENGTH_SHORT).show()
                                        println("Exceção no login: ${e.message}")
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = usuario.isNotBlank() && senha.isNotBlank(),
                            colors =ButtonDefaults.buttonColors(
                                containerColor = corBotoes)
                        ) {
                            Text("Entrar")
                        }
                        Text(
                            text = "esqueci minha senha",
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

