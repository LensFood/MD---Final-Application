plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.lensfood1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.lensfood1"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        renderscriptTargetApi = 21
        renderscriptSupportModeEnabled = true

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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        mlModelBinding = true
    }
}

dependencies {
    implementation ("com.github.Dimezis:BlurView:version-2.0.3")
    implementation ("com.github.lzyzsd:circleprogress:1.2.1")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.25")
    implementation ("com.google.android.material:material:1.3.0-alpha03")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.makeramen:roundedimageview:2.3.0")
    implementation("androidx.camera:camera-camera2")
    implementation("androidx.camera:camera-lifecycle")
    implementation("androidx.camera:camera-view")
    implementation ("com.google.firebase:firebase-auth:21.0.1")
    implementation ("com.google.android.gms:play-services-auth:19.2.0")
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)



    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.github.yalantis:ucrop:2.2.8")
    implementation("org.tensorflow:tensorflow-lite-task-vision:0.4.2")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.4.4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation ("io.coil-kt:coil:2.0.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")


    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.ai.client.generativeai:generativeai:0.1.2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.camera.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}