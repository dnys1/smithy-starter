import software.amazon.smithy.gradle.tasks.SmithyBuild
import software.amazon.smithy.gradle.tasks.Validate

// This is mostly boilerplate adapted from here:
// https://github.com/awslabs/smithy-kotlin/blob/main/tests/codegen/waiter-tests/build.gradle.kts
//
// This sets up the Gradle plugin which triggers the Smithy build step as well as the Kotlin code generator which
// outputs a kotlin package to the build/ folder. It then adds the output to the source set of this project so you
// can use the generated types in the root project. Finally, it triggers the codegen each time your run the gradle
// `build` task.

plugins {
    kotlin("jvm") version "1.8.0"
    application
    id("software.amazon.smithy") version "0.7.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

val codegen by configurations.creating
dependencies {
    codegen("software.amazon.smithy.kotlin:smithy-kotlin-codegen:0.21.3")
    codegen("software.amazon.smithy:smithy-cli:1.33.0")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("software.amazon.smithy:smithy-model:[1.0, 2.0[")
    api("aws.smithy.kotlin:runtime-core:0.21.3")
    api("aws.smithy.kotlin:smithy-client:0.21.3")
    api("aws.smithy.kotlin:tracing-core:0.21.3")

    testImplementation(kotlin("test"))
}

val generateSdk = tasks.register<SmithyBuild>("generateSdk") {
    group = "codegen"
    classpath = codegen
    inputs.file(projectDir.resolve("smithy-build.json"))
    inputs.files(codegen)
}

tasks["smithyBuildJar"].enabled = false

tasks.named<Validate>("smithyValidate") {
    classpath = codegen
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    dependsOn(generateSdk)
}

kotlin {
    jvmToolchain(17)

    sourceSets.getByName("main") {
        kotlin.srcDir("${project.buildDir}/smithyprojections/smithy-starter/source/kotlin-codegen/src/main/kotlin/generated/model")
    }
}

application {
    mainClass.set("MainKt")
}
