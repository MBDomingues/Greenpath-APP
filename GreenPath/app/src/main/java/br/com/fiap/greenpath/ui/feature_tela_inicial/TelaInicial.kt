package br.com.fiap.greenpath.ui.feature_tela_inicial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.greenpath.navigation.Routes
import br.com.fiap.greenpath.R
import br.com.fiap.greenpath.ui.components.Logo
import br.com.fiap.greenpath.ui.theme.MontserratFamily
import br.com.fiap.greenpath.ui.theme.corBotoes
import br.com.fiap.greenpath.ui.theme.corFinal
import br.com.fiap.greenpath.ui.theme.corVerdeTopo

@Composable
fun TelaInicial(modifier: Modifier = Modifier, navController: NavController) {
    val brushDegradeVertical = Brush.verticalGradient(
        colors = listOf(corVerdeTopo, corFinal)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = brushDegradeVertical)
    ){
        Column {
            Logo(
                texto = stringResource(R.string.gp_onboarding_headline)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.gp_onboarding_subhead),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    lineHeight = 24.sp,
                    fontFamily = MontserratFamily
                )

                Spacer(modifier = Modifier.height(24.dp))

                Image(
                    painter = painterResource(id = R.drawable.globoelanicial),
                    contentDescription = stringResource(R.string.gp_cd_earth_globe),
                    modifier = Modifier.size(180.dp)
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 40.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { navController.navigate(Routes.LOGIN) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = corBotoes)
                    ) {
                        Text(stringResource(R.string.gp_btn_i_have_account))
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { navController.navigate(Routes.CADASTRO) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = corBotoes)
                    ) {
                        Text(stringResource(R.string.gp_btn_sign_up))
                    }
                }
            }
        }
    }
}
