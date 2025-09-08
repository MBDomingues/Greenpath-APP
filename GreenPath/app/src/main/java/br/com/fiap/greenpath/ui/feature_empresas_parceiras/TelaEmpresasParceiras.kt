package br.com.fiap.greenpath.ui.feature_empresas_parceiras

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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

@Composable
fun TelaEmpresasParceiras(navController: NavController) {
    val bg = Brush.verticalGradient(listOf(corVerdeTopo, corFinal))
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            text = "Lojas Parceiras",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = corBotoes,
            fontFamily = MontserratFamily,
            textAlign = TextAlign.Center
        )

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ParceiroRowCard(
                iconRes = R.drawable.mercadosicon,
                titulo = "Mercado",
                nota = "4,5",
                distancia = "2,5Km",
                shape = shape
            )
            ParceiroRowCard(
                iconRes = R.drawable.viagensicon,
                titulo = "Viagem",
                nota = "4,5",
                distancia = "2,5Km",
                shape = shape
            )
            ParceiroRowCard(
                iconRes = R.drawable.restaurantesicon,
                titulo = "Restaurante",
                nota = "4,5",
                distancia = "2,5Km",
                shape = shape
            )
            ParceiroRowCard(
                iconRes = R.drawable.eletronicosicon,
                titulo = "Eletrônicos",
                nota = "4,5",
                distancia = "2,5Km",
                shape = shape
            )
        }

        // Rodapé
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Dúvida ?", style = MaterialTheme.typography.bodyLarge, fontFamily = MontserratFamily)
        }
    }
}

@Composable
fun ParceiroRowCard(
    iconRes: Int,
    titulo: String,
    nota: String,
    distancia: String,
    shape: RoundedCornerShape,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, shape, clip = false)
            .border(1.dp, corBotoes, shape),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = titulo,
                modifier = Modifier.size(48.dp)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = corBotoes,
                    fontFamily = MontserratFamily
                )
                Text(
                    text = "Nota: $nota",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = MontserratFamily
                )
            }

            Text(
                text = distancia,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = MontserratFamily
            )
        }
    }
}

@Preview(showBackground = true, name = "Empresas Parceiras - 4 rows")
@Composable
private fun PreviewEmpresasParceiras() {
    val nav = rememberNavController()
    TelaEmpresasParceiras(nav)
}
