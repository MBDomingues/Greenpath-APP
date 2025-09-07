package br.com.fiap.greenpath.ui.feature_cadastro

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
    
    //instancia do repositório
    var context = LocalContext.current
    val usuarioRepository = UsuarioRepository(context)

    val scope = rememberCoroutineScope()    
    
    val brushDegradeVertical = Brush.verticalGradient(
        colors = listOf(corVerdeTopo, corFinal)
    )

    // Estados para os campos do formulário de cadastro
    var email by rememberSaveable { mutableStateOf("") }
    var usuario by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var confirmarSenha by rememberSaveable { mutableStateOf("") }

    // Estados para visibilidade das senhas
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    var confirmarSenhaVisivel by rememberSaveable { mutableStateOf(false) }


    val senhasCoincidem = senha == confirmarSenha && senha.isNotBlank()



    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = brushDegradeVertical)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Logo(
                texto = "Crie sua conta",
                modifier = Modifier
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Campo de E-mail
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("E-mail") },
                    shape = RoundedCornerShape(20.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Campo de Usuário
                OutlinedTextField(
                    value = usuario,
                    onValueChange = { usuario = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nome de usuário") },
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Campo de Senha
                OutlinedTextField(
                    value = senha,
                    onValueChange = { senha = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Senha") },
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                    singleLine = true,
                    visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),

                )
                Spacer(modifier = Modifier.height(12.dp))

                // Campo de Confirmar Senha
                OutlinedTextField(
                    value = confirmarSenha,
                    onValueChange = { confirmarSenha = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Confirmar Senha") },
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                    singleLine = true,
                    visualTransformation = if (confirmarSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                )
                if (confirmarSenha.isNotBlank() && !senhasCoincidem) {
                    Text(
                        text = "As senhas não coincidem",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.width(200.dp)
                ) {
                    Button(
                        //Realiza o cadastrio
                        onClick = {
                            val usuarioNovo = Usuario(
                                user = usuario,
                                passWord = senha,
                                email = email
                            )
                            scope.launch { 
                                try {
                                    println("Iniciando cadastro...")
                                    val idNovoUsuario = usuarioRepository.cadastrar(usuarioNovo) 

                                    if (idNovoUsuario > 0) {
                                        Toast.makeText(context, "Conta criada com sucesso.", Toast.LENGTH_SHORT).show()
                                        navController.navigate(Routes.LOGIN) {
                                            popUpTo(Routes.INICIAL) { inclusive = true }
                                        }
                                    } else {
                                        Toast.makeText(context, "Falha ao criar conta.", Toast.LENGTH_SHORT).show()
                                        println("Falha ao criar conta")
                                    }
                                } catch (e: Exception) {

                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = email.isNotBlank() && usuario.isNotBlank() && senhasCoincidem, // Habilita se tudo estiver ok
                        colors = ButtonDefaults.buttonColors(
                            containerColor = corBotoes
                        )
                    ) {
                        Text("Cadastrar")
                    }
                }
            }
        }
    }
}
