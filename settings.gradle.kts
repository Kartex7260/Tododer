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
include(":core")
include(":room")
include(":data:todo:api")
include(":data:plan:api")
include(":data:todo:impl:room")
include(":data:plan:impl:room")
include(":app:phone")
include(":feat:todo")
include(":ui:todo")
include(":ui:plan")
include(":data:appData")
include(":data:appData:api")
include(":data:appData:impl:dataStore")
include(":domain:planDeleteBehaviour")
include(":domain:dataInitializer")
include(":domain:getPlanChildren")
include(":ui:deleter")
include(":feat:settings")
include(":ui:settings")
include(":data:settings:api")
include(":data:settings:impl:dataStore")
include(":data:progress:api")
include(":data:progress:impl:room")
include(":domain:progressComputer")
