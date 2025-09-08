@file:OptIn(ExperimentalSerializationApi::class, ExperimentalMaterial3Api::class) // Adicionei ExperimentalMaterial3Api aqui

package br.com.fiap.greenpath.ui.feature_cadastro_transporte

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
import androidx.compose.ui.platform.LocalContext      // Importante
import androidx.compose.ui.res.painterResource
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
import br.com.fiap.greenpath.database.model.Co2E               // Sua entidade Room
import br.com.fiap.greenpath.database.model.EmissionFactorSelector
import br.com.fiap.greenpath.database.model.OpcaoTransporteApi // Sua classe de OpcaoTransporte
import br.com.fiap.greenpath.database.model.listaOpcoesTransporteApi // Sua lista de opções
import br.com.fiap.greenpath.database.repository.Co2ERepository
import br.com.fiap.greenpath.navigation.Routes
import br.com.fiap.greenpath.service.Co2eService          // Sua interface de serviço Retrofit
import br.com.fiap.greenpath.service.RetrofitFactory      // Sua factory do Retrofit
import br.com.fiap.greenpath.ui.theme.MontserratFamily
import br.com.fiap.greenpath.ui.theme.corBotoes
import br.com.fiap.greenpath.ui.theme.corFinal
import br.com.fiap.greenpath.ui.theme.corVerdeTopo
import kotlinx.coroutines.launch                            // Importante
import kotlinx.serialization.ExperimentalSerializationApi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun TelaCadastroTransporte(navController: NavController) {
    val bg = Brush.verticalGradient(listOf(corVerdeTopo, corFinal))
    // val shape = RoundedCornerShape(16.dp) // Você usou diretamente no botão

    // Contexto e CoroutineScope
    val context = LocalContext.current
    val scope = rememberCoroutineScope() // Para lançar coroutines a partir da UI

    //instância do UserPreferencesRepository
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val userIdState: State<Long?> = userPreferencesRepository.userIdFlow.collectAsState(initial = null)

    // Instâncias diretas
    val co2eService: Co2eService = remember { RetrofitFactory.co2eService }
    val co2ERepository = Co2ERepository(context)

    // Estados da UI
    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Select
    var expanded by remember { mutableStateOf(false) }
    // Usar a lista de OpcaoTransporteApi
    val opcoesApi = listaOpcoesTransporteApi // Renomeei para evitar conflito com a 'opcoes' antiga
    var tipoSelecionadoApi by remember { mutableStateOf(opcoesApi.firstOrNull() ?: OpcaoTransporteApi("Erro", null)) }

    // Distância
    var distanciaText by remember { mutableStateOf("") } // Começa vazio

    // Função helper para mostrar snackbar
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

            Text(
                text = "Informações",
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
                    text = "Tipo de Transporte",
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
                        value = tipoSelecionadoApi.nomeExibicao, // Usar nomeExibicao do objeto
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        shape = RoundedCornerShape(14.dp),
                        textStyle = MaterialTheme.typography.bodyLarge
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        opcoesApi.forEach { opc -> // opc agora é um OpcaoTransporteApi
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
                    text = "Distância",
                    style = MaterialTheme.typography.bodyLarge,
                    fontFamily = MontserratFamily,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = distanciaText,
                    onValueChange = { novo ->
                        val permitido = novo.replace(',', '.')
                        if (permitido.isEmpty() || permitido.matches(Regex("^\\d*\\.?\\d*\$"))) { // Permite números e um único ponto decimal
                            distanciaText = permitido
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(200.dp),
                    singleLine = true,
                    suffix = {
                        Text(
                            "Km",
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

            // Botão cadastrar
            Button(
                onClick = {
                    val idUsuarioLogado: Long? = userIdState.value
                    val distanciaFloat = distanciaText.toFloatOrNull()

                    if (distanciaFloat == null || distanciaFloat <= 0) {
                        mostrarMensagem("Por favor, insira uma distância válida.")
                        return@Button
                    }
                    if (tipoSelecionadoApi.activityIdClimatiq == null && !tipoSelecionadoApi.ehZeroEmissao) {
                        mostrarMensagem("Tipo de transporte inválido ou não configurado para cálculo.")
                        return@Button
                    }

                    isLoading = true // Inicia o carregamento

                    if (idUsuarioLogado == null) {
                        mostrarMensagem("Erro: ID do usuário não encontrado. Faça login novamente.", SnackbarDuration.Long)
                        isLoading = false // Certifique-se de resetar o loading
                        return@Button
                    }

                    scope.launch {
                        var co2eValorFinal: Float = 0.0f
                        var co2eUnidadeFinal: String = "kg" // Default
                        var operacaoBemSucedida = false

                        try {
                            if (tipoSelecionadoApi.ehZeroEmissao) {
                                // Emissão zero, não chama API
                                co2eValorFinal = 0.0f
                                operacaoBemSucedida = true
                            }else if (tipoSelecionadoApi.activityIdClimatiq != null) {
                                // Chama a API Climatiq
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
                                    println("API Sucesso: $co2eValorFinal $co2eUnidadeFinal")
                                } else {
                                    val errorBody = response.errorBody()?.string() ?: "Erro desconhecido da API"
                                    println("API Erro ${response.code()}: $errorBody")
                                    mostrarMensagem("Erro da API: $errorBody", SnackbarDuration.Long)
                                    operacaoBemSucedida = false
                                }
                            } else {
                                mostrarMensagem("Configuração de transporte inválida.", SnackbarDuration.Long)
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
                                    mostrarMensagem("Transporte registrado com ${String.format("%.2f", co2eValorFinal)} $co2eUnidadeFinal CO2e!")
                                    distanciaText = ""
                                    navController.popBackStack()
                                } else {
                                    mostrarMensagem("Falha ao salvar no banco de dados.", SnackbarDuration.Long)
                                }
                            }
                        } catch (e: Exception) {
                            println("Exceção geral: ${e.message}")
                            e.printStackTrace()
                            mostrarMensagem("Erro: ${e.localizedMessage ?: "Ocorreu um problema"}", SnackbarDuration.Long)
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.6f),
                shape = RoundedCornerShape(16.dp), // Usando o shape
                colors = ButtonDefaults.buttonColors(containerColor = corBotoes),
                enabled = !isLoading
            ) {
                Text("Cadastrar", color = Color.White)
            }

            // Rodapé
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
private fun PreviewCadastroTransporte() {
    val nav = rememberNavController()
    TelaCadastroTransporte(nav)
}
