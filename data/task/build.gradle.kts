plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	id("org.jetbrains.kotlin.kapt")
	id("com.google.devtools.ksp")
	id("com.google.dagger.hilt.android")
}

android {
	namespace = "kanti.tododer"
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

	implementation(project(":data"))

	testImplementation("junit:junit:4.13.2")
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

	androidTestImplementation("androidx.test.ext:junit:1.1.5")

	implementation("com.google.dagger:hilt-android:2.48.1")
	kapt("com.google.dagger:hilt-android-compiler:2.48.1")
}