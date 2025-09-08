package br.com.fiap.greenpath.service // Ou seu pacote correto

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Usar 'object' para um Singleton é a prática comum em Kotlin para factories
object RetrofitFactory {

    private const val BASE_URL = "https://api.climatiq.io/data/"

    // 1. Configurar o OkHttpClient (com Logging Interceptor para debug)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Ótimo para ver o que está sendo enviado/recebido
    }

    private val okHttpClient: OkHttpClient by lazy { // Criar o cliente de forma lazy
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Adicionar o interceptor de logging
            // Você pode adicionar outros interceptors aqui se necessário (ex: para headers comuns)
            .build()
    }

    // 2. Configurar a instância do Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // <-- Fornecer o OkHttpClient configurado
            .addConverterFactory(GsonConverterFactory.create()) // Mantendo Gson conforme sua preferência
            // Não há .addCallAdapterFactory() aqui, o que é bom para suspend fun com Response<T>
            .build()
    }

    // 3. Expor sua Co2eService como uma propriedade lazy (Singleton do serviço)
    val co2eService: Co2eService by lazy {
        retrofit.create(Co2eService::class.java)
    }

    /*
    // Se você realmente quisesse uma função que retorna uma nova instância cada vez
    // (geralmente não é o padrão para serviços Retrofit, mas para mostrar a diferença):
    fun createCo2eServiceInstance(): Co2eService {
        return retrofit.create(Co2eService::class.java)
    }
    */
}
