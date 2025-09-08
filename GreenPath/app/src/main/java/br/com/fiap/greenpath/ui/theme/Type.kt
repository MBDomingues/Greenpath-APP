package br.com.fiap.greenpath.ui.theme
import br.com.fiap.greenpath.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val MontserratFamily = FontFamily(
    Font(R.font.montserrat_regular, FontWeight.Normal), // Mapeia o arquivo para o peso Normal
    Font(R.font.montserrat_bold, FontWeight.Bold)       // Mapeia o arquivo para o peso Bold
    // Se você adicionou outros pesos/estilos, adicione-os aqui:
    // Font(R.font.montserrat_light, FontWeight.Light),
    // Font(R.font.montserrat_italic, FontWeight.Normal, FontStyle.Italic)
)