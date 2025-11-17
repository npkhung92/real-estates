plugins {
    alias(libs.plugins.realestates.android.library)
}

android {
    namespace = "com.hung.core.unit_test"
}

dependencies {
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.junit)
    implementation(libs.mockk)
}