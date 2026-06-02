plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.midterm22.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.midterm22 .app"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

configurations.all {
    resolutionStrategy {
        force("androidx.core:core:1.15.0")
        force("androidx.core:core-ktx:1.15.0")
        force("androidx.activity:activity:1.9.3")
    }
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)

    // Firebase
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-auth:23.2.0")

    // GridLayout
    implementation("androidx.gridlayout:gridlayout:1.0.0")

    // CardView
    implementation("androidx.cardview:cardview:1.0.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // MPAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    testImplementation(libs.junit)

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
