// Apply necessary plugins for multiplatform development, including Kotlin multiplatform, Android library, Compose, and serialization.
plugins {
    kotlin("multiplatform")
    alias(libs.plugins.android.library) // Alias for Android library plugin
    alias(libs.plugins.jetbrains.compose) // Alias for JetBrains Compose plugin
    alias(libs.plugins.serialization.plugin) // Alias for Kotlin serialization plugin
}

// Define the multiplatform configuration
kotlin {
    // For Android target
    androidTarget()

    // For iOS targets
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    // Define source sets for common code shared between platforms
    sourceSets {
        // Common code shared between platforms
        val commonMain by getting {
            dependencies {
                // Compose dependencies for common code
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                // Coroutines for common code
                implementation(libs.kotlinx.coroutines)

                // Ktor dependencies for common code
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.negotiation)
                implementation(libs.ktor.json)
                implementation(libs.ktor.logging)
                implementation(libs.kotlinx.serialization)
                implementation(libs.ktor.client.encoding)

                // Koin dependencies for common code
                implementation(libs.koin.core)
                implementation(libs.koin.compose)

                // DataStore dependency for common code
                implementation(libs.androidx.datastore.preferences.core)

                // mvvm/viewModels dependencies for common code
                api(libs.mvvm.core)
                api(libs.mvvm.compose)
                api(libs.mvvm.flow)
                api(libs.mvvm.flow.compose)
            }
        }

        // Android-specific source set
        val androidMain by getting {
            dependencies {
                api(libs.activity.compose)
                api(libs.appcompat)
                api(libs.core.ktx)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.koin.androidx.compose)
                implementation(libs.accompanist.systemuicontroller)
                implementation(libs.lottie.compose)
                implementation(libs.coil.compose)
            }
        }

        // iOS-specific source sets
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.ios)
            }
        }
    }
}

// Android-specific configuration
android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "org.debugdesk.app.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

// Dependencies section
dependencies {
    implementation(libs.androidx.core) // AndroidX core dependency
}
