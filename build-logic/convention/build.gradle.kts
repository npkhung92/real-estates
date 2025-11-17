plugins {
    `kotlin-dsl` version libs.versions.kotlinDsl
}

group = "com.hung.real_estates.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(libs.androidx.room.gradle.plugin)
    compileOnly(libs.gradle)
    compileOnly(libs.kotlin.gradle.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "com.hung.real_estates.convention.application.compose"
            implementationClass = "com.hung.real_estates.convention.AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "com.hung.real_estates.convention.android.library"
            implementationClass = "com.hung.real_estates.convention.AndroidLibraryConventionPlugin"
        }
        register("hiltLibrary") {
            id = "com.hung.real_estates.convention.hilt.library"
            implementationClass = "com.hung.real_estates.convention.AndroidHiltConventionPlugin"
        }
        register("composeLibrary") {
            id = "com.hung.real_estates.convention.compose.library"
            implementationClass = "com.hung.real_estates.convention.AndroidComposeLibraryConventionPlugin"
        }
        register("roomLibrary") {
            id = "com.hung.real_estates.convention.room.library"
            implementationClass = "com.hung.real_estates.convention.AndroidRoomConventionPlugin"
        }
    }
}
