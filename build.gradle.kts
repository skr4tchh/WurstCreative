plugins {
    id("java")
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

group = "wurstmc.creative"
version = "1.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    compileOnly("io.papermc.paper:paper-api:26.2.build.+")

    compileOnly("org.rocksdb:rocksdbjni:9.11.2")
}

bukkit {
    main = "fun.wurstmc.creative.WurstCreative"
    version = project.version.toString()
    authors = listOf("Skr4tch (TG: skr4tch)")
    libraries = listOf("org.rocksdb:rocksdbjni:9.11.2")
    apiVersion = "26.2"
    foliaSupported = true
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}