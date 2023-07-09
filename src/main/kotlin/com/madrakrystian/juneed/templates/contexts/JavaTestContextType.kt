package com.madrakrystian.juneed.templates.contexts

import com.intellij.psi.PsiElement
import com.madrakrystian.juneed.PluginLanguageConfiguration
import com.siyeh.ig.psiutils.TestUtils

internal class JavaTestContextType : BaseTestContextType(PluginLanguageConfiguration.JAVA) {
    override fun isPartOfTest(element: PsiElement): Boolean = TestUtils.isPartOfJUnitTestMethod(element)
}