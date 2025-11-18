plugins {
    alias(libs.plugins.realestates.android.library)
    alias(libs.plugins.realestates.hilt.library)
}

android {
    namespace = "com.hung.feature_listing.presentation"
}

dependencies {
    implementation(projects.core.coreDomain)
    implementation(projects.core.corePresentation)
    implementation(projects.featureListing.listingDomain)

    implementation(libs.androidx.core.ktx)

    testImplementation(projects.core.unitTest)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine.test)
}