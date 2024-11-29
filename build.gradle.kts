val koinVersion: String by project
val slf4jVersion: String by project
val coroutinesVersion: String by project

plugins {
    kotlin("jvm") version "2.0.21"
    id("org.sonarqube") version "6.0.1.5171"
}

allprojects {
    group = "br.com.dillmann.nginxignition"
    version = "1.0.0"

    repositories {
        mavenCentral()
    }

    sonarqube {
        properties {
            property("sonar.organization", "lucasdillmann_nginx-ignition")
            property("sonar.projectKey", "lucasdillmann")
        }
    }
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "org.sonarqube")

    dependencies {
        implementation("io.insert-koin:koin-core-jvm:$koinVersion")
        implementation("org.slf4j:slf4j-api:$slf4jVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$coroutinesVersion")
    }
}

kotlin {
    jvmToolchain(21)
}
