package kanti.tododer.data.colorstyle

import javax.annotation.concurrent.Immutable

interface ColorStyleData {

    val id: Int
    val name: String
    val type: ColorStyleType
}

@Immutable
interface ColorStyle : ColorStyleData {

    val primaryLight: Long get() = 0
    val onPrimaryLight: Long get() = 0
    val primaryContainerLight: Long get() = 0
    val onPrimaryContainerLight: Long get() = 0

    val secondaryLight: Long get() = 0
    val onSecondaryLight: Long get() = 0
    val secondaryContainerLight: Long get() = 0
    val onSecondaryContainerLight: Long get() = 0

    val tertiaryLight: Long get() = 0
    val onTertiaryLight: Long get() = 0
    val tertiaryContainerLight: Long get() = 0
    val onTertiaryContainerLight: Long get() = 0

    val errorLight: Long get() = 0
    val onErrorLight: Long get() = 0
    val errorContainerLight: Long get() = 0
    val onErrorContainerLight: Long get() = 0

    val backgroundLight: Long get() = 0
    val onBackgroundLight: Long get() = 0
    val surfaceLight: Long get() = 0
    val onSurfaceLight: Long get() = 0
    val surfaceVariantLight: Long get() = 0
    val onSurfaceVariantLight: Long get() = 0

    val outlineLight: Long get() = 0


    val primaryDark: Long get() = 0
    val onPrimaryDark: Long get() = 0
    val primaryContainerDark: Long get() = 0
    val onPrimaryContainerDark: Long get() = 0

    val secondaryDark: Long get() = 0
    val onSecondaryDark: Long get() = 0
    val secondaryContainerDark: Long get() = 0
    val onSecondaryContainerDark: Long get() = 0

    val tertiaryDark: Long get() = 0
    val onTertiaryDark: Long get() = 0
    val tertiaryContainerDark: Long get() = 0
    val onTertiaryContainerDark: Long get() = 0

    val errorDark: Long get() = 0
    val onErrorDark: Long get() = 0
    val errorContainerDark: Long get() = 0
    val onErrorContainerDark: Long get() = 0

    val backgroundDark: Long get() = 0
    val onBackgroundDark: Long get() = 0
    val surfaceDark: Long get() = 0
    val onSurfaceDark: Long get() = 0
    val surfaceVariantDark: Long get() = 0
    val onSurfaceVariantDark: Long get() = 0

    val outlineDark: Long get() = 0
}

enum class ColorStyleType {

    Standard,

    Red,
    Orange,
    Yellow,
    Green,
    LightBlue,
    Blue,
    Purple,

    Custom
}

private class ColorStyleImpl(
    override val id: Int,
    override val name: String,
    override val type: ColorStyleType,

    override val primaryLight: Long,
    override val onPrimaryLight: Long,
    override val primaryContainerLight: Long,
    override val onPrimaryContainerLight: Long,

    override val secondaryLight: Long,
    override val onSecondaryLight: Long,
    override val secondaryContainerLight: Long,
    override val onSecondaryContainerLight: Long,

    override val tertiaryLight: Long,
    override val onTertiaryLight: Long,
    override val tertiaryContainerLight: Long,
    override val onTertiaryContainerLight: Long,

    override val errorLight: Long,
    override val onErrorLight: Long,
    override val errorContainerLight: Long,
    override val onErrorContainerLight: Long,

    override val backgroundLight: Long,
    override val onBackgroundLight: Long,
    override val surfaceLight: Long,
    override val onSurfaceLight: Long,
    override val surfaceVariantLight: Long,
    override val onSurfaceVariantLight: Long,

    override val outlineLight: Long,


    override val primaryDark: Long,
    override val onPrimaryDark: Long,
    override val primaryContainerDark: Long,
    override val onPrimaryContainerDark: Long,

    override val secondaryDark: Long,
    override val onSecondaryDark: Long,
    override val secondaryContainerDark: Long,
    override val onSecondaryContainerDark: Long,

    override val tertiaryDark: Long,
    override val onTertiaryDark: Long,
    override val tertiaryContainerDark: Long,
    override val onTertiaryContainerDark: Long,

    override val errorDark: Long,
    override val onErrorDark: Long,
    override val errorContainerDark: Long,
    override val onErrorContainerDark: Long,

    override val backgroundDark: Long,
    override val onBackgroundDark: Long,
    override val surfaceDark: Long,
    override val onSurfaceDark: Long,
    override val surfaceVariantDark: Long,
    override val onSurfaceVariantDark: Long,

    override val outlineDark: Long,
) : ColorStyle

