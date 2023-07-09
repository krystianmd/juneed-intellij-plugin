package com.madrakrystian.juneed.templates.context

import com.intellij.psi.PsiElement
import com.madrakrystian.juneed.PluginLanguageConfiguration
import com.siyeh.ig.psiutils.TestUtils
import org.jetbrains.kotlin.asJava.toLightMethods
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.psiUtil.getStrictParentOfType

internal class KotlinTestContextType : TemplateTestContextType(PluginLanguageConfiguration.KOTLIN) {
    override fun isPartOfTest(element: PsiElement): Boolean = element.getStrictParentOfType<KtNamedFunction>()
        ?.takeUnless { it.isTopLevel }
        ?.toLightMethods()
        ?.firstOrNull()
        ?.let { TestUtils.isJUnitTestMethod(it) } ?: false
}