plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.deelib.perfScout"
    compileSdk = 35

    defaultConfig {
        minSdk = 25
        targetSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
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
}

dependencies {

    implementation(libs.androidx.annotation.jvm)
    implementation(libs.material)
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}

//Publishing configuration
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "com.github.deekshasinghal326"
                artifactId = "perfScout"
                version = "1.0.0"

                pom {
                    name.set("PerfScout")
                    description.set("Android library providing device and app performance metrics with a clean, modular API")
                    url.set("https://github.com/deekshasinghal326/perfScout")

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }

                    developers {
                        developer {
                            id.set("deekshasinghal326")
                            name.set("Deeksha Singhal")
                            email.set("deekshasinghal3@gmail.com")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://github.com/deekshasinghal326/perfScout.git")
                        developerConnection.set("scm:git:ssh://github.com/deekshasinghal326/perfScout.git")
                        url.set("https://github.com/deekshasinghal326/perfScout")
                    }
                }
            }
        }
    }
}