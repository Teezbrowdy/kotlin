description = "Annotation Processor for Kotlin K2"

plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    api(project(":compiler:util"))
    api(project(":compiler:cli"))
    api(project(":compiler:backend"))
    api(project(":compiler:frontend"))
    api(project(":compiler:frontend.java"))
    api(project(":compiler:plugin-api"))

    embedded(project(":analysis:analysis-api")) { isTransitive = false }
    embedded(project(":analysis:analysis-api-fir")) { isTransitive = false }
    embedded(project(":analysis:low-level-api-fir")) { isTransitive = false }
    embedded(project(":analysis:analysis-api-standalone")) { isTransitive = false }
    embedded(project(":analysis:analysis-api-standalone:analysis-api-standalone-base")) { isTransitive = false }
    embedded(project(":analysis:analysis-api-impl-base")) { isTransitive = false }
    embedded(project(":analysis:analysis-api-providers")) { isTransitive = false }
    embedded(project(":analysis:project-structure")) { isTransitive = false }
    embedded(project(":analysis:analysis-internal-utils")) { isTransitive = false }
    embedded(project(":analysis:symbol-light-classes")) { isTransitive = false }

//    embedded(project(":kotlin-annotation-processing-cli")) { isTransitive = false }
//    embedded(project(":kotlin-annotation-processing-base")) { isTransitive = false }
//    embedded(project(":kotlin-annotation-processing-runtime")) { isTransitive = false }

    compileOnly(project(":kotlin-annotation-processing"))
    embedded(project(":kotlin-annotation-processing")){ isTransitive = false }

    embedded(project(":kotlin-annotation-processing-base")){ isTransitive = false }
    testImplementation(project(":kotlin-annotation-processing-cli"))
    embedded(project(":kotlin-annotation-processing-runtime")){ isTransitive = false }
    implementation(project(":compiler:backend.jvm.entrypoint"))

    compileOnly(toolsJarApi())
    compileOnly(intellijCore())
    compileOnly(commonDependency("org.jetbrains.intellij.deps:asm-all"))

    testImplementation(intellijCore())
    testRuntimeOnly(intellijResources()) { isTransitive = false }

    testApiJUnit5()
    testApi(projectTests(":compiler:tests-common-new"))
    testApi(projectTests(":compiler:test-infrastructure"))
    testApi(projectTests(":compiler:test-infrastructure-utils"))
    testApi(projectTests(":kotlin-annotation-processing"))
    testApi(projectTests(":kotlin-annotation-processing-cli"))

    testApi(projectTests(":kotlin-annotation-processing-base"))

    testCompileOnly(toolsJarApi())
    testRuntimeOnly(toolsJar())
}

optInToExperimentalCompilerApi()

sourceSets {
    "main" { projectDefault() }
    "test" {
        projectDefault()
        generatedTestDir()
    }
}

testsJar {}

kaptTestTask("test", JavaLanguageVersion.of(8))
//kaptTestTask("testJdk11", JavaLanguageVersion.of(11))

fun Project.kaptTestTask(name: String, javaLanguageVersion: JavaLanguageVersion) {
    val service = extensions.getByType<JavaToolchainService>()

    projectTest(taskName = name, parallel = true) {
        useJUnitPlatform {
            excludeTags = setOf("IgnoreJDK11")
        }
        workingDir = rootDir
        dependsOn(":dist")
        javaLauncher.set(service.launcherFor { languageVersion.set(javaLanguageVersion) })
    }
}

publish()

runtimeJar()
sourcesJar()
javadocJar()



allprojects {
    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
        kotlinOptions {
//            languageVersion = "2.0"
            freeCompilerArgs += "-Xcontext-receivers"

        }
    }
}
