package com.madrakrystian.juneed.templates.context

import com.intellij.psi.PsiElement
import com.madrakrystian.juneed.PluginLanguageConfiguration
import com.siyeh.ig.psiutils.TestUtils

internal class JavaTestContextType : TemplateTestContextType(PluginLanguageConfiguration.JAVA) {
    override fun isPartOfTest(element: PsiElement): Boolean = TestUtils.isPartOfJUnitTestMethod(element)
}