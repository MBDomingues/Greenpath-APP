package br.com.fiap.greenpath.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import br.com.fiap.greenpath.navigation.Routes
import br.com.fiap.greenpath.ui.theme.MontserratFamily
import br.com.fiap.greenpath.ui.theme.corBotoes
import br.com.fiap.greenpath.ui.theme.corFinal
import br.com.fiap.greenpath.ui.theme.corVerdeTopo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroEnergia(navController: NavController) {
    val bg = Brush.verticalGradient(listOf(corVerdeTopo, corFinal))
    val shape = RoundedCornerShape(16.dp)

    // Estado: consumo (texto + float)
    var consumoText by remember { mutableStateOf("") }
    var consumoKwh by remember { mutableStateOf(0f) }

    // Estado: tipo de gás (select)
    var gasExpanded by remember { mutableStateOf(false) }
    val opcoesGas = listOf("Encanado", "Botijão")
    var tipoGas by remember { mutableStateOf(opcoesGas[0]) }

    // Estado: quantidade (texto + float)
    var quantidadeText by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp),
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
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.voltar),
                    contentDescription = "Voltar",
                    modifier = Modifier.size(30.dp),
                    tint = Color.Unspecified
                )
            }
            IconButton(onClick = { navController.navigate(Routes.HOME) }) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = R.drawable.home),
                    contentDescription = "Home",
                    modifier = Modifier.size(30.dp),
                    tint = Color.Unspecified
                )
            }
        }

        // Título
        Text(
            text = "Informações",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = corBotoes,
            fontFamily = MontserratFamily,
            textAlign = TextAlign.Center
        )

        // Lembrete centralizado
        Text(
            text = "Lembre-se: você só precisa fazer isso uma vez por mês,\nusando os dados da sua conta de luz e gás.",
            style = MaterialTheme.typography.bodySmall,
            fontFamily = MontserratFamily,
            textAlign = TextAlign.Center
        )

        // Tipo de Gás (select: Encanado / Botijão)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tipo de gás",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = MontserratFamily,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = gasExpanded,
                onExpandedChange = { gasExpanded = !gasExpanded },
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                OutlinedTextField(
                    value = tipoGas,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = gasExpanded)
                    },
                    shape = RoundedCornerShape(14.dp),
                    textStyle = MaterialTheme.typography.bodyLarge
                )
                ExposedDropdownMenu(
                    expanded = gasExpanded,
                    onDismissRequest = { gasExpanded = false }
                ) {
                    opcoesGas.forEach { opc ->
                        DropdownMenuItem(
                            text = { Text(opc) },
                            onClick = {
                                tipoGas = opc
                                gasExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // Quantidade (m³)
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Quantidade",
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = MontserratFamily,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = quantidadeText,
                onValueChange = { novo ->
                    val permitido = novo.replace(',', '.')
                    if (permitido.all { it.isDigit() || it == '.' }) {
                        quantidadeText = permitido
                        quantidade = permitido.toFloatOrNull() ?: 0f
                    }
                },
                modifier = Modifier.fillMaxWidth(0.85f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(14.dp),
                textStyle = MaterialTheme.typography.bodyLarge,
                suffix = {
                    Text("m³", fontSize = 18.sp, fontFamily = MontserratFamily)
                }
            )
        }

        // 6) Consumo de Eletricidade (título)
        Text(
            text = "Consumo de Eletricidade",
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = MontserratFamily,
            textAlign = TextAlign.Center
        )

        // 7) Card Consumo (200dp, 0 + kWh dentro, 100sp / 60sp)
        OutlinedTextField(
            value = consumoText,
            onValueChange = { novo ->
                val permitido = novo.replace(',', '.')
                if (permitido.all { it.isDigit() || it == '.' }) {
                    consumoText = permitido
                    consumoKwh = permitido.toFloatOrNull() ?: 0f
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(200.dp),
            singleLine = true,
            placeholder = { Text("0", fontSize = 100.sp) },
            suffix = {
                Text("kWh", fontSize = 60.sp, fontFamily = MontserratFamily)
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

        // 8) Cadastrar (central, 0.6f)
        Button(
            onClick = {
                // consumoKwh (Float), tipoGas (String), quantidade (Float)
            },
            modifier = Modifier.fillMaxWidth(0.6f),
            shape = shape,
            colors = ButtonDefaults.buttonColors(containerColor = corBotoes)
        ) {
            Text("Cadastrar", color = Color.White)
        }

        // 9) Dúvida
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

/* Preview */
@Preview(showBackground = true)
@Composable
private fun PreviewCadastroEnergia() {
    val nav = rememberNavController()
    TelaCadastroEnergia(nav)
}
