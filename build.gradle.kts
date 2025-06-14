plugins {
    kotlin("jvm") version "2.1.0"

    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.github.alwaysdarkk"
version = "1.0.2"

allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        mavenCentral()

        maven(url = "https://jitpack.io")
        maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven(url = "https://repo.codemc.io/repository/maven-public/")
        maven(url = "https://oss.sonatype.org/content/groups/public/")
        maven(url = "https://repo.aikar.co/content/groups/aikar//")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    kotlin {
        jvmToolchain(11)
    }
}