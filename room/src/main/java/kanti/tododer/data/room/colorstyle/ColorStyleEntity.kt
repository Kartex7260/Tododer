package kanti.tododer.data.room.colorstyle

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "color_style")
data class ColorStyleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val type: String = "",

    @ColumnInfo(name = "primary_light") val primaryLight: Long = 0,
    @ColumnInfo(name = "on_primary_light") val onPrimaryLight: Long = 0,
    @ColumnInfo(name = "primary_container_light") val primaryContainerLight: Long = 0,
    @ColumnInfo(name = "on_primary_container_light") val onPrimaryContainerLight: Long = 0,

    @ColumnInfo(name = "secondary_light") val secondaryLight: Long = 0,
    @ColumnInfo(name = "on_secondary_light") val onSecondaryLight: Long = 0,
    @ColumnInfo(name = "secondary_container_light") val secondaryContainerLight: Long = 0,
    @ColumnInfo(name = "on_secondary_container_light") val onSecondaryContainerLight: Long = 0,

    @ColumnInfo(name = "tertiary_light") val tertiaryLight: Long = 0,
    @ColumnInfo(name = "on_tertiary_light") val onTertiaryLight: Long = 0,
    @ColumnInfo(name = "tertiary_container_light") val tertiaryContainerLight: Long = 0,
    @ColumnInfo(name = "on_tertiary_container_light") val onTertiaryContainerLight: Long = 0,

    @ColumnInfo(name = "error_light") val errorLight: Long = 0,
    @ColumnInfo(name = "on_error_light") val onErrorLight: Long = 0,
    @ColumnInfo(name = "error_container_light") val errorContainerLight: Long = 0,
    @ColumnInfo(name = "on_error_container_light") val onErrorContainerLight: Long = 0,

    @ColumnInfo(name = "background_light") val backgroundLight: Long = 0,
    @ColumnInfo(name = "on_background_light") val onBackgroundLight: Long = 0,
    @ColumnInfo(name = "surface_light") val surfaceLight: Long = 0,
    @ColumnInfo(name = "on_surface_light") val onSurfaceLight: Long = 0,
    @ColumnInfo(name = "surface_variant_light") val surfaceVariantLight: Long = 0,
    @ColumnInfo(name = "on_surface_variant_light") val onSurfaceVariantLight: Long = 0,

    @ColumnInfo(name = "outline_light") val outlineLight: Long = 0,


    @ColumnInfo(name = "primary_dark") val primaryDark: Long = 0,
    @ColumnInfo(name = "on_primary_dark") val onPrimaryDark: Long = 0,
    @ColumnInfo(name = "primary_container_dark") val primaryContainerDark: Long = 0,
    @ColumnInfo(name = "on_primary_container_dark") val onPrimaryContainerDark: Long = 0,

    @ColumnInfo(name = "secondary_dark") val secondaryDark: Long = 0,
    @ColumnInfo(name = "on_secondary_dark") val onSecondaryDark: Long = 0,
    @ColumnInfo(name = "secondary_container_dark") val secondaryContainerDark: Long = 0,
    @ColumnInfo(name = "on_secondary_container_dark") val onSecondaryContainerDark: Long = 0,

    @ColumnInfo(name = "tertiary_dark") val tertiaryDark: Long = 0,
    @ColumnInfo(name = "on_tertiary_dark") val onTertiaryDark: Long = 0,
    @ColumnInfo(name = "tertiary_container_dark") val tertiaryContainerDark: Long = 0,
    @ColumnInfo(name = "on_tertiary_container_dark") val onTertiaryContainerDark: Long = 0,

    @ColumnInfo(name = "error_dark") val errorDark: Long = 0,
    @ColumnInfo(name = "on_error_dark") val onErrorDark: Long = 0,
    @ColumnInfo(name = "error_container_dark") val errorContainerDark: Long = 0,
    @ColumnInfo(name = "on_error_container_dark") val onErrorContainerDark: Long = 0,

    @ColumnInfo(name = "background_dark") val backgroundDark: Long = 0,
    @ColumnInfo(name = "on_background_dark") val onBackgroundDark: Long = 0,
    @ColumnInfo(name = "surface_dark") val surfaceDark: Long = 0,
    @ColumnInfo(name = "on_surface_dark") val onSurfaceDark: Long = 0,
    @ColumnInfo(name = "surface_variant_dark") val surfaceVariantDark: Long = 0,
    @ColumnInfo(name = "on_surface_variant_dark") val onSurfaceVariantDark: Long = 0,

    @ColumnInfo(name = "outline_dark") val outlineDark: Long = 0
)
