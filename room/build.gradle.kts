plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.kapt)
	alias(libs.plugins.google.ksp)
	alias(libs.plugins.dagger.hilt)
	alias(libs.plugins.room)
}

android {
	namespace = "kanti.tododer.data.room"
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
}

room {
	schemaDirectory("$projectDir/schemas")
}

dependencies {

	implementation(project(":core"))

	implementation(libs.androidx.room)
	ksp(libs.androidx.room.compiler)

	implementation(libs.dagger.hilt)
	kapt(libs.dagger.hilt.compiler)

	androidTestImplementation(libs.androidx.test.runner)
	androidTestImplementation(libs.androidx.test.ext.junit)
	androidTestImplementation(libs.coroutines.test)
}