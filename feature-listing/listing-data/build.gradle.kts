plugins {
    alias(libs.plugins.realestates.android.library)
}

android {
    namespace = "com.hung.feature_listing.data"
}

dependencies {
    implementation(projects.featureListing.listingDomain)
    implementation(projects.core.network)
    implementation(projects.core.coreDomain)

    implementation(libs.androidx.core.ktx)
    implementation(libs.paging.runtime)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}