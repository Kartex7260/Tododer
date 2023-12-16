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
include(":data")
include(":data:plan")
include(":core")
include(":data:todo")
include(":room")
include(":data:todo:api")
include(":data:plan:api")
