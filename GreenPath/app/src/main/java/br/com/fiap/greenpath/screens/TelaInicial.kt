package br.com.fiap.greenpath.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.greenpath.R
import br.com.fiap.greenpath.components.Logo
import br.com.fiap.greenpath.ui.theme.MontserratFamily
import br.com.fiap.greenpath.ui.theme.corBotoes
import br.com.fiap.greenpath.ui.theme.corFinal
import br.com.fiap.greenpath.ui.theme.corVerdeTopo

@Composable
fun TelaInicial(modifier: Modifier = Modifier) {
    val brushDegradeVertical = Brush.verticalGradient(
        colors = listOf(corVerdeTopo, corFinal)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = brushDegradeVertical)
    ){
        Column() {
            Logo(
                texto = "Sua jornada verde começa aqui"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Acompanhe suas emissões de CO₂ e transforme \n" +
                            "hábitos em ações sustentáveis.",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    lineHeight = 24.sp,
                    fontFamily = MontserratFamily
                )

                Spacer(modifier = Modifier.height(24.dp))

                Image(
                    painter = painterResource(id = R.drawable.globoelanicial),
                    contentDescription = "Globo Terrestre",
                    modifier = Modifier.size(180.dp)
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botão "Já tenho conta"
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = corBotoes
                        )
                    ) {
                        Text("Já tenho conta")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {},
                        modifier = Modifier
                            .weight(1f),
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



@Preview(showBackground = true)
@Composable
fun PreviewTelaComLogoNoTopoCentralizada() {
        TelaInicial()
}