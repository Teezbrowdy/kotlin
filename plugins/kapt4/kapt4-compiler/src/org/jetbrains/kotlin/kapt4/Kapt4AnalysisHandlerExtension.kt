/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.kapt4

import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.util.Context
import org.jetbrains.kotlin.analysis.api.KtAnalysisApiInternals
import org.jetbrains.kotlin.analysis.api.lifetime.KtAlwaysAccessibleLifetimeTokenFactory
import org.jetbrains.kotlin.analysis.api.session.KtAnalysisSessionProvider
import org.jetbrains.kotlin.analysis.api.standalone.buildStandaloneAnalysisAPISession
import org.jetbrains.kotlin.analysis.project.structure.KtSourceModule
import org.jetbrains.kotlin.asJava.classes.KtLightClass
import org.jetbrains.kotlin.asJava.findFacadeClass
import org.jetbrains.kotlin.asJava.toLightClass
import org.jetbrains.kotlin.base.kapt3.*
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.jvm.config.JavaSourceRoot
import org.jetbrains.kotlin.cli.jvm.config.JvmClasspathRoot
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.fir.extensions.FirAnalysisHandlerExtension
import org.jetbrains.kotlin.fir.extensions.K2AnalysisResult
import org.jetbrains.kotlin.kapt3.KAPT_OPTIONS
import org.jetbrains.kotlin.kapt3.base.*
import org.jetbrains.kotlin.kapt3.base.util.KaptLogger
import org.jetbrains.kotlin.kapt3.base.util.WriterBackedKaptLogger
import org.jetbrains.kotlin.kapt3.util.MessageCollectorBackedKaptLogger
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.util.ServiceLoaderLite
import java.io.File
import java.net.URLClassLoader
import javax.annotation.processing.Processor

class Kapt4AnalysisHandlerExtension : FirAnalysisHandlerExtension() {

