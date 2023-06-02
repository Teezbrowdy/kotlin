import java.net.URI

plugins {
    kotlin("jvm")
    id("jps-compatible")
    kotlin("plugin.serialization")
}

repositories {
    ivy {
        url = URI("https://github.com/webassembly/testsuite/zipball/")
        patternLayout {
            artifact("[revision]")
        }
        metadataSources { artifact() }
        content { includeModule("webassembly", "testsuite") }
    }

    ivy {
        url = URI("https://github.com/webassembly/wabt/releases/download/")
        patternLayout {
            artifact("[revision]/[artifact]-[revision]-[classifier].[ext]")
        }
        metadataSources { artifact() }
        content { includeModule("webassembly", "wabt") }
    }
}

val wabtDir = File(buildDir, "wabt")
val wabtVersion = "1.0.19"
val testSuiteRevision = "18f8340"
val testSuiteDir = File(buildDir, "testsuite")

val gradleOs = org.gradle.internal.os.OperatingSystem.current()
val wabtOS = when {
    gradleOs.isMacOsX -> "macos"
    gradleOs.isWindows -> "windows"
    gradleOs.isLinux -> "ubuntu"
    else -> error("Unsupported OS: $gradleOs")
}

val wabt by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

val testSuite by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

dependencies {
    implementation(kotlinStdlib())
    implementation(kotlinxCollectionsImmutable())
    testImplementation(commonDependency("junit:junit"))
    testCompileOnly(project(":kotlin-test:kotlin-test-jvm"))
    testCompileOnly(project(":kotlin-test:kotlin-test-junit"))
    testImplementation(projectTests(":compiler:tests-common"))
    testImplementation(commonDependency("org.jetbrains.kotlinx", "kotlinx-serialization-json"))

    testSuite("webassembly:testsuite:$testSuiteRevision@zip")
    wabt("webassembly:wabt:$wabtVersion:$wabtOS@tar.gz")

    cachedDependencies("webassembly:wabt:$wabtVersion:windows@tar.gz")
    cachedDependencies("webassembly:wabt:$wabtVersion:ubuntu@tar.gz")
}

val unzipTestSuite by task<Copy> {
    dependsOn(testSuite)

    from(provider {
        zipTree(testSuite.singleFile)
    })

    into(testSuiteDir)
}

val unzipWabt by task<Copy> {
    dependsOn(wabt)

    from(provider {
        tarTree(resources.gzip(wabt.singleFile))
    })

    into(wabtDir)
}

sourceSets {
    "main" { projectDefault() }
    "test" { projectDefault() }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += listOf(
        "-opt-in=kotlin.ExperimentalUnsignedTypes",
        "-Xskip-prerelease-check"
    )
}

projectTest("test", true) {
    dependsOn(unzipWabt)
    dependsOn(unzipTestSuite)
    systemProperty("wabt.bin.path", "$wabtDir/wabt-$wabtVersion/bin")
    systemProperty("wasm.testsuite.path", "$testSuiteDir/WebAssembly-testsuite-$testSuiteRevision")
    workingDir = projectDir
}

testsJar()
