@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.fiap.greenpath.ui.feature_cadastro_energia

import UserPreferencesRepository
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.greenpath.R
import br.com.fiap.greenpath.database.model.ClimatiqEstimateRequest
import br.com.fiap.greenpath.database.model.Co2E
import br.com.fiap.greenpath.database.model.EmissionFactorSelector
import br.com.fiap.greenpath.database.repository.Co2ERepository
import br.com.fiap.greenpath.navigation.Routes
import br.com.fiap.greenpath.service.Co2eService
import br.com.fiap.greenpath.service.RetrofitFactory
import br.com.fiap.greenpath.ui.theme.MontserratFamily
import br.com.fiap.greenpath.ui.theme.corBotoes
import br.com.fiap.greenpath.ui.theme.corFinal
import br.com.fiap.greenpath.ui.theme.corVerdeTopo
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


const val ACTIVITY_ID_ELETRICIDADE_DEFAULT = "electricity-supply_grid-source_residual_mix"
const val DATA_VERSION_CLIMATIQ = "^21"

@Composable
fun TelaCadastroEnergia(navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val bg = Brush.verticalGradient(listOf(corVerdeTopo, corFinal))
    val shape = RoundedCornerShape(16.dp)

    //instância do UserPreferencesRepository
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val userIdState: State<Long?> = userPreferencesRepository.userIdFlow.collectAsState(initial = null)




    val co2eService: Co2eService = remember { RetrofitFactory.co2eService }
    val co2ERepository = Co2ERepository(context)


    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }


    var consumoEletricidadeText by remember { mutableStateOf("") }


    fun mostrarMensagem(mensagem: String, duracao: SnackbarDuration = SnackbarDuration.Short) {
        scope.launch {
            snackbarHostState.showSnackbar(message = mensagem, duration = duracao)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(bg),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar (seu código)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.voltar),
                        contentDescription = "Voltar",
                        modifier = Modifier.size(25.dp),
                        tint = Color.Unspecified
                    )
                }
                IconButton(onClick = { navController.navigate(Routes.HOME) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = "Home",
                        modifier = Modifier.size(25.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            // Título
            Text(
                text = "Informações de Energia",
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = corBotoes,
                fontFamily = MontserratFamily,
                textAlign = TextAlign.Center
            )

            // Lembrete centralizado
            Text(
                text = "Lembre-se: você só precisa fazer isso uma vez por mês,\nusando os dados da sua conta de luz.",
                style = MaterialTheme.typography.bodySmall,
                fontFamily = MontserratFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // --- Consumo de Eletricidade ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
            ) {
                Text(
                    text = "Consumo Mensal de Eletricidade",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = MontserratFamily,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = consumoEletricidadeText,
                    onValueChange = { novo ->
                        val permitido = novo.replace(',', '.')
                        if (permitido.isEmpty() || permitido.matches(Regex("^\\d*\\.?\\d*\$"))) {
                            consumoEletricidadeText = permitido
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(200.dp),
                    singleLine = true,
                    label = { Text("Consumo em kWh") },
                    suffix = { Text("kWh", fontSize = 60.sp, fontFamily = MontserratFamily) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    shape = RoundedCornerShape(14.dp),
                    textStyle = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 100.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 100.sp
                    ),
                )
            }


            // Indicador de Carregamento
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
            }

            // Botão Cadastrar
            Button(
                onClick = {
                    val idUsuarioLogado: Long? = userIdState.value

                    val consumoEletricidadeFloat = consumoEletricidadeText.toFloatOrNull()

                    if (consumoEletricidadeFloat == null || consumoEletricidadeFloat <= 0) {
                        mostrarMensagem("Por favor, insira um consumo de eletricidade válido.")
                        return@Button
                    }

                    isLoading = true
                    if (idUsuarioLogado == null) {
                        mostrarMensagem("Erro: ID do usuário não encontrado. Faça login novamente.", SnackbarDuration.Long)
                        isLoading = false // Certifique-se de resetar o loading
                        return@Button
                    }
                    scope.launch {
                        var sucessoNaOperacao = false



                        try {
                            val requestBody = ClimatiqEstimateRequest(
                                emissionFactor = EmissionFactorSelector(
                                    activityId = ACTIVITY_ID_ELETRICIDADE_DEFAULT, // USE O ID CORRETO PARA SUA REGIÃO!
                                    dataVersion = DATA_VERSION_CLIMATIQ
                                ),
                                parameters = mapOf(
                                    "energy" to consumoEletricidadeFloat.toDouble(),
                                    "energy_unit" to "kWh"
                                )
                            )
                            val response = co2eService.calcularEmissao(requestBody = requestBody)

                            if (response.isSuccessful && response.body() != null) {
                                val apiData = response.body()!!
                                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                val dataAtualFormatada = sdf.format(Date())

                                val novaEmissao = Co2E(
                                    usuarioId = idUsuarioLogado,
                                    valorCo2e = apiData.co2e.toFloat(),
                                    unidadeCo2e = apiData.co2eUnit,
                                    dataRegistro = dataAtualFormatada

                                )
                                val idSalvo = co2ERepository.cadastrar(novaEmissao)
                                if (idSalvo > 0) {
                                    mostrarMensagem("Eletricidade: ${String.format("%.2f", apiData.co2e.toFloat())} ${apiData.co2eUnit} CO2e registrados!")
                                    consumoEletricidadeText = "" // Limpa o campo
                                    sucessoNaOperacao = true

                                } else {
                                    mostrarMensagem("Falha ao salvar eletricidade no banco.", SnackbarDuration.Long)
                                }
                            } else {
                                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido da API para Eletricidade"
                                mostrarMensagem("Erro API (Eletricidade): $errorBody", SnackbarDuration.Long)
                            }
                        } catch (e: Exception) {
                            mostrarMensagem("Erro (Eletricidade): ${e.localizedMessage ?: "Ocorreu um problema"}", SnackbarDuration.Long)
                            e.printStackTrace()
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.6f).padding(vertical = 16.dp),
                shape = shape,
                colors = ButtonDefaults.buttonColors(containerColor = corBotoes),
                enabled = !isLoading
            ) {
                Text("Cadastrar", color = Color.White)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Dúvida ?",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = MontserratFamily
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCadastroEnergia() {
    val nav = rememberNavController()
    TelaCadastroEnergia(nav)
}
