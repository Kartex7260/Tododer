plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.kapt)
	alias(libs.plugins.dagger.hilt)
	alias(libs.plugins.kotlin.compose)
}

android {
	namespace = "kanti.tododer.feat.todo"
	compileSdk = libs.versions.android.api.target.get().toInt()

	defaultConfig {
		minSdk = libs.versions.android.api.minimal.get().toInt()

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

	val composeBom = platform(libs.compose.bom)
	implementation(composeBom)

	implementation(libs.compose.material3)
	implementation(libs.compose.navigation)
	implementation(libs.compose.hilt)
	implementation(libs.compose.lifecycle)
	implementation(libs.compose.ui.tooling.preview)
	debugImplementation(libs.compose.ui.tooling)

	implementation(libs.dagger.hilt)
	kapt(libs.dagger.hilt.compiler)

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