    @OptIn(KtAnalysisApiInternals::class)
    override fun doAnalysis(configuration: CompilerConfiguration): K2AnalysisResult {
        val module: KtSourceModule

        val analysisSession = buildStandaloneAnalysisAPISession {
            buildKtModuleProviderByCompilerConfiguration(configuration) {
                module = it
            }
        }
        val ktAnalysisSession = KtAnalysisSessionProvider.getInstance(analysisSession.project)
            .getAnalysisSessionByUseSiteKtModule(module, KtAlwaysAccessibleLifetimeTokenFactory)
        val ktFiles = module.ktFiles

        val lightClasses = mutableListOf<Pair<KtLightClass, KtFile>>().apply {
            ktFiles.flatMapTo(this) { file ->
                file.children.filterIsInstance<KtClassOrObject>().mapNotNull {
                    it.toLightClass()?.let { it to file }
                }
            }
            ktFiles.mapNotNullTo(this) { ktFile -> ktFile.findFacadeClass()?.let { it to ktFile } }
        }.toMap()

        try {
            val contentRoots = configuration[CLIConfigurationKeys.CONTENT_ROOTS] ?: emptyList()

            val options = (configuration[KAPT_OPTIONS] ?: KaptOptions.Builder()).apply {
                projectBaseDir = ktAnalysisSession.useSiteModule.project.basePath?.let(::File)
                compileClasspath.addAll(contentRoots.filterIsInstance<JvmClasspathRoot>().map { it.file })
                javaSourceRoots.addAll(contentRoots.filterIsInstance<JavaSourceRoot>().map { it.file })
                classesOutputDir = classesOutputDir ?: configuration.get(JVMConfigurationKeys.OUTPUT_DIRECTORY)
            }.build()

            val logger: KaptLogger = MessageCollectorBackedKaptLogger(
                options.flags[KaptFlag.VERBOSE],
                options.flags[KaptFlag.INFO_AS_WARNINGS],
                configuration.get(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY)!!
            )

            if (options.mode == AptMode.WITH_COMPILATION) {
                logger.error("KAPT \"compile\" mode is not supported in Kotlin 2.x. Run kapt with -Kapt-mode=stubsAndApt and use kotlinc for the final compilation step.")
                return K2AnalysisResult.CompilationError
            }

//            if (!optionsBuilder.checkOptions(project, logger, configuration)) {
//                return K2AnalysisResult.InternalError(IllegalArgumentException("co"))
//            }

            val context = Kapt4ContextForStubGeneration(
                options,
                withJdk = false,
                WriterBackedKaptLogger(isVerbose = false),
                lightClasses.keys.toList(),
                lightClasses,
                ktAnalysisSession.ktMetadataCalculator
            )

            val generator = with(context) { Kapt4StubGenerator(ktAnalysisSession) }
            val processorLoader = object : ProcessorLoader(options, logger) {
                override fun doLoadProcessors(classpath: LinkedHashSet<File>, classLoader: ClassLoader): List<Processor> =
                    when (classLoader) {
                        is URLClassLoader -> ServiceLoaderLite.loadImplementations(Processor::class.java, classLoader)
                        else -> super.doLoadProcessors(classpath, classLoader)
                    }
            }
            val processors = processorLoader.loadProcessors(findClassLoaderWithJavac())
            if (options.mode != AptMode.APT_ONLY) {
                val stubs = generator.generateStubs()
                saveStubs(
                    context,
                    stubs.values.filterNotNull().toList(),
                    options.stubsOutputDir
                )
            }
            if (options.mode == AptMode.STUBS_ONLY) return K2AnalysisResult.DoNotGenerateCode
            var sourcesToProcess = options.collectJavaSourceFiles(context.sourcesToReprocess)
            var processedSources = emptySet<File>()
            while (true) {
                context.doAnnotationProcessing(
                    sourcesToProcess,
                    processors.processors,
//                    binaryTypesToReprocess = collectAggregatedTypes(context.sourcesToReprocess)
                )
                val generatedSources = options.sourcesOutputDir.walkTopDown().filter { it.isFile && it.name.endsWith(".java", true) }.toSet()
                if (generatedSources.size == processedSources.size) {
                    return K2AnalysisResult.RetryWithAdditionalRoots(listOf(options.sourcesOutputDir), listOf())
                }
                sourcesToProcess = (generatedSources - processedSources).toList()
                processedSources = generatedSources
            }
        } catch (e: Exception) {
            return K2AnalysisResult.InternalError(e)
        }
    }

    private fun findClassLoaderWithJavac(): ClassLoader {
        // Class.getClassLoader() may return null if the class is defined in a bootstrap class loader
        return Context::class.java.classLoader ?: ClassLoader.getSystemClassLoader()
    }

    fun saveStubs(
        kaptContext: Kapt4ContextForStubGeneration,
        stubs: List<Kapt4StubGenerator.KaptStub>,
        stubsOutputDir: File,
    ): List<File> = buildList {
        for (kaptStub in stubs) {
            val stub = kaptStub.file
            val className = (stub.defs.first { it is JCTree.JCClassDecl } as JCTree.JCClassDecl).simpleName.toString()

            val packageName = stub.getPackageNameJava9Aware()?.toString() ?: ""
            val packageDir = if (packageName.isEmpty()) stubsOutputDir else File(stubsOutputDir, packageName.replace('.', '/'))
            packageDir.mkdirs()

            val sourceFile = File(packageDir, "$className.java")
            sourceFile.writeText(stub.prettyPrint(kaptContext.context))

            add(sourceFile)

            kaptStub.writeMetadataIfNeeded(forSource = sourceFile)?.let { add(it) }
        }
    }
}

class KaptCompilerPluginRegistrar : CompilerPluginRegistrar() {
    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        Companion.registerExtensions(this)
    }

    override val supportsK2: Boolean
        get() = true


    companion object {
        fun registerExtensions(extensionStorage: ExtensionStorage) = with(extensionStorage) {
            FirAnalysisHandlerExtension.registerExtension(Kapt4AnalysisHandlerExtension())
        }
    }
}