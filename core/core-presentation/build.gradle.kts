plugins {
    alias(libs.plugins.realestates.android.library)
}

android {
    namespace = "com.hung.core.presentation"
}

dependencies {
    implementation(projects.core.coreDomain)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
}