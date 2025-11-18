plugins {
    alias(libs.plugins.realestates.android.library)
    alias(libs.plugins.realestates.hilt.library)
    alias(libs.plugins.realestates.room.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.hung.real_estates.datasource"
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(projects.core.network)

    implementation(libs.androidx.core.ktx)

    implementation(libs.paging.runtime)
    implementation(libs.room.paging)
    ksp(libs.paging.common)

    implementation(libs.retrofit.core)
    implementation(libs.kotlinx.serialization.json)
}