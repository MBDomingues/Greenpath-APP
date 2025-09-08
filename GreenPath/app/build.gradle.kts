import org.jetbrains.kotlin.gradle.model.Kapt



plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

android {
    namespace = "br.com.fiap.greenpath"
    compileSdk = 36

    defaultConfig {
        applicationId = "br.com.fiap.greenpath"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.animation.core.lint)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


// Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7") // VERSÃO CORRIGIDA
    // implementation("com.google.accompanist:accompanist-navigation-animation:0.30.1") // Accompanist é largamente depreciado. Use animações do navigation-compose.

    // Room
    implementation("androidx.room:room-runtime:2.6.1") // VERSÃO CORRIGIDA (ESTÁVEL)
    implementation("androidx.room:room-ktx:2.6.1")     // Para suporte a Coroutines/Flow com Room
    kapt("androidx.room:room-compiler:2.6.1")      // VERSÃO CORRIGIDA (ESTÁVEL)

    // Retrofit & Networking
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0") // Já está correto
    implementation("com.squareup.okhttp3:okhttp:4.12.0")             // ADICIONADO (OkHttp) - Verifique a última versão compatível com Retrofit 2.11.0
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // ADICIONADO (Logging) - Mesma versão do OkHttp

    // Kotlin Coroutines (geralmente trazidas transitivamente, mas bom ter explícito se você as usa muito)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0") // Verifique a última versão
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0") // Geralmente vem com coroutines-android

    // Coil (Image Loading for Compose)
    implementation("io.coil-kt:coil-compose:2.6.0")

    implementation("androidx.datastore:datastore-preferences:1.1.1")
}

