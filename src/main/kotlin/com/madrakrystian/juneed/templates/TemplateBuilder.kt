package com.madrakrystian.juneed.templates

import com.intellij.codeInsight.template.Template
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.codeInsight.template.impl.ConstantNode
import com.intellij.openapi.project.Project

/**
 * Builder for template text segments and properties.
 */
class TemplateBuilder(
    private val template: Template,
    private val templateText: String
) {
    private var currentIndex: Int = 0
    private var variables = mutableSetOf<String>()

    companion object {
        private const val NAME_VARIABLE: String = "\${NAME}"
        private const val DEFAULT_NAME_VARIABLE_VALUE: String = "test"

        fun template(project: Project, templateText: String): TemplateBuilder =
            TemplateBuilder(TemplateManager.getInstance(project).createTemplate("", ""), templateText)
    }

    /**
     * Common usage for predefined name variable.
     */
    fun nameVariable(): TemplateBuilder = predefinedVariable(NAME_VARIABLE, DEFAULT_NAME_VARIABLE_VALUE)

    /**
     * Searches for predefined variable in the template's text and adds a text segment for user to fill it when found.
     *
     * @param variable a string representation of predefined variable
     * @param defaultValue a default value placed in variable text segment
     * @return builder reference
     * @throws IllegalStateException when predefined variable is not found
     */
    fun predefinedVariable(variable: String, defaultValue: String = ""): TemplateBuilder {
        val variableIndex = findVariableIndex(variable)
        template.addTextSegment(templateText.substring(currentIndex, variableIndex))

        if (variables.contains(variable)) {
            template.addVariableSegment(variable.cleanVariableSyntax())
        } else {
            variables.add(variable)

            val expression = ConstantNode(defaultValue)
            template.addVariable(variable.cleanVariableSyntax(), expression, true)
        }
        currentIndex = variableIndex + variable.length
        return this
    }

    private fun findVariableIndex(variable: String): Int {
        val variableIndex = templateText.indexOf(variable, currentIndex)
        if (variableIndex == -1) {
            throw IllegalStateException("Variable=${variable} does not exist in the template")
        }
        return variableIndex
    }

    private fun String.cleanVariableSyntax() = this.replace(Regex("[{}]"), "")

    /**
     * Returns formatted result of builder steps.
     */
    fun build(): Template {
        template.addTextSegment(templateText.substring(currentIndex))
        template.setToIndent(true)
        template.isToReformat = true
        template.isToShortenLongNames = true
        return template
    }
}