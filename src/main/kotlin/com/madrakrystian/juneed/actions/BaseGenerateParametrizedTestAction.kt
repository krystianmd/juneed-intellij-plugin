package com.madrakrystian.juneed.actions

import com.intellij.codeInsight.CodeInsightActionHandler
import com.intellij.codeInsight.actions.CodeInsightAction
import com.intellij.codeInsight.template.Template
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiFile
import com.madrakrystian.juneed.PluginLanguageConfiguration
import com.siyeh.ig.psiutils.TestUtils

sealed class BaseGenerateParametrizedTestAction(
    private val sourceType: ParametrizedTestSourceType,
    private val configuration: PluginLanguageConfiguration
) : CodeInsightAction(), CodeInsightActionHandler {

    /**
     * Points to action invoking handler.
     */
    final override fun getHandler() = this

    /**
     * Updates presentation view of the action in the popup elements list.
     */
    final override fun update(presentation: Presentation, project: Project, editor: Editor, file: PsiFile) {
        presentation.text = sourceType.presentationText
        presentation.isEnabledAndVisible = isValidForFile(project, editor, file)
    }

    /**
     * Validates if presentation of the action should be enabled.
     */
    final override fun isValidForFile(project: Project, editor: Editor, file: PsiFile): Boolean {
        return isFileSupported(file) && TestUtils.isInTestSourceContent(getTargetClass(editor, file))
    }

    /**
     * Should check if file is supported by plugin and if it's valid for performing the action.
     */
    abstract fun isFileSupported(file: PsiFile): Boolean

    /**
     * Should extract target class from file.
     */
    abstract fun getTargetClass(editor: Editor, file: PsiFile): PsiClass?

    protected fun generateTemplate(project: Project): Template =
        sourceType.template(project, configuration)
}
