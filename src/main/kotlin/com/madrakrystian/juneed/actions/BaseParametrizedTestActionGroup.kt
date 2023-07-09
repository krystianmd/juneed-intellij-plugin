package com.madrakrystian.juneed.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * Custom action group for parameterized method actions.
 * <p>
 * For some reason {@link com.intellij.openapi.actionSystem.DefaultActionGroup} cannot show popup in GenerateGroup
 * simply via xml attribute, thus one of the solutions is to include the actions in that strange dynamic-static way
 */
sealed class BaseParametrizedTestActionGroup(private val generateParametrizedTestActions: Array<BaseGenerateParametrizedTestAction>) : ActionGroup() {
    final override fun getChildren(e: AnActionEvent?): Array<AnAction> = arrayOf(*generateParametrizedTestActions)
}