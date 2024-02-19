plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	id("com.google.devtools.ksp")
	kotlin("kapt")
	id("com.google.dagger.hilt.android")

	id("androidx.room") version "2.6.1" apply false
}

android {
	namespace = "kanti.tododer.data.room"
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

ksp {
	arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {

	implementation(project(":core"))

	implementation("androidx.room:room-ktx:2.6.1")
	ksp("androidx.room:room-compiler:2.6.1")

	implementation("com.google.dagger:hilt-android:2.48.1")
	kapt("com.google.dagger:hilt-android-compiler:2.48.1")

	androidTestImplementation("androidx.test:runner:1.5.2")
	androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
	androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}