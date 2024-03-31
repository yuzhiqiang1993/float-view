plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.vanniktechPublish) apply false
}

/**
 * 检查依赖库是否有新版本
 * ./gradlew dependencyUpdates
 */

allprojects {
    configurations.all {
        //依赖解析处理
        resolutionStrategy {
            cacheChangingModulesFor(0, TimeUnit.SECONDS)
            //依赖替换
            dependencySubstitution {

                //替换kotlin为指定版本
//                substitute(module("org.jetbrains.kotlin:kotlin-stdlib")).using(module("org.jetbrains.kotlin:kotlin-stdlib-jdk8"))
            }
        }
    }

}
