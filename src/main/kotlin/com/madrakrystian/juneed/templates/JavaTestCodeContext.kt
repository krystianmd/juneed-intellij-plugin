package com.madrakrystian.juneed.templates

import com.intellij.codeInsight.template.JavaCodeContextType
import com.intellij.psi.PsiElement
import com.siyeh.ig.psiutils.TestUtils.isInTestCode

/**
 * Provides context for assertions and fluent assertions in live templates for java files.
 */
class JavaTestCodeContext : JavaCodeContextType("JAVA_TEST_CODE", "Test code", Generic::class.java) {

    /**
     * Checks if given {@see PsiElement} is part of JUnit test code.
     */
    override fun isInContext(element: PsiElement) = isInTestCode(element)
}