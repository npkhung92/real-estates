plugins {
    alias(libs.plugins.realestates.android.library)
    alias(libs.plugins.realestates.compose.library)
}

android {
    namespace = "com.hung.core.ui"
}

dependencies {
    implementation(projects.core.corePresentation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.kotlinx.collections.immutable)
}