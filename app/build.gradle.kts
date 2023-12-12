import org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")

	id("org.jetbrains.kotlin.kapt")
	id("com.google.devtools.ksp")

	id("com.google.dagger.hilt.android")

	id("androidx.navigation.safeargs.kotlin")
}

tasks.withType<KaptGenerateStubsTask>().configureEach {
	kotlinOptions {
		jvmTarget = "1.8"
	}
}

android {
	namespace = "kanti.tododer"
	compileSdk = 34

	defaultConfig {
		applicationId = "kanti.tododer"
		minSdk = 24
		targetSdk = 34
		versionCode = 1
		versionName = "1.0.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	packaging {
		resources.excludes.addAll(
			arrayOf(
				"META-INF/LICENSE.md",
				"META-INF/LICENSE-notice.md"
			)
		)
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
	buildFeatures {
		viewBinding = true
	}
}

dependencies {

	implementation(project(mapOf("path" to ":LifecycleLogger")))
	implementation(project(mapOf("path" to ":FillingProgressView")))

	implementation(project(":core"))

	val hiltVersion = "2.48.1"
	val roomVersion = "2.6.1"
	val coroutinesVersion = "1.7.3"
	val lifecycleVersion = "2.6.2"
	val navigationVersion = "2.7.5"


//	Tests
	testImplementation("junit:junit:4.13.2")
	testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
	androidTestImplementation("androidx.test.ext:junit-ktx:1.1.5")
	androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
	androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

// 	Ui
	implementation("androidx.legacy:legacy-support-v4:1.0.0")
	implementation("androidx.core:core-ktx:1.12.0")
	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("com.google.android.material:material:1.10.0")
	implementation("androidx.constraintlayout:constraintlayout:2.1.4")
	implementation("androidx.cardview:cardview:1.0.0")
	implementation("androidx.recyclerview:recyclerview:1.3.2")
	implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
	implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
	implementation("androidx.fragment:fragment-ktx:1.6.2")
	implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
	implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
	implementation("androidx.preference:preference-ktx:1.2.1")

	implementation("com.google.android.material:material:1.10.0")

// 	Di
	implementation("com.google.dagger:hilt-android:$hiltVersion")
	kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

// 	Data
	implementation("androidx.room:room-runtime:$roomVersion")
	implementation("androidx.room:room-ktx:$roomVersion")
	ksp("androidx.room:room-compiler:$roomVersion")

	implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.21")
	implementation(files("../libs/kanti.sl-ktx.jar"))

	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
}

tasks.withType<Test> {
	useJUnitPlatform()
}