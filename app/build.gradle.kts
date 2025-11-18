plugins {
    alias(libs.plugins.realestates.compose.app)
    alias(libs.plugins.realestates.hilt.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.hung.real_estates"

    defaultConfig {
        applicationId = "com.hung.real_estates"
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(projects.core.coreDomain)
    implementation(projects.core.coreUi)
    implementation(projects.core.network)
    implementation(projects.datasource)
    implementation(projects.realEstates.featureListing.listingData)
    implementation(projects.realEstates.featureListing.listingPresentation)
    implementation(projects.realEstates.featureListing.listingDomain)
    implementation(projects.realEstates.featureListing.listingUi)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.navigation.compose)
}