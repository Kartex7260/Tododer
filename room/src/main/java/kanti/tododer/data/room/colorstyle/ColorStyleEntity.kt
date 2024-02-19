package kanti.tododer.data.room.colorstyle

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "color_style")
data class ColorStyleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val type: String,

    @ColumnInfo(name = "primary_light") val primaryLight: Long,
    @ColumnInfo(name = "on_primary_light") val onPrimaryLight: Long,
    @ColumnInfo(name = "primary_container_light") val primaryContainerLight: Long,
    @ColumnInfo(name = "on_primary_container_light") val onPrimaryContainerLight: Long,

    @ColumnInfo(name = "secondary_light") val secondaryLight: Long,
    @ColumnInfo(name = "on_secondary_light") val onSecondaryLight: Long,
    @ColumnInfo(name = "secondary_container_light") val secondaryContainerLight: Long,
    @ColumnInfo(name = "on_secondary_container_light") val onSecondaryContainerLight: Long,

    @ColumnInfo(name = "tertiary_light") val tertiaryLight: Long,
    @ColumnInfo(name = "on_tertiary_light") val onTertiaryLight: Long,
    @ColumnInfo(name = "tertiary_container_light") val tertiaryContainerLight: Long,
    @ColumnInfo(name = "on_tertiary_container_light") val onTertiaryContainerLight: Long,

    @ColumnInfo(name = "error_light") val errorLight: Long,
    @ColumnInfo(name = "on_error_light") val onErrorLight: Long,
    @ColumnInfo(name = "error_container_light") val errorContainerLight: Long,
    @ColumnInfo(name = "on_error_container_light") val onErrorContainerLight: Long,

    @ColumnInfo(name = "background_light") val backgroundLight: Long,
    @ColumnInfo(name = "on_background_light") val onBackgroundLight: Long,
    @ColumnInfo(name = "surface_light") val surfaceLight: Long,
    @ColumnInfo(name = "on_surface_light") val onSurfaceLight: Long,
    @ColumnInfo(name = "surface_variant_light") val surfaceVariantLight: Long,
    @ColumnInfo(name = "on_surface_variant_light") val onSurfaceVariantLight: Long,

    @ColumnInfo(name = "outline_light") val outlineLight: Long,


    @ColumnInfo(name = "primary_dark") val primaryDark: Long,
    @ColumnInfo(name = "on_primary_dark") val onPrimaryDark: Long,
    @ColumnInfo(name = "primary_container_dark") val primaryContainerDark: Long,
    @ColumnInfo(name = "on_primary_container_dark") val onPrimaryContainerDark: Long,

    @ColumnInfo(name = "secondary_dark") val secondaryDark: Long,
    @ColumnInfo(name = "on_secondary_dark") val onSecondaryDark: Long,
    @ColumnInfo(name = "secondary_container_dark") val secondaryContainerDark: Long,
    @ColumnInfo(name = "on_secondary_container_dark") val onSecondaryContainerDark: Long,

    @ColumnInfo(name = "tertiary_dark") val tertiaryDark: Long,
    @ColumnInfo(name = "on_tertiary_dark") val onTertiaryDark: Long,
    @ColumnInfo(name = "tertiary_container_dark") val tertiaryContainerDark: Long,
    @ColumnInfo(name = "on_tertiary_container_dark") val onTertiaryContainerDark: Long,

    @ColumnInfo(name = "error_dark") val errorDark: Long,
    @ColumnInfo(name = "on_error_dark") val onErrorDark: Long,
    @ColumnInfo(name = "error_container_dark") val errorContainerDark: Long,
    @ColumnInfo(name = "on_error_container_dark") val onErrorContainerDark: Long,

    @ColumnInfo(name = "background_dark") val backgroundDark: Long,
    @ColumnInfo(name = "on_background_dark") val onBackgroundDark: Long,
    @ColumnInfo(name = "surface_dark") val surfaceDark: Long,
    @ColumnInfo(name = "on_surface_dark") val onSurfaceDark: Long,
    @ColumnInfo(name = "surface_variant_dark") val surfaceVariantDark: Long,
    @ColumnInfo(name = "on_surface_variant_dark") val onSurfaceVariantDark: Long,

    @ColumnInfo(name = "outline_dark") val outlineDark: Long
)