fun ColorStyle(
    parent: ColorStyle = DefaultColorStyles.Standard,
    id: Int = parent.id,
    name: String = parent.name,
    type: ColorStyleType = parent.type,

    primaryLight: Long = parent.primaryLight,
    onPrimaryLight: Long = parent.onPrimaryLight,
    primaryContainerLight: Long = parent.primaryContainerLight,
    onPrimaryContainerLight: Long = parent.onPrimaryContainerLight,

    secondaryLight: Long = parent.secondaryLight,
    onSecondaryLight: Long = parent.onSecondaryLight,
    secondaryContainerLight: Long = parent.secondaryContainerLight,
    onSecondaryContainerLight: Long = parent.onSecondaryContainerLight,

    tertiaryLight: Long = parent.tertiaryLight,
    onTertiaryLight: Long = parent.onTertiaryLight,
    tertiaryContainerLight: Long = parent.tertiaryContainerLight,
    onTertiaryContainerLight: Long = parent.onTertiaryContainerLight,

    errorLight: Long = parent.errorLight,
    onErrorLight: Long = parent.onErrorLight,
    errorContainerLight: Long = parent.errorContainerLight,
    onErrorContainerLight: Long = parent.onErrorContainerLight,

    backgroundLight: Long = parent.backgroundLight,
    onBackgroundLight: Long = parent.onBackgroundLight,
    surfaceLight: Long = parent.surfaceLight,
    onSurfaceLight: Long = parent.onSurfaceLight,
    surfaceVariantLight: Long = parent.surfaceVariantLight,
    onSurfaceVariantLight: Long = parent.onSurfaceVariantLight,

    outlineLight: Long = parent.outlineLight,


    primaryDark: Long = parent.primaryDark,
    onPrimaryDark: Long = parent.onPrimaryDark,
    primaryContainerDark: Long = parent.primaryContainerDark,
    onPrimaryContainerDark: Long = parent.onPrimaryContainerDark,

    secondaryDark: Long = parent.secondaryDark,
    onSecondaryDark: Long = parent.onSecondaryDark,
    secondaryContainerDark: Long = parent.secondaryContainerDark,
    onSecondaryContainerDark: Long = parent.onSecondaryContainerDark,

    tertiaryDark: Long = parent.tertiaryDark,
    onTertiaryDark: Long = parent.onTertiaryDark,
    tertiaryContainerDark: Long = parent.tertiaryContainerDark,
    onTertiaryContainerDark: Long = parent.onTertiaryContainerDark,

    errorDark: Long = parent.errorDark,
    onErrorDark: Long = parent.onErrorDark,
    errorContainerDark: Long = parent.errorContainerDark,
    onErrorContainerDark: Long = parent.onErrorContainerDark,

    backgroundDark: Long = parent.backgroundDark,
    onBackgroundDark: Long = parent.onBackgroundDark,
    surfaceDark: Long = parent.surfaceDark,
    onSurfaceDark: Long = parent.onSurfaceDark,
    surfaceVariantDark: Long = parent.surfaceVariantDark,
    onSurfaceVariantDark: Long = parent.onSurfaceVariantDark,

    outlineDark: Long = parent.outlineDark
): ColorStyle {
    return ColorStyleImpl(
        id = id,
        name = name,
        type = type,

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
        surfaceDark = surfaceDark,
        onSurfaceDark = onSurfaceDark,
        surfaceVariantDark = surfaceVariantDark,
        onSurfaceVariantDark = onSurfaceVariantDark,

        outlineDark = outlineDark
    )
}
