package br.com.fiap.greenpath.ui.feature_home

import UserPreferencesRepository
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.greenpath.R
import br.com.fiap.greenpath.database.repository.Co2ERepository
import br.com.fiap.greenpath.navigation.Routes
import br.com.fiap.greenpath.ui.theme.MontserratFamily
import br.com.fiap.greenpath.ui.theme.corBotoes
import br.com.fiap.greenpath.ui.theme.corFinal
import br.com.fiap.greenpath.ui.theme.corVerdeTopo

@Composable
fun TelaHome(navController: NavController) {
    val context = LocalContext.current
    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val userIdState: State<Long?> = userPreferencesRepository.userIdFlow.collectAsState(initial = null)
    val idUsuarioLogado: Long? = userIdState.value

    val co2ERepository = Co2ERepository(context)
    val quantidadeCo2 = co2ERepository.buscarSomaGasto(idUsuarioLogado)

    val brushDegradeVertical = Brush.verticalGradient(colors = listOf(corVerdeTopo, corFinal))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = brushDegradeVertical)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = stringResource(R.string.gp_home_title),
                style = MaterialTheme.typography.titleLarge,
                fontFamily = MontserratFamily,
                color = corBotoes,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            EmissionSummaryCardSimple(
                titulo = stringResource(R.string.gp_home_co2_title),
                valor = quantidadeCo2
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.navigate(Routes.PEGADA) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(2.dp, RoundedCornerShape(14.dp), clip = false),
                    shape = RoundedCornerShape(14.dp),
                ) {
                    Text(stringResource(R.string.gp_home_btn_register_emissions))
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.gp_home_partners_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontFamily = MontserratFamily
                    )

                    VerMaisChip(
                        onClick = { navController.navigate(Routes.EMPRESAS) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ParceiroItem(
                        iconRes = R.drawable.restaurantesicon,
                        label = stringResource(R.string.gp_partner_restaurants),
                        onClick = { navController.navigate(Routes.EMPRESAS) }
                    )
                    ParceiroItem(
                        iconRes = R.drawable.mercadosicon,
                        label = stringResource(R.string.gp_partner_markets),
                        onClick = { navController.navigate(Routes.EMPRESAS) }
                    )
                    ParceiroItem(
                        iconRes = R.drawable.viagensicon,
                        label = stringResource(R.string.gp_partner_travel),
                        onClick = { navController.navigate(Routes.EMPRESAS) }
                    )
                }
            }
        }
    }
}

@Composable
fun EmissionSummaryCardSimple(
    titulo: String,
    valor: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                fontFamily = MontserratFamily,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.gp_home_value_with_kg, valor),
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                fontFamily = MontserratFamily
            )
        }
    }
}

@Composable
fun VerMaisChip(onClick: () -> Unit) {
    Surface(
        color = Color.White,
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .height(30.dp)
            .width(80.dp)
    ) {
        TextButton(onClick = onClick) {
            Text(
                stringResource(R.string.gp_home_chip_see_more),
                color = corBotoes,
                fontFamily = MontserratFamily,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun ParceiroItem(
    iconRes: Int,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .widthIn(min = 90.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(10.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = MontserratFamily
        )
    }
}
