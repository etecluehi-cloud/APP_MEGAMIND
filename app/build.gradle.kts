plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GROQ_API_KEY", "\"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\"")
    }

    buildFeatures {
        buildConfig = true
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


    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    //biblio lembrete worker
    implementation("androidx.work:work-runtime:2.8.1")
    implementation("com.google.guava:guava:31.1-android")

    // Firestore (banco de dados)
    implementation("com.google.firebase:firebase-firestore:25.1.1")

    // Auth (login do usuário)
    implementation("com.google.firebase:firebase-auth:23.2.0")

    // Firebase AI Logic (Gemini - chatbot)
    implementation("com.google.firebase:firebase-ai:16.0.0")

    // Material Design (botões, FAB)
    implementation("com.google.android.material:material:1.12.0")

    // RecyclerView (lista de notas)
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    implementation("com.google.firebase:firebase-functions:20.4.0")

    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")

    implementation("androidx.work:work-runtime:2.8.1")

    // Permite selecionar uma foto da galeria
    implementation("com.github.yalantis:ucrop:2.2.8")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("com.google.code.gson:gson:2.10.1")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}

