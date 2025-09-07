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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import br.com.fiap.greenpath.navigation.Routes
import br.com.fiap.greenpath.ui.theme.MontserratFamily
import br.com.fiap.greenpath.ui.theme.corBotoes
import br.com.fiap.greenpath.ui.theme.corFinal
import br.com.fiap.greenpath.ui.theme.corVerdeTopo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaCadastroTransporte(navController: NavController) {
    val bg = Brush.verticalGradient(listOf(corVerdeTopo, corFinal))
    val shape = RoundedCornerShape(16.dp)

    // Select
    var expanded by remember { mutableStateOf(false) }
    val opcoes = listOf("Bicicleta", "Carro", "A pé")
    var tipoSelecionado by remember { mutableStateOf(opcoes[0]) }

    // Distância
    var distanciaText by remember { mutableStateOf("") }
    var distanciaKm by remember { mutableStateOf(0f) }

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
                    painter = painterResource(id = R.drawable.voltar),
                    contentDescription = "Voltar",
                    modifier = Modifier.size(30.dp),
                    tint = Color.Unspecified
                )
            }
            IconButton(onClick = { navController.navigate(Routes.HOME) }) {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home",
                    modifier = Modifier.size(30.dp),
                    tint = Color.Unspecified
                )
            }
        }

        // Título centralizado
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
                    value = tipoSelecionado,
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
                    opcoes.forEach { opc ->
                        DropdownMenuItem(
                            text = { Text(opc) },
                            onClick = {
                                tipoSelecionado = opc
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        // Distância
        Column(
            modifier = Modifier
                .fillMaxWidth(),

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
                    if (permitido.all { it.isDigit() || it == '.' }) {
                        distanciaText = permitido
                        distanciaKm = permitido.toFloatOrNull() ?: 0f
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(200.dp), // mantém sua caixa alta
                singleLine = true,
                placeholder = { Text("0", fontSize = 100.sp) },
                // "Km" DENTRO da caixa e colado ao valor
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
                // Valor grande e CENTRALIZADO
                textStyle = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 100.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 100.sp
                )
            )
        }

        // Botão menor
        Button(
            onClick = {
                // Aqui você tem:
                // tipoSelecionado (String) e distanciaKm (Float)
            },
            modifier = Modifier
                .fillMaxWidth(0.6f),
            shape = shape,
            colors = ButtonDefaults.buttonColors(containerColor = corBotoes)
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

@Preview(showBackground = true)
@Composable
private fun PreviewCadastroTransporte() {
    val nav = rememberNavController()
    TelaCadastroTransporte(nav)
}
