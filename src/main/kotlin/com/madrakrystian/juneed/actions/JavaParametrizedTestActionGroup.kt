package com.madrakrystian.juneed.actions

internal class JavaParametrizedTestActionGroup : BaseParametrizedTestActionGroup(
    arrayOf(
        GenerateParameterizedTestWithIntsSourceAction,
        GenerateParameterizedTestWithStringsSourceAction,
        GenerateParameterizedTestWithEnumsSourceAction,
        GenerateParameterizedTestWithCsvSourceAction,
        GenerateParameterizedTestWithNullAndEmptySourceAction,
        GenerateParameterizedTestWithMethodSourceAction
    )
) {
    private object GenerateParameterizedTestWithIntsSourceAction :
        JavaBaseGenerateParametrizedTestAction(ParametrizedTestSourceType.INTS)

    private object GenerateParameterizedTestWithStringsSourceAction :
        JavaBaseGenerateParametrizedTestAction(ParametrizedTestSourceType.STRINGS)

    private object GenerateParameterizedTestWithEnumsSourceAction :
        JavaBaseGenerateParametrizedTestAction(ParametrizedTestSourceType.ENUMS)

    private object GenerateParameterizedTestWithCsvSourceAction :
        JavaBaseGenerateParametrizedTestAction(ParametrizedTestSourceType.CSV)

    private object GenerateParameterizedTestWithNullAndEmptySourceAction :
        JavaBaseGenerateParametrizedTestAction(ParametrizedTestSourceType.NULL_AND_EMPTY)

    private object GenerateParameterizedTestWithMethodSourceAction :
        JavaBaseGenerateParametrizedTestAction(ParametrizedTestSourceType.METHOD)
}