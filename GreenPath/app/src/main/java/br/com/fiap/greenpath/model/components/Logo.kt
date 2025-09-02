package br.com.fiap.greenpath.model.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.greenpath.R
import br.com.fiap.greenpath.ui.theme.GreenPathTheme
import br.com.fiap.greenpath.ui.theme.MontserratFamily

@Composable
fun TelaComLogoNoTopoCentralizada(
    modifier: Modifier = Modifier, // Boa prática: fornecer valor padrão
    texto: String
) {

    Box(
        modifier = modifier // Aplicar o modifier recebido PRIMEIRO
            .fillMaxWidth()
            .fillMaxHeight(0.5f), // O Box define o tamanho geral

        contentAlignment = Alignment.Center // Centraliza a Column interna no Box
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Centraliza os itens da Column horizontalmente
            verticalArrangement = Arrangement.Center // Centraliza os itens da Column verticalmente (opcional, pode ser Top)
        ) {
            // Sua Logo
            Image(
                painter = painterResource(id = R.drawable.logo), // Certifique-se que 'logo' existe em res/drawable
                contentDescription = "Logo da Empresa",
                modifier = Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(16.dp)) // Espaço entre a logo e o texto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(40.dp)
            ) {
                Text(
                    text = texto,
                    fontFamily = MontserratFamily,
                    fontWeight = FontWeight.Bold,// Aplicando a fonte Montserrat
                    fontSize = 29.sp,              // Definindo o tamanho da fonte
                    color = Color(0xFF333333),     // Definindo a cor (0xFF para alfa, seguido por RRGGBB)
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTelaComLogoNoTopoCentralizada() {
    GreenPathTheme{
        TelaComLogoNoTopoCentralizada(
            modifier = Modifier,
            texto = "Sua Jornada começa aqui",
        )
    }

}