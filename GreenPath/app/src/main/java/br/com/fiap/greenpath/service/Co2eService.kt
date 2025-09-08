package br.com.fiap.greenpath.service

import br.com.fiap.greenpath.database.model.ClimatiqEstimateRequest
import br.com.fiap.greenpath.database.model.ClimatiqEstimateResponse
import kotlinx.serialization.descriptors.StructureKind
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface Co2eService {

    /**
     * Endpoint para estimar emissões de CO2e.
     * Documentação: https://docs.climatiq.io/api-reference/estimate
     *
     * @param apiKey O token de autorização "Bearer SUA_CHAVE_API".
     * @param requestBody O corpo da requisição contendo o fator de emissão e os parâmetros.
     * @return Uma Response contendo o ClimatiqEstimateResponse ou um erro.
     */
    @POST("v1/estimate")
    suspend fun calcularEmissao(
        @Header("Authorization") apiKey: String = "Bearer 2DEHT0TBYS2NFBQGYNSWC9EYCG",

        @Body requestBody: ClimatiqEstimateRequest

    ): Response<ClimatiqEstimateResponse>

}