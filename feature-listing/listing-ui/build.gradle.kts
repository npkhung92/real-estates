plugins {
    alias(libs.plugins.realestates.android.library)
    alias(libs.plugins.realestates.compose.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.hung.feature_listing.ui"
}

dependencies {
    implementation(projects.core.corePresentation)
    implementation(projects.core.coreUi)
    implementation(projects.featureListing.listingPresentation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.navigation.compose)

    testImplementation(libs.junit)
}