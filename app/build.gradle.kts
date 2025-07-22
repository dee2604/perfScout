plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
    id("signing")
}

android {
    namespace = "com.deelib.perfScout"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
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

// Publishing configuration for Maven Central
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "io.github.deekshasinghal326"
                artifactId = "perfscout"
                version = "1.1.6"

                pom {
                    name.set("PerfScout")
                    description.set("Unified Android Performance & Usage Metrics Library - provides safe, modular, and permissionless access to all vital app and device performance metrics through a single, unified API surface.")
                    url.set("https://github.com/deekshasinghal326/perfScout")
                    
                    inceptionYear.set("2024")

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                            distribution.set("repo")
                        }
                    }

                    developers {
                        developer {
                            id.set("deekshasinghal326")
                            name.set("Deeksha Agrawal")
                            email.set("deekshasinghal3@gmail.com")
                            url.set("https://github.com/deekshasinghal326")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://github.com/deekshasinghal326/perfScout.git")
                        developerConnection.set("scm:git:ssh://github.com/deekshasinghal326/perfScout.git")
                        url.set("https://github.com/deekshasinghal326/perfScout")
                        tag.set("v1.1.6")
                    }
                    
                    issueManagement {
                        system.set("GitHub")
                        url.set("https://github.com/deekshasinghal326/perfScout/issues")
                    }
                    
                    ciManagement {
                        system.set("GitHub Actions")
                        url.set("https://github.com/deekshasinghal326/perfScout/actions")
                    }
                }
            }
        }
        
        repositories {
            maven {
                name = "OSSRH"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = project.findProperty("ossrhUsername") as String? ?: ""
                    password = project.findProperty("ossrhPassword") as String? ?: ""
                }
            }
        }
    }
    
    // Signing configuration for Maven Central
    signing {
        val signingKeyId = project.findProperty("signing.keyId") as String?
        val signingPassword = project.findProperty("signing.password") as String?
        val signingKey = project.findProperty("signing.key") as String?

        if (!signingKeyId.isNullOrBlank() && !signingKey.isNullOrBlank() && !signingPassword.isNullOrBlank()) {
            useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
            sign(publishing.publications)
        }
    }
}