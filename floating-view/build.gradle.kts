plugins {
    alias(libs.plugins.xeonyu.library)
    alias(libs.plugins.vanniktechPublish)
    alias(libs.plugins.kotlin.android)
}

// Maven Central 发布配置
mavenPublishing {
    // 发布到 Maven Central（自动检测 SNAPSHOT 和正式版本）
    publishToMavenCentral()

    // SNAPSHOT 版本发布到 snapshots 仓库，不需要 GPG 签名
    val versionName = project.findProperty("VERSION_NAME")?.toString() ?: ""
    val isSnapshot = versionName.endsWith("SNAPSHOT", ignoreCase = true)

    if (!isSnapshot) {
        signAllPublications()
    }
}

android {
    namespace = "com.yzq.floating_view"
    
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(platform(libs.kotlin.bom.stable))
    implementation(libs.androidx.core.ktx.stable)
    implementation(libs.androidx.appcompat.stable)
    implementation(libs.androidx.annotation.jvm)
}