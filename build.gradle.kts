import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

val shadowJarFileName = "kzSpells.jar"

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
    implementation(kotlin("reflect"))
    implementation("io.github.classgraph:classgraph:4.8.179")

    implementation(platform("com.intellectualsites.bom:bom-newest:1.40")) // Ref: https://github.com/IntellectualSites/bom
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit") { isTransitive = false }

    testImplementation("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    testImplementation("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit") { isTransitive = false }
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
        include(dependency("org.jetbrains.kotlin:.*"))
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

        include(dependency("io.github.classgraph:classgraph:4.8.179"))

        archiveFileName.set(shadowJarFileName)
    }
}

tasks.test {
    useJUnitPlatform()
}

fun getFile(propertyName: String, environmentName: String, isMissingCritical: Boolean): String {
    val file: String? = project.findProperty(propertyName) as? String ?: System.getenv(environmentName)
    handleMissingProperty(propertyName, file, isMissingCritical)
    return file ?: ""
}

fun handleMissingProperty(propertyName: String, file: String?, isMissingCritical: Boolean) {
    if (file != null){
        return
    }

    val errorMessage: String = if (isMissingCritical) {
        "Mandatory property $propertyName missing. Use -P$propertyName=<file>"
    } else {
        "Property $propertyName not specified. Use -P$propertyName=<file>"
    }
    if (isMissingCritical){
        throw GradleException(errorMessage)
    } else {
        logger.warn(errorMessage)
    }
}


fun getServerJar(): String {
    return getFile("serverJar", "SERVER_JAR", true)
}

fun getServerDirectory(): String {
    return getFile("serverDir", "SERVER_DIR", true)
}

tasks.register<Copy>("deployPlugin") {
    description = "Custom deployment task"
    dependsOn("shadowJar")

    // Specify the destination directory based on a property or environment variable

    val serverDir = getServerDirectory()

    // Explicitly specify the inputs (JAR file) for the task
    from(project.buildDir.resolve("libs/$shadowJarFileName"))

    into("$serverDir/plugins/")

    doLast {
        println("Jar deployed to: $serverDir/plugins/")
    }
}

tasks.register<JavaExec>("startServer") {
    description = "Start the server"

    val serverDir = getServerDirectory()
    val serverJar = getServerJar()

    val launchCommand = "java -jar $serverDir/$serverJar"

    val process = ProcessBuilder(launchCommand)
        .directory(File(serverDir))
        .start()

    // Optionally provide input to the process
//    if (project.hasProperty("reload")) {
//        process.outputStream.bufferedWriter().use { it.write(reloadCommand) }
//    }

    val exitCode = process.waitFor()
    if (exitCode == 0) {
        println("Server reloaded/relaunched successfully.")
    } else {
        println("Failed to reload/relaunch server. Exit code: $exitCode")
    }

    main = "-jar"
    classpath = files("$serverDir/$serverJar")

}



val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    languageVersion = "1.6"
}