import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version = "2.3.3"

plugins {
    kotlin("jvm") version "1.8.21"
    kotlin("plugin.serialization") version "1.8.21"
    `java-library`
    `maven-publish`
    application
}

group = "org.webapi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.json:json:20230227")
    testImplementation("javazoom:jlayer:1.0.1")
    testImplementation("com.github.kenglxn.QRGen:javase:3.0.1")

    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.whiterasbk"
            artifactId = "ktbiliapi"
            version = "0.1"
            from(components["kotlin"])
        }
    }
}

application {
    mainClass.set("MainKt")
}