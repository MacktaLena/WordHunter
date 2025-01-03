plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.wordhunter"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.wordhunter"
        minSdk = 25
        targetSdk = 34
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    implementation("io.ktor:ktor-client-core:2.3.4")          // Core-Funktionen
    implementation("io.ktor:ktor-client-android:2.3.4")       // HTTP-Engine (für Android)

    // Ktor Serialization mit Kotlinx
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4") // Content-Negotiation für JSON
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4") // Kotlinx-Serialization-JSON

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // Lifecycle Runtime (für lifecyclebewusste Coroutine-Scopes)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation(libs.androidx.lifecycle.viewmodel.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}