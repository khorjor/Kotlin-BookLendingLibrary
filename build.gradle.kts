plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.serialization") version "1.9.10"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    // --- Ktor Core ---
    implementation("io.ktor:ktor-server-core:2.3.4")
    implementation("io.ktor:ktor-server-netty:2.3.4")

    // --- JSON Serialization ---
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // --- Logging (optional but useful) ---
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // --- Unit Testing ---
    testImplementation("io.ktor:ktor-server-tests:2.3.4")
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.example.ApplicationKt")
}
