plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
	id("com.google.dagger.hilt.android")
}

android {
	namespace = "kanti.tododer.data.model.settings"
	compileSdk = 34

	defaultConfig {
		minSdk = 24

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
}

dependencies {

	implementation(project(":data:settings:api"))

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")
	implementation("androidx.datastore:datastore-preferences:1.0.0")

	implementation("com.google.dagger:hilt-android:2.48.1")
	kapt("com.google.dagger:hilt-android-compiler:2.48.1")

	androidTestImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
	androidTestImplementation("androidx.test:runner:1.5.2")
	androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}