plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services") // Apply the Google services plugin
}

android {
    namespace = "com.example.firebase"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.firebase"
        minSdk = 23
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Firebase dependencies
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(platform("com.google.firebase:firebase-bom:33.8.0")) // Firebase BOM for dependency version management
    implementation("com.google.firebase:firebase-auth:23.1.0")
    implementation("com.google.firebase:firebase-firestore:24.0.0")
    implementation("com.google.firebase:firebase-analytics")

    // Test dependencies
    testImplementation(libs.junit)  // For unit tests
    androidTestImplementation(libs.ext.junit)  // For Android Instrumentation tests
    androidTestImplementation(libs.espresso.core)  // For UI testing with Espresso
}

