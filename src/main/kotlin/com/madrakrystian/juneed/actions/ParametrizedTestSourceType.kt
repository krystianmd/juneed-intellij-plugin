package com.madrakrystian.juneed.actions

import com.intellij.codeInsight.template.Template
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.project.Project
import com.madrakrystian.juneed.PluginLanguageConfiguration
import com.madrakrystian.juneed.templates.TemplateBuilder.Companion.template

enum class ParametrizedTestSourceType(val presentationText: String) {
    INTS("Ints Source") {
        override fun template(project: Project, configuration: PluginLanguageConfiguration): Template =
            template(project, getTemplateText(project, configuration, presentationText))
                .predefinedVariable("\${INTS}")
                .nameVariable()
                .build()
    },
    STRINGS("Strings Source") {
        override fun template(project: Project, configuration: PluginLanguageConfiguration): Template =
            template(project, getTemplateText(project, configuration, presentationText))
                .predefinedVariable("\${STRINGS}")
                .nameVariable()
                .build()
    },
    ENUMS("Enums Source") {
        override fun template(project: Project, configuration: PluginLanguageConfiguration): Template =
            template(project, getTemplateText(project, configuration, presentationText))
                .predefinedVariable("\${ENUM}") // in the annotation
                .nameVariable()
                .predefinedVariable("\${ENUM}") // as the parameter type
                .build()
    },
    CSV("CSV Source") {
        override fun template(project: Project, configuration: PluginLanguageConfiguration): Template =
            template(project, getTemplateText(project, configuration, presentationText))
                .predefinedVariable("\${CSV}")
                .nameVariable()
                .predefinedVariable("\${CSV_PARAMS}")
                .build()
    },
    NULL_AND_EMPTY("Null And Empty Source") {
        override fun template(project: Project, configuration: PluginLanguageConfiguration): Template =
            template(project, getTemplateText(project, configuration, presentationText))
                .nameVariable()
                .predefinedVariable("\${PARAM_TYPE}")
                .build()
    },
    METHOD("Method Source") {
        override fun template(project: Project, configuration: PluginLanguageConfiguration): Template =
            template(project, getTemplateText(project, configuration, presentationText))
                .predefinedVariable("\${METHOD}")
                .nameVariable()
                .predefinedVariable("\${METHOD_PARAM}")
                .build()
    };

    abstract fun template(project: Project, configuration: PluginLanguageConfiguration): Template

    companion object {
        private fun getTemplateText(project: Project, configuration: PluginLanguageConfiguration, presentationText: String): String {
            return FileTemplateManager.getInstance(project)
                .getCodeTemplate(templateFileNameFor(configuration, presentationText)).text
        }

        private fun templateFileNameFor(configuration: PluginLanguageConfiguration, presentationText: String) =
            when (configuration) {
                PluginLanguageConfiguration.JAVA -> "Parametrized Test Method $presentationText.java"
                PluginLanguageConfiguration.KOTLIN -> "Parametrized Test Method $presentationText.kt"
            }
    }
}