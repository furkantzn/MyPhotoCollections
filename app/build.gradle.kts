import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.myphotocollections"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myphotocollections"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    val properties = Properties().apply {
        load(FileInputStream(rootProject.file("local.properties")))
    }
    val apiKey: String = properties.getProperty("apiKey") ?: ""

    // Add the API key as a BuildConfig field
    buildTypes {
        getByName("debug") {
            buildConfigField("String", "API_KEY", "\"$apiKey\"")
        }
        getByName("release") {
            buildConfigField("String", "API_KEY", "\"$apiKey\"")
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.core.splashscreen)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    val composeBom = platform("androidx.compose:compose-bom:2024.09.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Material Design 3
    implementation(libs.androidx.material3)
    implementation(libs.material.icons.extended)
    implementation(libs.androidx.material3.adaptive)
    // Android Studio Preview support
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle)

    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.gson)

    //Image loading library
    implementation(libs.coil)

    //Lottie
    implementation(libs.lottie)

    //Hilt
    implementation(libs.hilt)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation)

}