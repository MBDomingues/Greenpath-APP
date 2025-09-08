@file:OptIn(ExperimentalSerializationApi::class, ExperimentalMaterial3Api::class)

package br.com.fiap.greenpath.ui.feature_cadastro_transporte

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
import br.com.fiap.greenpath.database.model.OpcaoTransporteApi
import br.com.fiap.greenpath.database.model.listaOpcoesTransporteApi
import br.com.fiap.greenpath.database.repository.Co2ERepository
import br.com.fiap.greenpath.navigation.Routes
import br.com.fiap.greenpath.service.Co2eService
import br.com.fiap.greenpath.service.RetrofitFactory
import br.com.fiap.greenpath.ui.theme.MontserratFamily
import br.com.fiap.greenpath.ui.theme.corBotoes
import br.com.fiap.greenpath.ui.theme.corFinal
import br.com.fiap.greenpath.ui.theme.corVerdeTopo
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TelaCadastroTransporte(navController: NavController) {
    val bg = Brush.verticalGradient(listOf(corVerdeTopo, corFinal))

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val userIdState: State<Long?> = userPreferencesRepository.userIdFlow.collectAsState(initial = null)

    val co2eService: Co2eService = remember { RetrofitFactory.co2eService }
    val co2ERepository = Co2ERepository(context)

    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    var expanded by remember { mutableStateOf(false) }
    val opcoesApi = listaOpcoesTransporteApi
    var tipoSelecionadoApi by remember { mutableStateOf(opcoesApi.firstOrNull() ?: OpcaoTransporteApi("Erro", null)) }

    var distanciaText by remember { mutableStateOf("") }

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
                text = stringResource(R.string.gp_transport_title),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 24.sp,
                fontFamily = MontserratFamily,
                textAlign = TextAlign.Center
            )

            // Select: Tipo de Transporte
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.gp_transport_type_label),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = MontserratFamily,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    OutlinedTextField(
                        value = tipoSelecionadoApi.nomeExibicao,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        shape = RoundedCornerShape(14.dp),
                        textStyle = MaterialTheme.typography.bodyLarge
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        opcoesApi.forEach { opc ->
                            DropdownMenuItem(
                                text = { Text(opc.nomeExibicao) },
                                onClick = {
                                    tipoSelecionadoApi = opc
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Distância
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.gp_transport_distance_label),
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = MontserratFamily,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = distanciaText,
                    onValueChange = { novo ->
                        val permitido = novo.replace(',', '.')
                        if (permitido.isEmpty() || permitido.matches(Regex("^\\d*\\.?\\d*$"))) {
                            distanciaText = permitido
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(200.dp),
                    singleLine = true,
                    suffix = {
                        Text(
                            stringResource(R.string.gp_unit_km),
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
                    )
                )
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
            }

            // Botão
            Button(
                onClick = {
                    val idUsuarioLogado: Long? = userIdState.value
                    val distanciaFloat = distanciaText.toFloatOrNull()

                    if (distanciaFloat == null || distanciaFloat <= 0) {
                        mostrarMensagem(context.getString(R.string.gp_msg_distance_invalid))
                        return@Button
                    }
                    if (tipoSelecionadoApi.activityIdClimatiq == null && !tipoSelecionadoApi.ehZeroEmissao) {
                        mostrarMensagem(context.getString(R.string.gp_msg_transport_invalid))
                        return@Button
                    }

                    isLoading = true

                    if (idUsuarioLogado == null) {
                        mostrarMensagem(context.getString(R.string.gp_msg_userid_missing), SnackbarDuration.Long)
                        isLoading = false
                        return@Button
                    }

                    scope.launch {
                        var co2eValorFinal = 0.0f
                        var co2eUnidadeFinal = "kg"
                        var operacaoBemSucedida = false

                        try {
                            if (tipoSelecionadoApi.ehZeroEmissao) {
                                co2eValorFinal = 0.0f
                                operacaoBemSucedida = true
                            } else if (tipoSelecionadoApi.activityIdClimatiq != null) {
                                val requestBody = ClimatiqEstimateRequest(
                                    emissionFactor = EmissionFactorSelector(
                                        activityId = tipoSelecionadoApi.activityIdClimatiq!!,
                                        dataVersion = "^25"
                                    ),
                                    parameters = mapOf(
                                        "distance" to distanciaFloat.toDouble(),
                                        "distance_unit" to "km"
                                    )
                                )
                                val response = co2eService.calcularEmissao(requestBody = requestBody)

                                if (response.isSuccessful && response.body() != null) {
                                    val apiData = response.body()!!
                                    co2eValorFinal = apiData.co2e.toFloat()
                                    co2eUnidadeFinal = apiData.co2eUnit
                                    operacaoBemSucedida = true
                                } else {
                                    val errorBody = response.errorBody()?.string() ?: "Erro desconhecido da API"
                                    mostrarMensagem(
                                        context.getString(R.string.gp_msg_transport_api_error, errorBody),
                                        SnackbarDuration.Long
                                    )
                                    operacaoBemSucedida = false
                                }
                            } else {
                                mostrarMensagem(
                                    context.getString(R.string.gp_msg_transport_config_invalid),
                                    SnackbarDuration.Long
                                )
                                operacaoBemSucedida = false
                            }

                            if (operacaoBemSucedida) {
                                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                                val dataAtualFormatada = sdf.format(Date())

                                val novaEmissao = Co2E(
                                    usuarioId = idUsuarioLogado,
                                    valorCo2e = co2eValorFinal,
                                    unidadeCo2e = co2eUnidadeFinal,
                                    dataRegistro = dataAtualFormatada
                                )
                                val idSalvo = co2ERepository.cadastrar(novaEmissao)
                                if (idSalvo > 0) {
                                    mostrarMensagem(
                                        context.getString(
                                            R.string.gp_msg_transport_zero_saved,
                                            co2eValorFinal,
                                            co2eUnidadeFinal
                                        )
                                    )
                                    distanciaText = ""
                                    navController.popBackStack()
                                } else {
                                    // reutilizando msg de DB genérica
                                    mostrarMensagem(context.getString(R.string.gp_msg_energy_db_fail), SnackbarDuration.Long)
                                }
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
                modifier = Modifier.fillMaxWidth(0.6f),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = corBotoes),
                enabled = !isLoading
            ) {
                Text(stringResource(R.string.gp_btn_submit), color = Color.White)
            }

            // Rodapé
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
