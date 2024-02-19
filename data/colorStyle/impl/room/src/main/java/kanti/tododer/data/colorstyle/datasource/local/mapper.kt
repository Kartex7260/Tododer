package kanti.tododer.data.colorstyle.datasource.local

import kanti.tododer.data.colorstyle.ColorStyle
import kanti.tododer.data.colorstyle.ColorStyleData
import kanti.tododer.data.colorstyle.ColorStyleType
import kanti.tododer.data.room.colorstyle.ColorStyleDataEntity
import kanti.tododer.data.room.colorstyle.ColorStyleEntity

fun ColorStyleDataEntity.toColorStyleData(): ColorStyleData = ColorStyleData(
    id = id,
    name = name,
    type = ColorStyleType.valueOf(type)
)

fun ColorStyleEntity.toColorStyle(): ColorStyle = ColorStyle(
    id = id,
    name = name,
    type = ColorStyleType.valueOf(type),

    primaryLight = primaryLight,
    onPrimaryLight = onPrimaryLight,
    primaryContainerLight = primaryContainerLight,
    onPrimaryContainerLight = onPrimaryContainerLight,

    secondaryLight = secondaryLight,
    onSecondaryLight = onSecondaryLight,
    secondaryContainerLight = secondaryContainerLight,
    onSecondaryContainerLight = onSecondaryContainerLight,

    tertiaryLight = tertiaryLight,
    onTertiaryLight = onTertiaryLight,
    tertiaryContainerLight = tertiaryContainerLight,
    onTertiaryContainerLight = onTertiaryContainerLight,

    errorLight = errorLight,
    onErrorLight = onErrorLight,
    errorContainerLight = errorContainerLight,
    onErrorContainerLight = onErrorContainerLight,

    backgroundLight = backgroundLight,
    onBackgroundLight = onBackgroundLight,
    surfaceLight = surfaceLight,
    onSurfaceLight = onSurfaceLight,
    surfaceVariantLight = surfaceVariantLight,
    onSurfaceVariantLight = onSurfaceVariantLight,

    outlineLight = outlineLight,


    primaryDark = primaryDark,
    onPrimaryDark = onPrimaryDark,
    primaryContainerDark = primaryContainerDark,
    onPrimaryContainerDark = onPrimaryContainerDark,

    secondaryDark = secondaryDark,
    onSecondaryDark = onSecondaryDark,
    secondaryContainerDark = secondaryContainerDark,
    onSecondaryContainerDark = onSecondaryContainerDark,

    tertiaryDark = tertiaryDark,
    onTertiaryDark = onTertiaryDark,
    tertiaryContainerDark = tertiaryContainerDark,
    onTertiaryContainerDark = onTertiaryContainerDark,

    errorDark = errorDark,
    onErrorDark = onErrorDark,
    errorContainerDark = errorContainerDark,
    onErrorContainerDark = onErrorContainerDark,

    backgroundDark = backgroundDark,
    onBackgroundDark = onBackgroundDark,
    surfaceDark = secondaryDark,
    onSurfaceDark = onSurfaceDark,
    surfaceVariantDark = surfaceVariantDark,
    onSurfaceVariantDark = onSurfaceVariantDark,

    outlineDark = outlineDark
)

fun ColorStyle.toColorStyleEntity(): ColorStyleEntity = ColorStyleEntity(
    id = id,
    name = name,
    type = type.name,

    primaryLight = primaryLight,
    onPrimaryLight = onPrimaryLight,
    primaryContainerLight = primaryContainerLight,
    onPrimaryContainerLight = onPrimaryContainerLight,

    secondaryLight = secondaryLight,
    onSecondaryLight = onSecondaryLight,
    secondaryContainerLight = secondaryContainerLight,
    onSecondaryContainerLight = onSecondaryContainerLight,

    tertiaryLight = tertiaryLight,
    onTertiaryLight = onTertiaryLight,
    tertiaryContainerLight = tertiaryContainerLight,
    onTertiaryContainerLight = onTertiaryContainerLight,

    errorLight = errorLight,
    onErrorLight = onErrorLight,
    errorContainerLight = errorContainerLight,
    onErrorContainerLight = onErrorContainerLight,

    backgroundLight = backgroundLight,
    onBackgroundLight = onBackgroundLight,
    surfaceLight = surfaceLight,
    onSurfaceLight = onSurfaceLight,
    surfaceVariantLight = surfaceVariantLight,
    onSurfaceVariantLight = onSurfaceVariantLight,

    outlineLight = outlineLight,


    primaryDark = primaryDark,
    onPrimaryDark = onPrimaryDark,
    primaryContainerDark = primaryContainerDark,
    onPrimaryContainerDark = onPrimaryContainerDark,

    secondaryDark = secondaryDark,
    onSecondaryDark = onSecondaryDark,
    secondaryContainerDark = secondaryContainerDark,
    onSecondaryContainerDark = onSecondaryContainerDark,

    tertiaryDark = tertiaryDark,
    onTertiaryDark = onTertiaryDark,
    tertiaryContainerDark = tertiaryContainerDark,
    onTertiaryContainerDark = onTertiaryContainerDark,

    errorDark = errorDark,
    onErrorDark = onErrorDark,
    errorContainerDark = errorContainerDark,
    onErrorContainerDark = onErrorContainerDark,

    backgroundDark = backgroundDark,
    onBackgroundDark = onBackgroundDark,
    surfaceDark = secondaryDark,
    onSurfaceDark = onSurfaceDark,
    surfaceVariantDark = surfaceVariantDark,
    onSurfaceVariantDark = onSurfaceVariantDark,

    outlineDark = outlineDark
)