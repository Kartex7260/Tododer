pluginManagement {
	repositories {
		google()
		mavenCentral()
		gradlePluginPortal()
	}
}
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		google()
		mavenCentral()
	}
}

rootProject.name = "Tododer"
include(":app")
include(":LifecycleLogger")
include(":FillingProgressView")
include(":data")
include(":data:task")
include(":data:plan")
include(":room")
include(":data:progress")
include(":room:task")
include(":room:plan")
include(":room:progress")
include(":domain")
include(":core")
