package com.madrakrystian.juneed.templates

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
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
sealed class TemplateTestContextType(
    private val configuration: PluginLanguageConfiguration
) : TemplateContextType(configuration.templateContext.id, configuration.templateContext.presentableName) {

    final override fun isInContext(templateActionContext: TemplateActionContext) = with(templateActionContext) {
        if (getLanguageAtOffset(file, startOffset).isKindOf(configuration.language)) {
            file.findElementAt(startOffset)
                ?.takeUnless { it is PsiWhiteSpace }
                ?.let { isPartOfTest(it) } ?: false
        } else false
    }

    /**
     * Should check if given element is part of JUnit test.
     */
    abstract fun isPartOfTest(element: PsiElement): Boolean
}

private class JavaTestContextType : TemplateTestContextType(PluginLanguageConfiguration.JAVA) {
    override fun isPartOfTest(element: PsiElement): Boolean = isPartOfJUnitTestMethod(element)
}

private class KotlinTestContextType : TemplateTestContextType(PluginLanguageConfiguration.KOTLIN) {
    override fun isPartOfTest(element: PsiElement): Boolean =
        element.getStrictParentOfType<KtNamedFunction>()
            ?.takeUnless { it.isTopLevel }
            ?.toLightMethods()
            ?.firstOrNull()
            ?.let { isJUnitTestMethod(it) } ?: false
}