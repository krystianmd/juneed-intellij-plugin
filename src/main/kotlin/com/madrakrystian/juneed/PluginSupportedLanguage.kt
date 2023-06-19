package com.madrakrystian.juneed

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import org.jetbrains.kotlin.idea.KotlinLanguage

enum class PluginSupportedLanguage(val value: Language, val templateContext: PluginTemplateContext) {
    JAVA(JavaLanguage.INSTANCE, PluginTemplateContext("JAVA_TEST", ResourceBundle.message("template.context.name.java"))),
    KOTLIN(KotlinLanguage.INSTANCE, PluginTemplateContext("KOTLIN_TEST", ResourceBundle.message("template.context.name.kotlin")));
}

data class PluginTemplateContext(val id: String, val presentableName: String)