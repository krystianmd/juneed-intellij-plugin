package com.madrakrystian.juneed

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import org.jetbrains.kotlin.idea.KotlinLanguage

/** Must not be changed for the plugin to work correctly. */
private const val JAVA_TEMPLATE_CONTEXT_ID = "JAVA_TEST"
private const val KOTLIN_TEMPLATE_CONTEXT_ID = "KOTLIN_TEST"

enum class PluginLanguageConfiguration(val language: Language, val templateContext: TemplateContext) {
    JAVA(JavaLanguage.INSTANCE,
            TemplateContext(id = JAVA_TEMPLATE_CONTEXT_ID, presentableName = ResourceBundle.message("template.context.name.java"))),
    KOTLIN(KotlinLanguage.INSTANCE,
            TemplateContext(id = KOTLIN_TEMPLATE_CONTEXT_ID, presentableName = ResourceBundle.message("template.context.name.kotlin")));

    data class TemplateContext(val id: String, val presentableName: String)
}
