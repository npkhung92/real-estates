plugins {
    alias(libs.plugins.realestates.android.library)
    alias(libs.plugins.realestates.hilt.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.hung.core.network"
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)

    implementation(libs.okhttp.logging)
    implementation(libs.okhttp)

    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
}