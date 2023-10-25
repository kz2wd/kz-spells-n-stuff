import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    `maven-publish`
    kotlin("jvm") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("org.hibernate.orm") version "6.1.1.Final"
}

group = "com.cludivers"
description = "kz2wdPrison"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    implementation("commons-io:commons-io:2.11.0")
    implementation("org.hibernate:hibernate-core:6.1.1.Final")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("com.h2database:h2:2.2.224")

    testImplementation("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation(kotlin("test"))
    testImplementation("org.apache.logging.log4j:log4j-core:2.20.0")

}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.withType<ShadowJar> {
    dependencies {
        // Kotlin
        include(dependency("org.jetbrains.kotlin:kotlin-stdlib:1.6.21"))
        include(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.21"))
        include(dependency("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.21"))
        include(dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.1"))

        // Hibernates and persistence
        include(dependency("mysql:mysql-connector-java:8.0.33"))
        include(dependency("com.h2database:h2:2.2.224"))
        include(dependency("org.hibernate.orm:hibernate-core:6.1.1.Final"))
        include(dependency("jakarta.persistence:jakarta.persistence-api:3.0.0"))
        include(dependency("jakarta.transaction:jakarta.transaction-api:2.0.0"))
        include(dependency("org.jboss.logging:jboss-logging:3.4.3.Final"))
        include(dependency("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1"))
        include(dependency("com.fasterxml:classmate:1.5.1"))
        include(dependency("net.bytebuddy:byte-buddy:1.12.9"))
        include(dependency("org.hibernate.common:hibernate-commons-annotations:6.0.2.Final"))
        include(dependency("org.antlr:antlr4-runtime:4.10"))

        archiveFileName.set("kzSpells.jar")
    }
}

tasks.test {
    useJUnitPlatform()
}

