package br.com.fiap.greenpath.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.fiap.greenpath.screens.*
import br.com.fiap.greenpath.ui.feature_cadastro.TelaCadastro
import br.com.fiap.greenpath.ui.feature_cadastro_energia.TelaCadastroEnergia
import br.com.fiap.greenpath.ui.feature_cadastro_transporte.TelaCadastroTransporte
import br.com.fiap.greenpath.ui.feature_empresas_parceiras.TelaEmpresasParceiras
import br.com.fiap.greenpath.ui.feature_home.TelaHome
import br.com.fiap.greenpath.ui.feature_pegada_carbono.TelaPegadaCarbono
import br.com.fiap.greenpath.ui.feature_tela_inicial.TelaInicial

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
        composable(Routes.INICIAL) { TelaInicial(navController = nav) }
        composable(Routes.LOGIN)   { Login(navController = nav) }
        composable(Routes.CADASTRO){ TelaCadastro(navController = nav) }

        composable(Routes.HOME)    { TelaHome(nav) }
        composable(Routes.PEGADA)  { TelaPegadaCarbono(nav) }
        composable(Routes.EMPRESAS){ TelaEmpresasParceiras(nav) }

        composable(Routes.CADASTRO_TRANSPORTE) { TelaCadastroTransporte(nav) }
        composable(Routes.CADASTRO_ENERGIA)    { TelaCadastroEnergia(nav) } // stub abaixo
    }
}
