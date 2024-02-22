plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")

	kotlin("kapt")
	id("com.google.dagger.hilt.android")
}

android {
	namespace = "kanti.tododer.feat.todo"
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
	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.7"
	}
}

dependencies {

	implementation("androidx.navigation:navigation-compose:2.7.7")
	implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
	implementation(platform("androidx.compose:compose-bom:2023.10.01"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")

	implementation("com.google.dagger:hilt-android:2.48.1")
	kapt("com.google.dagger:hilt-android-compiler:2.48.1")

	implementation(project(":core"))
	implementation(project(":data"))
	implementation(project(":data:todo:api"))
	implementation(project(":data:plan:api"))
	implementation(project(":data:appData:api"))
	implementation(project(":data:progress:api"))
	implementation(project(":domain:planDeleteBehaviour"))
	implementation(project(":domain:getPlanChildren"))
	implementation(project(":domain:progressComputer"))
	implementation(project(":domain:deleteTodo"))
	implementation(project(":ui:deleter"))
	implementation(project(":ui:todo"))
	implementation(project(":ui:plan"))
	implementation(project(":ui:fillingProgressBar"))
	implementation(project(":ui:common"))
}