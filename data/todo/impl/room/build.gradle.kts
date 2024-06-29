plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.kapt)
	alias(libs.plugins.dagger.hilt)
}

android {
	namespace = "kanti.tododer.data.model.todo.datasource.local"
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

dependencies {

	implementation(project(":core"))
	implementation(project(":data"))
	implementation(project(":data:todo:api"))
	implementation(project(":room"))

	implementation(libs.dagger.hilt)
	kapt(libs.dagger.hilt.compiler)

	implementation(kotlin("reflect"))
	implementation(files("../../../../lib/kanti.sl-ktx.jar"))

	testImplementation(libs.junit.jupiter)
	testImplementation(libs.coroutines.test)
}

tasks.withType<Test> {
	useJUnitPlatform()
}