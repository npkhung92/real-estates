plugins {
    alias(libs.plugins.realestates.android.library)
}

android {
    namespace = "com.hung.feature_listing.domain"
}

dependencies {
    implementation(projects.core.coreDomain)

    implementation(libs.androidx.core.ktx)
}