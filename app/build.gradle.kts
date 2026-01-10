plugins {
    alias(libs.plugins.xeonyu.application)
}


android {
    namespace = "com.yzq.floatview_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.yzq.floatview_app"
        versionCode = 1
        versionName = "1.0"

    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
//    implementation(platform(libs.kotlin.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.google.material)
    implementation(libs.xeonyu.binding)
    implementation(libs.xeonyu.logger)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.lottie)
//    implementation(libs.brv)

    implementation(project(":floating-view"))
}