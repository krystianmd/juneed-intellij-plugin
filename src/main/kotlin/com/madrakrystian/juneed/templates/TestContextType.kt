package com.madrakrystian.juneed.templates

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiUtilCore.getLanguageAtOffset
import com.madrakrystian.juneed.PluginLanguageConfiguration
import com.siyeh.ig.psiutils.TestUtils.isPartOfJUnitTestMethod
import com.siyeh.ig.psiutils.TestUtils.isJUnitTestMethod
import org.jetbrains.kotlin.asJava.toLightMethods
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.getStrictParentOfType

/**
 * Provides context for assertions and fluent assertions in live templates based on language.
 */
sealed class TestContextType(private val configuration: PluginLanguageConfiguration) : TemplateContextType(configuration.templateContext.id, configuration.templateContext.presentableName) {

    final override fun isInContext(templateActionContext: TemplateActionContext) = templateActionContext.run {
        if (isOfLanguage(configuration.language)) {
            startOffsetElement()?.let { isPartOfTest(it) } ?: false
        } else false
    }

    private fun TemplateActionContext.isOfLanguage(language: Language) = getLanguageAtOffset(file, startOffset).isKindOf(language)
    private fun TemplateActionContext.startOffsetElement() = file.findElementAt(startOffset)
            .takeUnless { it is PsiWhiteSpace }

    /**
     * Should check if given {@see PsiElement} is part of JUnit test.
     */
    abstract fun isPartOfTest(element: PsiElement): Boolean
}

private class JavaTestContextType : TestContextType(PluginLanguageConfiguration.JAVA) {
    override fun isPartOfTest(element: PsiElement): Boolean = isPartOfJUnitTestMethod(element)
}

private class KotlinTestContextType : TestContextType(PluginLanguageConfiguration.KOTLIN) {
    override fun isPartOfTest(element: PsiElement): Boolean =
            element.getStrictParentOfType<KtNamedFunction>()
                    ?.takeUnless { it.isTopLevel }
                    ?.toLightMethods()
                    ?.firstOrNull()
                    ?.let { isJUnitTestMethod(it) } ?: false
}