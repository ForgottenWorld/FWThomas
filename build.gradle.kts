import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

repositories {
    jcenter()
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.4-R0.1-SNAPSHOT")
    compileOnly("com.github.BrunoSilvaFreire:Skedule:0.1.3")
    implementation("org.koin:koin-core:2.2.2")
    compileOnly("com.github.ForgottenWorld:FWKotlinDeps:3d8e6200db")
}

group = "it.forgottenworld"
version = "0.0.4-SNAPSHOT"
description = "FWThomas"
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks.withType<ShadowJar> {
    dependencies {
        exclude {
            !setOf("it.forgottenworld", "org.koin").contains(it.moduleGroup)
        }
    }
    relocate("org.koin", "it.forgottenworld.thomas.koin")
}


tasks.withType<JavaCompile> {
    sourceCompatibility = "1.8"
    targetCompatibility = "1.8"
    options.encoding = "UTF-8"
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}