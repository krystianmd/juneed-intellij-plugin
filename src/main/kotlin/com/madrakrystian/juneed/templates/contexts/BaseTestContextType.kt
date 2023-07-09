package com.madrakrystian.juneed.templates.contexts

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiUtilCore
import com.madrakrystian.juneed.PluginLanguageConfiguration

/**
 * Provides context for assertions and fluent assertions in live templates based on language.
 */
sealed class BaseTestContextType(
    private val configuration: PluginLanguageConfiguration
) : TemplateContextType(configuration.templateContext.id, configuration.templateContext.presentableName) {

    final override fun isInContext(templateActionContext: TemplateActionContext) = with(templateActionContext) {
        if (PsiUtilCore.getLanguageAtOffset(file, startOffset).isKindOf(configuration.language)) {
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