plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "kanti.tododer.ui.components"
    compileSdk = libs.versions.android.api.target.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.api.minimal.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    val jvm = libs.versions.jvm.target.get()
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(jvm)
        targetCompatibility = JavaVersion.toVersion(jvm)
    }
    kotlinOptions {
        jvmTarget = jvm
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(project(":core"))
    implementation(project(":data:settings:api"))

    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)

    implementation(libs.compose.material3)

    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)
}