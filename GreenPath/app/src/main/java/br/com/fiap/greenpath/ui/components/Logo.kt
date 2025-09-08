package br.com.fiap.greenpath.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.greenpath.R
import br.com.fiap.greenpath.ui.theme.MontserratFamily
import androidx.compose.ui.graphics.Color

@Composable
fun Logo(
    modifier: Modifier = Modifier,
    texto: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.gp_cd_logo),
                modifier = Modifier.size(250.dp)
            )
            Box(modifier = Modifier.padding(40.dp)) {
                Text(
                    text = texto,
                    fontFamily = MontserratFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 29.sp,
                    color = Color(0xFF333333),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
