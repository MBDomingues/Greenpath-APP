package br.com.fiap.greenpath.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.greenpath.screens.*

@Composable
fun AppNav(
    modifier: Modifier = Modifier,
    startDestination: String = Routes.INICIAL
) {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = startDestination
    ) {
        composable(Routes.INICIAL) { TelaInicial(navController = nav) /* sem nav aqui; botões só navegam para login/cadastro */ }
        composable(Routes.LOGIN)   { Login(navController = nav) }
        composable(Routes.CADASTRO){ TelaCadastro() }

        composable(Routes.HOME)    { TelaHome(nav) }
        composable(Routes.PEGADA)  { TelaPegadaCarbono(nav) }
        composable(Routes.EMPRESAS){ TelaEmpresasParceiras(nav) }

        composable(Routes.CADASTRO_TRANSPORTE) { TelaCadastroTransporte(nav) }
        composable(Routes.CADASTRO_ENERGIA)    { TelaCadastroEnergia(nav) } // stub abaixo
    }
}
