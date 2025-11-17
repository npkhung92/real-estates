package com.hung.real_estates.convention

import androidx.room.gradle.RoomExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("androidx.room")
                apply("com.google.devtools.ksp")
            }
            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                add("ksp", libs.findLibrary("room-compiler").get())
                add("implementation", libs.findLibrary("room-runtime").get())
                add("implementation", libs.findLibrary("room-kotlin-extensions").get())
            }
        }
    }
}
