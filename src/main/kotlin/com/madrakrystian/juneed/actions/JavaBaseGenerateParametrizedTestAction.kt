package com.madrakrystian.juneed.actions

import com.intellij.codeInsight.CodeInsightUtilCore
import com.intellij.codeInsight.generation.GenerateMembersUtil
import com.intellij.codeInsight.generation.PsiGenerationInfo
import com.intellij.codeInsight.hint.HintManager
import com.intellij.codeInsight.template.Template
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiCompiledElement
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiMethod
import com.intellij.refactoring.util.CommonRefactoringUtil
import com.intellij.testIntegration.TestIntegrationUtils
import com.intellij.util.IncorrectOperationException
import com.madrakrystian.juneed.PluginLanguageConfiguration
import org.jetbrains.kotlin.psi.psiUtil.getStrictParentOfType

internal sealed class JavaBaseGenerateParametrizedTestAction(sourceType: ParametrizedTestSourceType) :
    BaseGenerateParametrizedTestAction(sourceType, PluginLanguageConfiguration.JAVA) {

    override fun isFileSupported(file: PsiFile) = file is PsiJavaFile && file !is PsiCompiledElement

    override fun getTargetClass(editor: Editor, file: PsiFile) = file.findElementAt(editor.caretModel.offset)
        ?.getStrictParentOfType<PsiClass>()
        ?.takeUnless { it.isInterface }

    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        if (file.isReadOnly()) {
            return
        }
        val offset = editor.caretModel.offset
        val targetClass = getTargetClass(editor, file)

        targetClass?.let {
            WriteCommandAction.runWriteCommandAction(project) {
                commitProjectDocuments(project)
                createMethodFromTemplate(project, file, offset, editor, targetClass)
            }
        }
    }

    private fun PsiFile.isReadOnly() = !CommonRefactoringUtil.checkReadOnlyStatus(this)

    private fun commitProjectDocuments(project: Project) {
        PsiDocumentManager.getInstance(project).commitAllDocuments()
    }

    private fun createMethodFromTemplate(project: Project, file: PsiFile, offset: Int, editor: Editor, targetClass: PsiClass) {
        createMethod(file, offset)?.let {
            val template = generateTemplate(project)
            runMethodTemplate(editor, targetClass, it, template)
        }
    }

    private fun createMethod(file: PsiFile, atOffset: Int): PsiMethod? {
        val methodGenerationInfo = TestIntegrationUtils.createDummyMethod(file)
            ?.let { PsiGenerationInfo(it) } ?: return null

        GenerateMembersUtil.insertMembersAtOffset(file, atOffset, listOf(methodGenerationInfo))

        return methodGenerationInfo.psiMember
            ?.let { CodeInsightUtilCore.forcePsiPostprocessAndRestoreElement(it) }
    }

    private fun runMethodTemplate(editor: Editor, targetClass: PsiClass, method: PsiMethod, template: Template) {
        try {
            TestIntegrationUtils.runTestMethodTemplate(editor, targetClass, method, false, template)
        } catch (e: IncorrectOperationException) {
            HintManager.getInstance().showErrorHint(editor, "Couldn't generate test method: ${e.message}")
        }
    }
}