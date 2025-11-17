pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "RealEstates"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:core-domain")
include(":core:core-presentation")
include(":core:core-ui")
include(":core:network")
include(":core:unit-test")
include(":feature-listing:listing-domain")
include(":feature-listing:listing-presentation")
include(":feature-listing:listing-data")
include(":feature-listing:listing-ui")
