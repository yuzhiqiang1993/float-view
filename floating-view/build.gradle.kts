plugins {
    alias(libs.plugins.xeonyu.library)
    alias(libs.plugins.vanniktechPublish)
}

android {
    namespace = "com.yzq.floating_view"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.kotlin.bom))
    implementation(libs.androidx.appcompat)
    api(libs.xeonyu.application)
    implementation(libs.xeonyu.coroutine)
    implementation(libs.xeonyu.logger)
}