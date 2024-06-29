import java.io.FileInputStream
import java.util.Calendar
import java.util.Properties

plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)

	alias(libs.plugins.kotlin.kapt)
	alias(libs.plugins.dagger.hilt)

	alias(libs.plugins.kotlin.compose)
}

android {
	signingConfigs {
		val keyStoreProperties = "Tododer.properties"
		if (!project.hasProperty(keyStoreProperties)) {
			System.err.println("Project no has property=$keyStoreProperties")
			return@signingConfigs
		}
		val keyStorePropFile = File(project.property(keyStoreProperties) as String)
		if (!keyStorePropFile.exists()) {
			System.err.println("Not find file from prop=$keyStoreProperties, " +
					"file=${project.property(keyStoreProperties)}")
			return@signingConfigs
		}

		create("release") {
			val props = Properties()
			props.load(FileInputStream(keyStorePropFile))

			storeFile = file(props["RELEASE_STORE_FILE"] as String)
			storePassword = props["RELEASE_STORE_PASSWORD"] as String
			keyAlias = props["RELEASE_KEY_ALIAS"] as String
			keyPassword = props["RELEASE_KEY_PASSWORD"] as String
		}
    }

	namespace = "kanti.tododer"
	compileSdk = libs.versions.android.api.target.get().toInt()

	defaultConfig {
		applicationId = "kanti.tododer"
		minSdk = libs.versions.android.api.minimal.get().toInt()
		targetSdk = libs.versions.android.api.target.get().toInt()
		versionCode = 2

		val calendar = Calendar.getInstance()
		versionName = "${calendar.get(Calendar.YEAR)}" +
				".${calendar.get(Calendar.MONTH) + 1}" +
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
			isShrinkResources = true
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

			signingConfig = signingConfigs.getByName("release")
			matchingFallbacks += listOf("release")
		}
		debug {
			isDebuggable = true

			isMinifyEnabled = false

			applicationIdSuffix = ".debug"
			versionNameSuffix = "-debug"
		}
		create("pre-release") {
			isDebuggable = false

			applicationIdSuffix = ".pre_release"
			versionNameSuffix = "-pre-release"

            signingConfig = signingConfigs.getByName("release")
            matchingFallbacks += listOf("release")
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
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {

	val platform = platform(libs.compose.bom)
	implementation(platform)

	implementation(libs.compose.material3)
	implementation(libs.compose.navigation)

	implementation(libs.compose.ui.tooling.preview)
	debugImplementation(libs.compose.ui.tooling)

	implementation(libs.android.material)

	implementation(libs.dagger.hilt)
	kapt(libs.dagger.hilt.compiler)

	implementation(project(":data:plan:api"))
	implementation(project(":data:todo:api"))

	implementation(project(":core"))
	implementation(project(":data"))
	implementation(project(":feat:todo"))
	implementation(project(":feat:settings"))
	implementation(project(":data:colorStyle:api"))
	implementation(project(":data:colorStyle:impl:room"))
	implementation(project(":data:todo:impl:room"))
	implementation(project(":data:plan:impl:room"))
	implementation(project(":data:progress:impl:room"))
	implementation(project(":data:appData:impl:dataStore"))
	implementation(project(":data:settings:api"))
	implementation(project(":data:settings:impl:dataStore"))

	implementation(project(":ui:common"))

//	implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
}