package com.madrakrystian.juneed

import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import org.jetbrains.kotlin.idea.KotlinLanguage

enum class SupportedLanguage(val templateContextId: String, val templatePresentableName: String, val value: Language) {
    JAVA("JAVA_TEST",
            ResourceBundle.message("template.context.name.java"),
            JavaLanguage.INSTANCE),
    KOTLIN("KOTLIN_TEST",
            ResourceBundle.message("template.context.name.kotlin"),
            KotlinLanguage.INSTANCE);
}