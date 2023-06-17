package com.madrakrystian.juneed.templates

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.lang.Language
import com.intellij.lang.java.JavaLanguage
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtilCore.getLanguageAtOffset
import com.madrakrystian.juneed.ResourceBundle
import com.siyeh.ig.psiutils.TestUtils.isPartOfJUnitTestMethod
import com.siyeh.ig.psiutils.TestUtils.isJUnitTestMethod
import org.jetbrains.kotlin.asJava.toLightMethods
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtNamedFunction

/**
 * Provides context for assertions and fluent assertions in live templates based on language.
 */
abstract class TestContextType(id: String, presentableName: String, private val language: Language) : TemplateContextType(id, presentableName) {

    final override fun isInContext(templateActionContext: TemplateActionContext) = templateActionContext.run {
        if (isOfLanguage(language)) {
            startOffsetElement()
                    ?.takeUnless { it is PsiWhiteSpace }
                    ?.let { isPartOfTest(it) } ?: false
        } else false
    }

    /**
     * Should check if given {@see PsiElement} is part of JUnit test.
     */
    abstract fun isPartOfTest(element: PsiElement): Boolean

    private fun TemplateActionContext.isOfLanguage(language: Language) = getLanguageAtOffset(file, startOffset).isKindOf(language)
    private fun TemplateActionContext.startOffsetElement() = file.findElementAt(startOffset)
}

class JavaTestContextType : TestContextType("JAVA_TEST", ResourceBundle.message("template.context.name.java"), JavaLanguage.INSTANCE) {
    override fun isPartOfTest(element: PsiElement) = isPartOfJUnitTestMethod(element)
}

class KotlinTestContextType : TestContextType("KOTLIN_TEST", ResourceBundle.message("template.context.name.kotlin"), KotlinLanguage.INSTANCE) {
    override fun isPartOfTest(element: PsiElement): Boolean {
        return PsiTreeUtil.getParentOfType(element, KtNamedFunction::class.java)
                ?.takeUnless { it.isTopLevel }
                ?.toLightMethods()
                ?.firstOrNull()?.let { isJUnitTestMethod(it) } ?: false
    }
}