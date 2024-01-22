import java.util.Calendar

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")

	kotlin("kapt")
	id("com.google.dagger.hilt.android")
}

android {
	namespace = "kanti.tododer"
	compileSdk = 34

	defaultConfig {
		applicationId = "kanti.tododer"
		minSdk = 24
		targetSdk = 34
		versionCode = 1

		val calendar = Calendar.getInstance()
		versionName = "${calendar.get(Calendar.YEAR)}" +
				".${calendar.get(Calendar.MONTH)}" +
				".${calendar.get(Calendar.DAY_OF_MONTH)}"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	buildTypes {
		release {
			isDebuggable = false
			isMinifyEnabled = true
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
		debug {
			isDebuggable = true
			isMinifyEnabled = false
			applicationIdSuffix = ".debug"
			versionNameSuffix = "-debug"
		}
		create("debug-release") {
			isDebuggable = false
			isMinifyEnabled = true
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
			applicationIdSuffix = ".debug"
			versionNameSuffix = "-debug-release"
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
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {

	implementation("androidx.navigation:navigation-compose:2.7.6")
	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
	implementation("androidx.activity:activity-compose:1.8.2")
	implementation(platform("androidx.compose:compose-bom:2023.10.01"))
	implementation("androidx.compose.ui:ui")
	implementation("androidx.compose.ui:ui-graphics")
	implementation("androidx.compose.ui:ui-tooling-preview")
	implementation("androidx.compose.material3:material3")
	testImplementation("junit:junit:4.13.2")
	androidTestImplementation("androidx.test.ext:junit:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
	androidTestImplementation("androidx.compose.ui:ui-test-junit4")
	debugImplementation("androidx.compose.ui:ui-tooling")
	debugImplementation("androidx.compose.ui:ui-test-manifest")

	implementation("com.google.dagger:hilt-android:2.48.1")
	kapt("com.google.dagger:hilt-android-compiler:2.48.1")

	implementation(files("../../lib/fillingProgressBar-compose.aar"))

	implementation(project(":data:plan:api"))

	implementation(project(":data"))
	implementation(project(":feat:todo"))
	implementation(project(":feat:settings"))
	implementation(project(":domain:dataInitializer"))
	implementation(project(":data:todo:impl:room"))
	implementation(project(":data:plan:impl:room"))
	implementation(project(":data:progress:impl:room"))
	implementation(project(":data:appData:impl:dataStore"))
	implementation(project(":data:settings:api"))
	implementation(project(":data:settings:impl:dataStore"))

//	implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
}