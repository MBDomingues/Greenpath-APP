package br.com.fiap.greenpath.database.model

import com.google.gson.annotations.SerializedName

// Não precisa de @Serializable com Gson
data class ClimatiqEstimateResponse(
    // Use @SerializedName se o nome da propriedade Kotlin for diferente da chave JSON
    @SerializedName("co2e") val co2e: Double,
    @SerializedName("co2e_unit") val co2eUnit: String
    // Adicione outros campos da resposta conforme necessário, usando @SerializedName se preciso
)

// Não precisa de @Serializable
data class ClimatiqEstimateRequest(
    @SerializedName("emission_factor") val emissionFactor: EmissionFactorSelector,
    val parameters: Map<String, Any> // Gson geralmente lida bem com Map<String, Any>
)

// Não precisa de @Serializable
data class EmissionFactorSelector(
    @SerializedName("activity_id") // Gson
    val activityId: String,

    @SerializedName("source") // Gson
    val source: String? = null,

    @SerializedName("region") // Gson
    val region: String? = null,

    @SerializedName("year") // Gson
    val year: Int? = null,

    @SerializedName("lca_activity") // Gson
    val lcaActivity: String? = null,

    // ADICIONE ESTA LINHA:
    @SerializedName("data_version") // Gson
    val dataVersion: String
)


//Opções de transporte
data class OpcaoTransporteApi(
    val nomeExibicao: String,
    val activityIdClimatiq: String?,
    val ehZeroEmissao: Boolean = false
)

val listaOpcoesTransporteApi = listOf(
    OpcaoTransporteApi(nomeExibicao = "A Pé", activityIdClimatiq = null, ehZeroEmissao = true),
    OpcaoTransporteApi(nomeExibicao = "Bicicleta", activityIdClimatiq = null, ehZeroEmissao = true),
    OpcaoTransporteApi(
        nomeExibicao = "Carro (Gasolina)",
        // VERIFIQUE E USE O ID CORRETO DA DOCUMENTAÇÃO DA CLIMATIQ!
        activityIdClimatiq = "passenger_vehicle-vehicle_type_car-fuel_source_gasoline-engine_size_na-vehicle_age_na-vehicle_weight_na"
    ),
    OpcaoTransporteApi(
        nomeExibicao = "Carro Elétrico",
        activityIdClimatiq = "passenger_vehicle-vehicle_type_car-fuel_source_electricity-engine_size_na-vehicle_age_na-vehicle_weight_na"
    ),
    OpcaoTransporteApi(
        nomeExibicao = "Motocicleta",
        activityIdClimatiq = "passenger_vehicle-vehicle_type_motorcycle-fuel_source_gasoline-engine_size_na-vehicle_age_na-vehicle_weight_na"
    ),
    OpcaoTransporteApi(
        nomeExibicao = "Ônibus Urbano",
        activityIdClimatiq = "passenger_bus-vehicle_type_bus_urban-fuel_source_diesel-engine_size_na-vehicle_age_na-vehicle_weight_na"
    )
)