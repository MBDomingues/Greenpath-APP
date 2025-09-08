@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.fiap.greenpath.ui.feature_cadastro_energia

import UserPreferencesRepository
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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

    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val userIdState: State<Long?> = userPreferencesRepository.userIdFlow.collectAsState(initial = null)

    val co2eService: Co2eService = remember { RetrofitFactory.co2eService }
    val co2ERepository = Co2ERepository(context)

    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    var consumoEletricidadeText by remember { mutableStateOf("") }

    fun mostrarMensagem(mensagem: String, duracao: SnackbarDuration = SnackbarDuration.Short) {
        scope.launch { snackbarHostState.showSnackbar(message = mensagem, duration = duracao) }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(bg),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.voltar),
                        contentDescription = stringResource(R.string.gp_cd_back),
                        modifier = Modifier.size(25.dp),
                        tint = Color.Unspecified
                    )
                }
                IconButton(onClick = { navController.navigate(Routes.HOME) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.home),
                        contentDescription = stringResource(R.string.gp_cd_home_icon),
                        modifier = Modifier.size(25.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            // Título
            Text(
                text = stringResource(R.string.gp_energy_title),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = corBotoes,
                fontFamily = MontserratFamily,
                textAlign = TextAlign.Center
            )

            // Lembrete
            Text(
                text = stringResource(R.string.gp_energy_reminder),
                style = MaterialTheme.typography.bodySmall,
                fontFamily = MontserratFamily,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Consumo
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.gp_energy_monthly_label),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = MontserratFamily,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = consumoEletricidadeText,
                    onValueChange = { novo ->
                        val permitido = novo.replace(',', '.')
                        if (permitido.isEmpty() || permitido.matches(Regex("^\\d*\\.?\\d*$"))) {
                            consumoEletricidadeText = permitido
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(200.dp),
                    singleLine = true,
                    label = { Text(stringResource(R.string.gp_energy_kwh_label)) },
                    suffix = {
                        Text(
                            stringResource(R.string.gp_unit_kwh),
                            fontSize = 60.sp,
                            fontFamily = MontserratFamily
                        )
                    },
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

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
            }

            // Botão
            Button(
                onClick = {
                    val idUsuarioLogado: Long? = userIdState.value
                    val consumoEletricidadeFloat = consumoEletricidadeText.toFloatOrNull()

                    if (consumoEletricidadeFloat == null || consumoEletricidadeFloat <= 0) {
                        mostrarMensagem(context.getString(R.string.gp_msg_energy_invalid))
                        return@Button
                    }

                    isLoading = true
                    if (idUsuarioLogado == null) {
                        mostrarMensagem(context.getString(R.string.gp_msg_userid_missing), SnackbarDuration.Long)
                        isLoading = false
                        return@Button
                    }
                    scope.launch {
                        try {
                            val requestBody = ClimatiqEstimateRequest(
                                emissionFactor = EmissionFactorSelector(
                                    activityId = ACTIVITY_ID_ELETRICIDADE_DEFAULT,
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
                                    mostrarMensagem(
                                        context.getString(
                                            R.string.gp_msg_energy_saved,
                                            apiData.co2e.toFloat(),
                                            apiData.co2eUnit
                                        )
                                    )
                                    consumoEletricidadeText = ""
                                } else {
                                    mostrarMensagem(context.getString(R.string.gp_msg_energy_db_fail), SnackbarDuration.Long)
                                }
                            } else {
                                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido da API para Eletricidade"
                                mostrarMensagem(context.getString(R.string.gp_msg_energy_api_error, errorBody), SnackbarDuration.Long)
                            }
                        } catch (e: Exception) {
                            mostrarMensagem(
                                context.getString(
                                    R.string.gp_generic_error,
                                    e.localizedMessage ?: "Ocorreu um problema"
                                ),
                                SnackbarDuration.Long
                            )
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .padding(vertical = 16.dp),
                shape = shape,
                colors = ButtonDefaults.buttonColors(containerColor = corBotoes),
                enabled = !isLoading
            ) {
                Text(stringResource(R.string.gp_btn_submit), color = Color.White)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(R.string.gp_question_short),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = MontserratFamily
                )
            }
        }
    }
}
