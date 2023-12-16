plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	id("com.google.devtools.ksp")
	id("org.jetbrains.kotlin.kapt")
	id("com.google.dagger.hilt.android")
}

android {
	namespace = "kanti.tododer.room"
	compileSdk = 34

	defaultConfig {
		minSdk = 24

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
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
}

dependencies {

	implementation(project(":core"))
	implementation(project(":room:plan"))
	implementation(project(":room:todo"))

	implementation("androidx.room:room-runtime:2.6.1")
	implementation("androidx.room:room-ktx:2.6.1")
	ksp("androidx.room:room-compiler:2.6.1")

	implementation("com.google.dagger:hilt-android:2.48.1")
	kapt("com.google.dagger:hilt-android-compiler:2.48.1")
}