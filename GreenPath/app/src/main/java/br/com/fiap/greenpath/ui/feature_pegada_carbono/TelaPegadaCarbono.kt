package br.com.fiap.greenpath.ui.feature_pegada_carbono

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.fiap.greenpath.R
import br.com.fiap.greenpath.navigation.Routes
import br.com.fiap.greenpath.ui.theme.MontserratFamily
import br.com.fiap.greenpath.ui.theme.corBotoes
import br.com.fiap.greenpath.ui.theme.corFinal
import br.com.fiap.greenpath.ui.theme.corVerdeTopo

@Composable
fun TelaPegadaCarbono(navController: NavController) {
    val bg = Brush.verticalGradient(listOf(corVerdeTopo, corFinal))
    val cardShape = RoundedCornerShape(18.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.voltar),
                    contentDescription = stringResource(R.string.gp_cd_back),
                    modifier = Modifier.size(28.dp),
                    tint = Color.Unspecified
                )
            }
            IconButton(onClick = { navController.navigate(Routes.HOME) }) {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = stringResource(R.string.gp_cd_home_icon),
                    modifier = Modifier.size(28.dp),
                    tint = Color.Unspecified
                )
            }
        }

        PegadaOptionCard(
            iconRes = R.drawable.carro,
            label = stringResource(R.string.gp_footprint_transport),
            shape = cardShape,
            onClick = { navController.navigate(Routes.CADASTRO_TRANSPORTE) }
        )

        PegadaOptionCard(
            iconRes = R.drawable.casa,
            label = stringResource(R.string.gp_footprint_home_energy),
            shape = cardShape,
            onClick = { navController.navigate(Routes.CADASTRO_ENERGIA) }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.gp_question_short),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = MontserratFamily
            )
        }
    }
}

@Composable
fun PegadaOptionCard(
    iconRes: Int,
    label: String,
    shape: RoundedCornerShape,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, shape, clip = false)
            .border(1.dp, corBotoes, shape)
            .clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(84.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = corBotoes,
                fontFamily = MontserratFamily
            )
        }
    }
}
