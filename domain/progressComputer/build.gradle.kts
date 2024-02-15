plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
	kotlin("kapt")
	id("com.google.dagger.hilt.android")
}

android {
	namespace = "kanti.tododer.domain.progress.computer"
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

	implementation(project(":core"))
	implementation(project(":data"))
	implementation(project(":data:plan:api"))
	implementation(project(":data:todo:api"))
	implementation(project(":data:progress:api"))
	implementation(project(":domain:getPlanChildren"))

	implementation("com.google.dagger:hilt-android:2.48.1")
	kapt("com.google.dagger:hilt-android-compiler:2.48.1")

	testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
	testImplementation("org.mockito:mockito-core:4.11.0")
	testImplementation("org.mockito:mockito-junit-jupiter:4.11.0")
	testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}

tasks.withType<Test> {
	useJUnitPlatform()
}