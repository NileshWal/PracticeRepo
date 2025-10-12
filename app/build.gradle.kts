plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.com.google.dagger)
}

android {
    namespace = "com.nilesh.practiceapps"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.nilesh.practiceapps"
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
        debug {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.ktx)

    //For recyclerview swipe refresh
    implementation(libs.androidx.swiperefreshlayout)

    // Retrofit2 for Networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.conscrypt.android)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.gson)

    //Coroutine Lifecycle Scope
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Android Room Database Library
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit.jupiter)
    // To use Kotlin annotation processing tool (kapt)
    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Glide - non Compose
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    //HILT
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    /*annotationProcessor(libs.hilt.compiler)*/

    //Jetpack Compose
    implementation(libs.androidx.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.constraintlayout.compose)

    //Compose Nag Host
    implementation(libs.androidx.navigation.compose)

    //Runtime LiveData
    implementation(libs.androidx.runtime.livedata)

    //Accompanist (Permission) for Jetpack Compose
    implementation(libs.accompanist.permissions)

    //Jetpack Compose HILT
    implementation(libs.androidx.hilt.navigation.compose)

    //Glide - Compose
    /*implementation("com.github.bumptech.glide:compose:1.0.0-alpha.1")*/

    //Google Services & Maps
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)
    // Optionally, you can include the Compose utils library for Clustering, etc.
    implementation(libs.maps.compose.utils)
    implementation(libs.play.services.location)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // required if you want to use Mockito for unit tests
    testImplementation(libs.mockito.core)
    // required if you want to use Mockito for Android tests
    androidTestImplementation(libs.mockito.android)

    //Truth library
    testImplementation(libs.truth)
}

tasks.withType<Test> {
    useJUnitPlatform()
}